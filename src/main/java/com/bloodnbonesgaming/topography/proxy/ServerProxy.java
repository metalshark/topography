package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.event.ServerEventSubscriber;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

public class ServerProxy extends CommonProxy
{
    @Override
    public void registerEventHandlers()
    {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(new ServerEventSubscriber());
    }
    
    @Override
    public void onServerAboutToStart(FMLServerAboutToStartEvent event)
    {
        super.onServerAboutToStart(event);
        
        if (event.getServer() instanceof DedicatedServer)
        {
            final DedicatedServer server = (DedicatedServer) event.getServer();
            
            String s1 = server.getStringProperty("level-type", "DEFAULT");
            WorldType worldType = WorldType.parseWorldType(s1);
            
            if (worldType instanceof WorldTypeCustomizable)
            {
                ConfigurationManager.setup();
                
                String backup = "";
                
                for (final String name : ConfigurationManager.getInstance().getPresets().keySet())
                {
                    backup = name;
                    break;
                }
                
                final String settings = server.getStringProperty("generator-settings", backup);
                
                ConfigurationManager.getInstance().registerDimensions(settings);
            }
        }
    }
}
