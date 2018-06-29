package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.event.EventSubscriber;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
    public void registerEventHandlers()
    {
        MinecraftForge.EVENT_BUS.register(new EventSubscriber());
    }
    
    public void onServerAboutToStart()
    {
        ConfigurationManager.setup();
    }
}