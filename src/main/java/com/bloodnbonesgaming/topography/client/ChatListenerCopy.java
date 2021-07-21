package com.bloodnbonesgaming.topography.client;

import java.util.UUID;

import net.minecraft.client.ClipboardHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public class ChatListenerCopy implements IChatListener{
	
	private final ClipboardHelper helper = new ClipboardHelper();

	@Override
	public void say(ChatType type, ITextComponent component, UUID uuid) {
		if (component.getStyle() != null)
		{
			if (component.getStyle().getClickEvent() != null)
			{
				if (component.getStyle().getClickEvent().getValue() != null)
				{
					if (component.getStyle().getClickEvent().getValue().startsWith("/clipboard"))
					{
						helper.setClipboardString(Minecraft.getInstance().getMainWindow().getHandle(), component.getUnformattedComponentText());
					}
				}
			}
		}
	}
}
