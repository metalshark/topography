package com.bloodnbonesgaming.topography.client.renderer;

import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.IRenderHandler;

public class TopographyWeatherRenderer extends IRenderHandler {
	
	private boolean persistentSnow = false;
	private boolean persistentRain = false;
	private boolean persistentClear = false;
	
	private float persistentStrength = -1;
	
	private ResourceLocation snowTexture = EntityRenderer.SNOW_TEXTURES;
	private ResourceLocation rainTexture = EntityRenderer.RAIN_TEXTURES;
	
	private float redSnow = 1;
	private float greenSnow = 1;
	private float blueSnow = 1;
	
	private float redRain = 1;
	private float greenRain = 1;
	private float blueRain = 1;
	
	@ScriptMethodDocumentation(usage = "", notes = "Makes snowfall permanently render, regardless of whether or not it's actually snowing, or if the biome allows snow.")
	public void persistentSnow()
	{
		this.persistentSnow = true;
		this.persistentStrength = 100;
	}
	
	public void persistentSnow(final float strength)
	{
		this.persistentSnow = true;
		this.persistentStrength = strength;
	}
	
	@ScriptMethodDocumentation(usage = "", notes = "Makes rainfall permanently render, regardless of whether or not it's actually raining, or if the biome allows rain.")
	public void persistentRain()
	{
		this.persistentRain = true;
		this.persistentStrength = 100;
	}
	
	public void persistentRain(final float strength)
	{
		this.persistentRain = true;
		this.persistentStrength = strength;
	}
	
	public void persistentClear()
	{
		this.persistentClear = true;
	}
	
	@ScriptMethodDocumentation(args = "int", usage = "color hex", notes = "Sets the color to render rain.")
	public void setRainColor(final int color)
	{
		this.redRain = ((color >> 16) & 255) / 255F;
		this.greenRain = ((color >> 8) & 255) / 255F;
		this.blueRain = (color & 255) / 255F;
	}
	
	@ScriptMethodDocumentation(args = "int", usage = "color hex", notes = "Sets the color to render snow.")
	public void setSnowColor(final int color)
	{
		this.redSnow = ((color >> 16) & 255) / 255F;
		this.greenSnow = ((color >> 8) & 255) / 255F;
		this.blueSnow = (color & 255) / 255F;
	}
	
	@ScriptMethodDocumentation(args = "String", usage = "texture resource location", notes = "Sets the texture to use for rendering rain.")
	public void setRainTexture(final String string)
	{
		this.rainTexture = new ResourceLocation(string);
	}
	
