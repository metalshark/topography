package com.bloodnbonesgaming.topography.proxy;

import java.io.File;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.event.ClientEventSubscriber;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.command.FunctionObject;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
                            ConfigurationManager.setup();
                            ConfigurationManager.setGeneratorSettings(settings);
                            ConfigurationManager.getInstance().registerDimensions();
                            
//                    		final File regionFile = new File("./saves/" + server.getFolderName() + "/region");
//                    		
//                    		if (!regionFile.exists() || regionFile.listFiles() == null)
//                    		{
//                    			Topography.instance.getLog().info("Region folder doesn't exist or has no files.");
//                    			
//                    			final ResourceLocation function = ConfigurationManager.getInstance().getPreset().getInitialServerFunction();
//                    			
//                    			if (function != null)
//                    			{
//                                	FunctionObject functionobject = server.getFunctionManager().getFunction(function);
//                                	server.getFunctionManager().execute(functionobject, CommandSenderWrapper.create(server).computePositionVector().withPermissionLevel(2).withSendCommandFeedback(false));
//                    			}
//                    		}
                        }
                    }
                }
            }
        }
    }
}
