package com.bloodnbonesgaming.randomgenskyislands.proxy;

import com.bloodnbonesgaming.randomgenskyislands.config.ConfigurationManager;
import com.bloodnbonesgaming.randomgenskyislands.event.EventSubscriber;

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