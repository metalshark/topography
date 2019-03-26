package com.bloodnbonesgaming.topography.event;

import com.bloodnbonesgaming.topography.client.gui.worldbookcompat.GuiWorldBookOverride;

import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldBookEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
    public void onOpenGui(final GuiOpenEvent event)
    {
		if (event.getGui() instanceof GuiWorldSelection && !(event.getGui() instanceof GuiWorldBookOverride))
        {
        	event.setGui(new GuiWorldBookOverride(((GuiWorldSelection)event.getGui()).prevScreen));
        }
    }
}
