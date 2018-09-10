package com.bloodnbonesgaming.topography.event;

import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.WorldType;
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
            WorldTypeCustomizable.gui = (GuiCreateWorld) event.getGui();
            
            if (ConfigurationManager.getInstance().defaultWorldType())
            {
                for (int i = 0; i < WorldType.WORLD_TYPES.length; i++)
                {
                    if (WorldType.WORLD_TYPES[i] instanceof WorldTypeCustomizable)
                    {
                        ((GuiCreateWorld) event.getGui()).selectedIndex = i;
                        break;
                    }
                }
            }
        }
    }
}
