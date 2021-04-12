package com.bloodnbonesgaming.topography.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.client.ISkyRenderHandler;

public class SkyRendererBlank implements ISkyRenderHandler {

	@Override
	public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc) {
		
	}

}
