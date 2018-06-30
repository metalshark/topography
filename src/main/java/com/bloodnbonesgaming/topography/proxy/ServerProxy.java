package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.event.ServerEventSubscriber;

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
