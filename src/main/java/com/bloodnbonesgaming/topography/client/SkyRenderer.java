package com.bloodnbonesgaming.topography.client;

import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ISkyRenderHandler;

public class SkyRenderer implements ISkyRenderHandler {
	   @Nullable
	   private VertexBuffer starVBO;
	   @Nullable
	   private VertexBuffer skyVBO;
	   @Nullable
	   private VertexBuffer sky2VBO;
	private final VertexFormat skyVertexFormat = DefaultVertexFormats.POSITION;
	   private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
	   private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");
	
	public SkyRenderer() {
	      this.generateStars();
	      this.generateSky();
	      this.generateSky2();
	}

	   private void generateSky2() {
	      Tessellator tessellator = Tessellator.getInstance();
	      BufferBuilder bufferbuilder = tessellator.getBuffer();
	      if (this.sky2VBO != null) {
	         this.sky2VBO.close();
	      }

	      this.sky2VBO = new VertexBuffer(this.skyVertexFormat);
	      this.renderSky(bufferbuilder, -16.0F, true);
	      bufferbuilder.finishDrawing();
	      this.sky2VBO.upload(bufferbuilder);
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
	      int i = 64;
	      int j = 6;
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

	   private void generateStars() {
	      Tessellator tessellator = Tessellator.getInstance();
	      BufferBuilder bufferbuilder = tessellator.getBuffer();
	      if (this.starVBO != null) {
	         this.starVBO.close();
	      }

	      this.starVBO = new VertexBuffer(this.skyVertexFormat);
	      this.renderStars(bufferbuilder);
	      bufferbuilder.finishDrawing();
	      this.starVBO.upload(bufferbuilder);
	   }

	   private void renderStars(BufferBuilder bufferBuilderIn) {
	      Random random = new Random(10842L);
	      bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

	      for(int i = 0; i < 1500; ++i) {
	         double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
	         double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
	         double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
	         double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
	         double d4 = d0 * d0 + d1 * d1 + d2 * d2;
	         if (d4 < 1.0D && d4 > 0.01D) {
	            d4 = 1.0D / Math.sqrt(d4);
	            d0 = d0 * d4;
	            d1 = d1 * d4;
	            d2 = d2 * d4;
	            double d5 = d0 * 100.0D;
	            double d6 = d1 * 100.0D;
	            double d7 = d2 * 100.0D;
	            double d8 = Math.atan2(d0, d2);
	            double d9 = Math.sin(d8);
	            double d10 = Math.cos(d8);
	            double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
	            double d12 = Math.sin(d11);
	            double d13 = Math.cos(d11);
	            double d14 = random.nextDouble() * Math.PI * 2.0D;
	            double d15 = Math.sin(d14);
	            double d16 = Math.cos(d14);

	            for(int j = 0; j < 4; ++j) {
	               double d17 = 0.0D;
	               double d18 = (double)((j & 2) - 1) * d3;
	               double d19 = (double)((j + 1 & 2) - 1) * d3;
	               double d20 = 0.0D;
	               double d21 = d18 * d16 - d19 * d15;
	               double d22 = d19 * d16 + d18 * d15;
	               double d23 = d21 * d12 + 0.0D * d13;
	               double d24 = 0.0D * d12 - d21 * d13;
	               double d25 = d24 * d9 - d22 * d10;
	               double d26 = d22 * d9 + d24 * d10;
	               bufferBuilderIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
	            }
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
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.depthMask(false);
        RenderSystem.enableFog();
        RenderSystem.color3f(f, f1, f2);
        this.skyVBO.bindBuffer();
        this.skyVertexFormat.setupBufferState(0L);
        this.skyVBO.draw(matrixStack.getLast().getMatrix(), 7);
        VertexBuffer.unbindBuffer();
        this.skyVertexFormat.clearBufferState();
        RenderSystem.disableFog();
        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float[] afloat = world.func_239132_a_().func_230492_a_(world.func_242415_f(partialTicks), partialTicks);
        if (afloat != null) {
           RenderSystem.disableTexture();
           RenderSystem.shadeModel(7425);
           matrixStack.push();
           matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
           float f3 = MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F;
           matrixStack.rotate(Vector3f.ZP.rotationDegrees(f3));
           matrixStack.rotate(Vector3f.ZP.rotationDegrees(90.0F));
           float f4 = afloat[0];
           float f5 = afloat[1];
           float f6 = afloat[2];
           Matrix4f matrix4f = matrixStack.getLast().getMatrix();
           bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
           bufferbuilder.pos(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();
           int i = 16;

           for(int j = 0; j <= 16; ++j) {
              float f7 = (float)j * ((float)Math.PI * 2F) / 16.0F;
              float f8 = MathHelper.sin(f7);
              float f9 = MathHelper.cos(f7);
              bufferbuilder.pos(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
           }

           bufferbuilder.finishDrawing();
           WorldVertexBufferUploader.draw(bufferbuilder);
           matrixStack.pop();
           RenderSystem.shadeModel(7424);
        }

        RenderSystem.enableTexture();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        matrixStack.push();
        float f11 = 1.0F - world.getRainStrength(partialTicks);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, f11);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(world.func_242415_f(partialTicks) * 360.0F));
        Matrix4f matrix4f1 = matrixStack.getLast().getMatrix();
        float f12 = 30.0F;
        mc.textureManager.bindTexture(SUN_TEXTURES);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(matrix4f1, -f12, 100.0F, -f12).tex(0.0F, 0.0F).endVertex();
        bufferbuilder.pos(matrix4f1, f12, 100.0F, -f12).tex(1.0F, 0.0F).endVertex();
        bufferbuilder.pos(matrix4f1, f12, 100.0F, f12).tex(1.0F, 1.0F).endVertex();
        bufferbuilder.pos(matrix4f1, -f12, 100.0F, f12).tex(0.0F, 1.0F).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        f12 = 20.0F;
        mc.textureManager.bindTexture(MOON_PHASES_TEXTURES);
        int k = world.getMoonPhase();
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f13 = (float)(l + 0) / 4.0F;
        float f14 = (float)(i1 + 0) / 2.0F;
        float f15 = (float)(l + 1) / 4.0F;
        float f16 = (float)(i1 + 1) / 2.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(matrix4f1, -f12, -100.0F, f12).tex(f15, f16).endVertex();
        bufferbuilder.pos(matrix4f1, f12, -100.0F, f12).tex(f13, f16).endVertex();
        bufferbuilder.pos(matrix4f1, f12, -100.0F, -f12).tex(f13, f14).endVertex();
        bufferbuilder.pos(matrix4f1, -f12, -100.0F, -f12).tex(f15, f14).endVertex();
        bufferbuilder.finishDrawing();
        WorldVertexBufferUploader.draw(bufferbuilder);
        RenderSystem.disableTexture();
        float f10 = world.getStarBrightness(partialTicks) * f11;
        if (f10 > 0.0F) {
           RenderSystem.color4f(f10, f10, f10, f10);
           this.starVBO.bindBuffer();
           this.skyVertexFormat.setupBufferState(0L);
           this.starVBO.draw(matrixStack.getLast().getMatrix(), 7);
           VertexBuffer.unbindBuffer();
           this.skyVertexFormat.clearBufferState();
        }

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableFog();
        matrixStack.pop();
        RenderSystem.disableTexture();
        RenderSystem.color3f(0.0F, 0.0F, 0.0F);
        double d0 = mc.player.getEyePosition(partialTicks).y - world.getWorldInfo().getVoidFogHeight();
        if (d0 < 0.0D) {
           matrixStack.push();
           matrixStack.translate(0.0D, 12.0D, 0.0D);
           this.sky2VBO.bindBuffer();
           this.skyVertexFormat.setupBufferState(0L);
           this.sky2VBO.draw(matrixStack.getLast().getMatrix(), 7);
           VertexBuffer.unbindBuffer();
           this.skyVertexFormat.clearBufferState();
           matrixStack.pop();
        }

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
