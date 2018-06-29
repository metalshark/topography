package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.event.ClientEventSubscriber;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerEventHandlers()
    {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(new ClientEventSubscriber());
    }
}
