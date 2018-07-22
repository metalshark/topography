package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.event.ClientEventSubscriber;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraft.client.Minecraft;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.WorldType;
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
                        
            WorldType worldType = server.worldSettings.getTerrainType();
            
            if (worldType instanceof WorldTypeCustomizable)
            {
                ISaveHandler isavehandler = Minecraft.getMinecraft().getSaveLoader().getSaveLoader(server.getFolderName(), false);
                WorldInfo worldinfo = isavehandler.loadWorldInfo();
                
                if (worldinfo != null)
                {
                    String settings = worldinfo.getGeneratorOptions();ConfigurationManager.setup();
                    
                    if (settings.isEmpty())
                    {
                        for (final String name : ConfigurationManager.getInstance().getPresets().keySet())
                        {
                            settings = name;
                            break;
                        }
                    }
                    server.worldSettings.setGeneratorOptions(settings);
                    
                    ConfigurationManager.setGeneratorSettings(settings);
                    ConfigurationManager.getInstance().registerDimensions();
                }
            }
        }
    }
}