	@ScriptMethodDocumentation(args = "String", usage = "texture resource location", notes = "Sets the texture to use for rendering snow.")
	public void setSnowTexture(final String string)
	{
		this.snowTexture = new ResourceLocation(string);
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		
		float f = mc.world.getRainStrength(partialTicks);
		
		if (persistentStrength >= 0)
		{
			f = persistentStrength;
		}
        if (!persistentClear && f > 0.0F)
        {
        	mc.entityRenderer.enableLightmap();
            Entity entity = mc.getRenderViewEntity();
            int i = MathHelper.floor(entity.posX);
            int j = MathHelper.floor(entity.posY);
            int k = MathHelper.floor(entity.posZ);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.disableCull();
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.alphaFunc(516, 0.1F);
            double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
            double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
            double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
            int l = MathHelper.floor(d1);
            int i1 = 5;

            if (mc.gameSettings.fancyGraphics)
            {
                i1 = 10;
            }

            int j1 = -1;
            float f1 = (float)mc.entityRenderer.rendererUpdateCount + partialTicks;
            bufferbuilder.setTranslation(-d0, -d1, -d2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k1 = k - i1; k1 <= k + i1; ++k1)
            {
                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    int i2 = (k1 - k + 16) * 32 + l1 - i + 16;
                    double d3 = (double)mc.entityRenderer.rainXCoords[i2] * 0.5D;
                    double d4 = (double)mc.entityRenderer.rainYCoords[i2] * 0.5D;
                    blockpos$mutableblockpos.setPos(l1, 0, k1);
                    Biome biome = world.getBiome(blockpos$mutableblockpos);

                    if ((biome.canRain() || biome.getEnableSnow() || persistentSnow || persistentRain))
                    {
                        int j2 = world.getPrecipitationHeight(blockpos$mutableblockpos).getY();
                        int k2 = j - i1;
                        int l2 = j + i1;

                        if (k2 < j2)
                        {
                            k2 = j2;
                        }

                        if (l2 < j2)
                        {
                            l2 = j2;
                        }

                        int i3 = j2;

                        if (j2 < l)
                        {
                            i3 = l;
                        }

                        if (k2 != l2)
                        {
                        	mc.entityRenderer.random.setSeed((long)(l1 * l1 * 3121 + l1 * 45238971 ^ k1 * k1 * 418711 + k1 * 13761));
                            blockpos$mutableblockpos.setPos(l1, k2, k1);
                            float f2 = biome.getTemperature(blockpos$mutableblockpos);

                            if (persistentRain || (world.getBiomeProvider().getTemperatureAtHeight(f2, j2) >= 0.15F && !persistentSnow))
                            {
                                if (j1 != 0)
                                {
                                    if (j1 >= 0)
                                    {
                                        tessellator.draw();
                                    }

                                    j1 = 0;
                                    mc.getTextureManager().bindTexture(rainTexture);
                                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d5 = -((double)(mc.entityRenderer.rendererUpdateCount + l1 * l1 * 3121 + l1 * 45238971 + k1 * k1 * 418711 + k1 * 13761 & 31) + (double)partialTicks) / 32.0D * (3.0D + mc.entityRenderer.random.nextDouble());
                                double d6 = (double)((float)l1 + 0.5F) - entity.posX;
                                double d7 = (double)((float)k1 + 0.5F) - entity.posZ;
                                float f3 = MathHelper.sqrt(d6 * d6 + d7 * d7) / (float)i1;
                                float f4 = ((1.0F - f3 * f3) * 0.5F + 0.5F) * f;
                                blockpos$mutableblockpos.setPos(l1, i3, k1);
                                int j3 = world.getCombinedLight(blockpos$mutableblockpos, 0);
                                int k3 = j3 >> 16 & 65535;
                                int l3 = j3 & 65535;
                                bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)k2 * 0.25D + d5).color(redRain, greenRain, blueRain, f4).lightmap(k3, l3).endVertex();
                                bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)k2 * 0.25D + d5).color(redRain, greenRain, blueRain, f4).lightmap(k3, l3).endVertex();
                                bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)l2 * 0.25D + d5).color(redRain, greenRain, blueRain, f4).lightmap(k3, l3).endVertex();
                                bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)l2 * 0.25D + d5).color(redRain, greenRain, blueRain, f4).lightmap(k3, l3).endVertex();
                            }
                            else
                            {
                                if (j1 != 1)
                                {
                                    if (j1 >= 0)
                                    {
                                        tessellator.draw();
                                    }

                                    j1 = 1;
                                    mc.getTextureManager().bindTexture(snowTexture);
                                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                                }

                                double d8 = (double)(-((float)(mc.entityRenderer.rendererUpdateCount & 511) + partialTicks) / 512.0F);
                                double d9 = mc.entityRenderer.random.nextDouble() + (double)f1 * 0.01D * (double)((float)mc.entityRenderer.random.nextGaussian());
                                double d10 = mc.entityRenderer.random.nextDouble() + (double)(f1 * (float)mc.entityRenderer.random.nextGaussian()) * 0.001D;
                                double d11 = (double)((float)l1 + 0.5F) - entity.posX;
                                double d12 = (double)((float)k1 + 0.5F) - entity.posZ;
                                float f6 = MathHelper.sqrt(d11 * d11 + d12 * d12) / (float)i1;
                                float f5 = ((1.0F - f6 * f6) * 0.3F + 0.5F) * f;
                                blockpos$mutableblockpos.setPos(l1, i3, k1);
                                int i4 = (world.getCombinedLight(blockpos$mutableblockpos, 0) * 3 + 15728880) / 4;
                                int j4 = i4 >> 16 & 65535;
                                int k4 = i4 & 65535;
                                bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D + d9, (double)k2 * 0.25D + d8 + d10).color(redSnow, greenSnow, blueSnow, f5).lightmap(j4, k4).endVertex();
                                bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D + d9, (double)k2 * 0.25D + d8 + d10).color(redSnow, greenSnow, blueSnow, f5).lightmap(j4, k4).endVertex();
                                bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D + d9, (double)l2 * 0.25D + d8 + d10).color(redSnow, greenSnow, blueSnow, f5).lightmap(j4, k4).endVertex();
                                bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D + d9, (double)l2 * 0.25D + d8 + d10).color(redSnow, greenSnow, blueSnow, f5).lightmap(j4, k4).endVertex();
                            }
                        }
                    }
                }
            }

            if (j1 >= 0)
            {
                tessellator.draw();
            }

            bufferbuilder.setTranslation(0.0D, 0.0D, 0.0D);
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            mc.entityRenderer.disableLightmap();
        }
	}
}
