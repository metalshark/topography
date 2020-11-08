package com.bloodnbonesgaming.topography.client;

import java.util.UUID;

import com.bloodnbonesgaming.topography.Topography;

import net.minecraft.client.ClipboardHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

public class ChatListenerCopy implements IChatListener{
	
	private final ClipboardHelper helper = new ClipboardHelper();

	@Override
	public void say(ChatType type, ITextComponent component, UUID uuid) {
		Topography.getLog().info("Chat listener: " + component.getUnformattedComponentText());
		if (component.getStyle() != null)
		{
			Topography.getLog().info("style");
			if (component.getStyle().getClickEvent() != null)
			{
				Topography.getLog().info("event");
				if (component.getStyle().getClickEvent().getValue() != null)
				{
					Topography.getLog().info("value" + component.getStyle().getClickEvent().getValue());
					if (component.getStyle().getClickEvent().getValue().startsWith("/clipboard"))
					{
						Topography.getLog().info("copy");
						helper.setClipboardString(Minecraft.getInstance().getMainWindow().getHandle(), component.getUnformattedComponentText());
						//ClipboardHelper.copyToClipboard(component.getUnformattedComponentText());
					}
				}
			}
		}
	}
}
