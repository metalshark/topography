package com.bloodnbonesgaming.topography.client;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.ISkyRenderHandler;

public class SkyRendererFogOnly implements ISkyRenderHandler {
	   @Nullable
	   private VertexBuffer starVBO;
	   @Nullable
	   private VertexBuffer skyVBO;
	   @Nullable
	   private VertexBuffer sky2VBO;
	private final VertexFormat skyVertexFormat = DefaultVertexFormats.POSITION;
	
	public SkyRendererFogOnly() {
	      this.generateSky();
	}

	   private void generateSky() {
	      Tessellator tessellator = Tessellator.getInstance();
	      BufferBuilder bufferbuilder = tessellator.getBuffer();
	      if (this.skyVBO != null) {
	         this.skyVBO.close();
	      }

	      this.skyVBO = new VertexBuffer(this.skyVertexFormat);
	      this.renderSky(bufferbuilder, 16.0F, false);
	      bufferbuilder.finishDrawing();
	      this.skyVBO.upload(bufferbuilder);
	   }

	   private void renderSky(BufferBuilder bufferBuilderIn, float posY, boolean reverseX) {
	      bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

	      for(int k = -384; k <= 384; k += 64) {
	         for(int l = -384; l <= 384; l += 64) {
	            float f = (float)k;
	            float f1 = (float)(k + 64);
	            if (reverseX) {
	               f1 = (float)k;
	               f = (float)(k + 64);
	            }

	            bufferBuilderIn.pos((double)f, (double)posY, (double)l).endVertex();
	            bufferBuilderIn.pos((double)f1, (double)posY, (double)l).endVertex();
	            bufferBuilderIn.pos((double)f1, (double)posY, (double)(l + 64)).endVertex();
	            bufferBuilderIn.pos((double)f, (double)posY, (double)(l + 64)).endVertex();
	         }
	      }

	   }

	@Override
	public void render(int ticks, float partialTicks, MatrixStack matrixStack, ClientWorld world, Minecraft mc) {
		RenderSystem.disableTexture();
        Vector3d vector3d = world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), partialTicks);
        vector3d = new Vector3d(0.1, 0.1, 0.1);
        float f = (float)vector3d.x;
        float f1 = (float)vector3d.y;
        float f2 = (float)vector3d.z;
        FogRenderer.applyFog();
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color3f(f, f1, f2);
        this.skyVBO.bindBuffer();
        this.skyVertexFormat.setupBufferState(0L);
        this.skyVBO.draw(matrixStack.getLast().getMatrix(), 7);
        VertexBuffer.unbindBuffer();
        this.skyVertexFormat.clearBufferState();

        if (world.func_239132_a_().func_239216_b_()) {
           RenderSystem.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
        } else {
           RenderSystem.color3f(f, f1, f2);
        }

        RenderSystem.enableTexture();
        RenderSystem.depthMask(true);
        RenderSystem.disableFog();
	}

}
