package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.lib.util.JsonHelper;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.event.ClientEventSubscriber;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerEventHandlers()
    {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(new ClientEventSubscriber());
    }
    
    @Override
    public void onServerAboutToStart(FMLServerAboutToStartEvent event)
    {
        super.onServerAboutToStart(event);
        
        if (event.getServer() instanceof IntegratedServer)
        {
            final IntegratedServer server = (IntegratedServer) event.getServer();
                        
//            WorldType worldType = server.worldSettings.getTerrainType();
//            
//            if (worldType instanceof WorldTypeCustomizable)
            if (ConfigurationManager.getInstance() != null)
            {
                ISaveHandler isavehandler = Minecraft.getMinecraft().getSaveLoader().getSaveLoader(server.getFolderName(), false);
                WorldInfo worldinfo = isavehandler.loadWorldInfo();
                
                if (worldinfo != null)
                {
                    String settings = worldinfo.getGeneratorOptions();
                    
                    final JsonParser parser = new JsonParser();
                    Topography.instance.getLog().info("reading json " + settings);
                    JsonElement element = parser.parse(settings);
                    if (element.isJsonObject())
                    {
                        JsonObject obj = (JsonObject) element;
                        JsonElement member = obj.get("Topography-Preset");
                        if (member != null)
                        {
                            settings = member.getAsString();
                        }
                    }
                    ConfigurationManager.setup();
                    
                    if (settings.isEmpty())
                    {
                        for (final String name : ConfigurationManager.getInstance().getPresets().keySet())
                        {
                            settings = name;
                            break;
                        }
                    }
//                    server.worldSettings.setGeneratorOptions(settings);
//                    Topography.instance.getLog().info("Replacing WorldType");
//                    server.worldSettings.terrainType = WorldType.parseWorldType("compactsky");
                    
                    ConfigurationManager.setGeneratorSettings(settings);
                    ConfigurationManager.getInstance().registerDimensions();
                }
            }
        }
    }
}
