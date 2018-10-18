package com.bloodnbonesgaming.topography.client.renderer.sky;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class SunRenderer extends SkyRenderObject{

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		
		GlStateManager.disableFog();
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		// Sun & Moon
		{
			// Sun
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
					GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.pushMatrix();
			float f16 = 1.0F - world.getRainStrength(partialTicks);
			GlStateManager.color(1.0F, 1.0F, 1.0F, f16);
			GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
			float f17 = 30.0F;
			mc.getTextureManager().bindTexture(new ResourceLocation("textures/environment/sun.png"));
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos((double) (-f17), 100.0D, (double) (-f17)).tex(0.0D, 0.0D).endVertex();
			bufferbuilder.pos((double) f17, 100.0D, (double) (-f17)).tex(1.0D, 0.0D).endVertex();
			bufferbuilder.pos((double) f17, 100.0D, (double) f17).tex(1.0D, 1.0D).endVertex();
			bufferbuilder.pos((double) (-f17), 100.0D, (double) f17).tex(0.0D, 1.0D).endVertex();
			tessellator.draw();
			// Moon
			f17 = 20.0F;
			mc.getTextureManager().bindTexture(new ResourceLocation("textures/environment/moon_phases.png"));
			int k1 = world.getMoonPhase();
			int i2 = k1 % 4;
			int k2 = k1 / 4 % 2;
			float f22 = (float) (i2 + 0) / 4.0F;
			float f23 = (float) (k2 + 0) / 2.0F;
			float f24 = (float) (i2 + 1) / 4.0F;
			float f14 = (float) (k2 + 1) / 2.0F;
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
			bufferbuilder.pos((double) (-f17), -100.0D, (double) f17).tex((double) f24, (double) f14).endVertex();
			bufferbuilder.pos((double) f17, -100.0D, (double) f17).tex((double) f22, (double) f14).endVertex();
			bufferbuilder.pos((double) f17, -100.0D, (double) (-f17)).tex((double) f22, (double) f23).endVertex();
			bufferbuilder.pos((double) (-f17), -100.0D, (double) (-f17)).tex((double) f24, (double) f23).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}

		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.disableFog();
	}
	
}
