package com.bloodnbonesgaming.topography.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ClientUtil {
	
	public Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}
	
	public World getWorld() {
		return getMinecraft().world;
	}
}
