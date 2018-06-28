package com.bloodnbonesgaming.randomgenskyislands.event;

import com.bloodnbonesgaming.randomgenskyislands.config.ConfigurationManager;

import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventSubscriber
{
    @SubscribeEvent
    public void onOpenGui(final GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiCreateWorld)
        {
            ConfigurationManager.setup();
        }
    }
}
