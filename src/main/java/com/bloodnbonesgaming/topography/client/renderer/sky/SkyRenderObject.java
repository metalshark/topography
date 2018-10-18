package com.bloodnbonesgaming.topography.client.renderer.sky;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

public abstract class SkyRenderObject {

	public abstract void render(float partialTicks, WorldClient world, Minecraft mc);
}
