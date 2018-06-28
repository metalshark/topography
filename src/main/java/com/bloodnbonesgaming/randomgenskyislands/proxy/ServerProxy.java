package com.bloodnbonesgaming.randomgenskyislands.proxy;

import com.bloodnbonesgaming.randomgenskyislands.config.ConfigurationManager;
import com.bloodnbonesgaming.randomgenskyislands.event.ServerEventSubscriber;

import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy
{
    @Override
    public void registerEventHandlers()
    {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(new ServerEventSubscriber());
    }
    
    @Override
    public void onServerAboutToStart()
    {
        super.onServerAboutToStart();
    }
}
