package com.bloodnbonesgaming.topography.client.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiElementTextureStretch extends GuiElementTexture {
	public GuiElementTextureStretch(EnumGuiLocation location, ResourceLocation texture) {
		super(location, texture);
	}

	public GuiElementTextureStretch(final EnumGuiLocation location, final ResourceLocation texture,
			final int imageWidth, final int imageHeight) {
		super(location, texture, imageWidth, imageHeight);
	}

	@Override
	protected void drawTexture(int x, int y, int texWidth, int texHeight, int width, int height, float texStartX, float texStartY, float texEndX, float texEndY) {
		float xStartPos = texStartX / texWidth;
		float yStartPos = texStartY / texHeight;
		float xEndPos = texEndX / texWidth;
		float yEndPos = texEndY / texHeight;

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double) (x + 0), (double) (y + height), 0.0).tex(xStartPos, yEndPos).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + height), 0.0).tex(xEndPos, yEndPos).endVertex();
		bufferbuilder.pos((double) (x + width), (double) (y + 0), 0.0).tex(xEndPos, yStartPos).endVertex();
		bufferbuilder.pos((double) (x + 0), (double) (y + 0), 0.0).tex(xStartPos, yStartPos).endVertex();
		tessellator.draw();
	}
}
