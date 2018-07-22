package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.event.ClientEventSubscriber;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.world.WorldType;
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
                String settings = server.worldSettings.getGeneratorOptions();
                
                ConfigurationManager.setup();
                
                if (settings.isEmpty())
                {
                    for (final String name : ConfigurationManager.getInstance().getPresets().keySet())
                    {
                        settings = name;
                        break;
                    }
                }
                server.worldSettings.setGeneratorOptions(settings);
                ConfigurationManager.getInstance().registerDimensions(settings);
            }
        }
    }
}
