package com.bloodnbonesgaming.randomgenskyislands.proxy;

import com.bloodnbonesgaming.randomgenskyislands.event.ClientEventSubscriber;

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
