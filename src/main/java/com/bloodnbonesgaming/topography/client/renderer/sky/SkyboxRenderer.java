package com.bloodnbonesgaming.topography.client.renderer.sky;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.client.TextureDataObject;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

@ScriptClassDocumentation(documentationFile = ModInfo.DOCUMENTATION_FOLDER + "SkyboxRenderer", classExplaination = 
"This file is for the SkyboxRenderer. This is for making a custom skybox using SkyRendererCustom.")
public class SkyboxRenderer extends SkyRenderObject {
	
	private final SkyboxTextureDataObject singleTexture;
	private final TextureDataObject topTexture;
	private final TextureDataObject bottomTexture;
	private final TextureDataObject northTexture;
	private final TextureDataObject southTexture;
	private final TextureDataObject eastTexture;
	private final TextureDataObject westTexture;
	
	private final Map<MinMaxBounds, MinMaxBounds> alphaModifiers = new LinkedHashMap<MinMaxBounds, MinMaxBounds>();
	
	public SkyboxRenderer(final String singleTexture)
	{
		this.singleTexture = new SkyboxTextureDataObject(new ResourceLocation(singleTexture));
		this.topTexture = null;
		this.bottomTexture = null;
		this.northTexture = null;
		this.southTexture = null;
		this.eastTexture = null;
		this.westTexture = null;
	}
	
	public SkyboxRenderer(final String topTexture, final String bottomTexture, final String northTexture, final String southTexture, final String eastTexture, final String westTexture)
	{
		this.topTexture = new TextureDataObject(new ResourceLocation(topTexture));
		this.bottomTexture = new TextureDataObject(new ResourceLocation(bottomTexture));
		this.northTexture = new TextureDataObject(new ResourceLocation(northTexture));
		this.southTexture = new TextureDataObject(new ResourceLocation(southTexture));
		this.eastTexture = new TextureDataObject(new ResourceLocation(eastTexture));
		this.westTexture = new TextureDataObject(new ResourceLocation(westTexture));
		this.singleTexture = null;
	}
	
	@ScriptMethodDocumentation(args = "MinMaxBounds, MinMaxBounds", usage = "angle bounds, alpha for transition", notes = "Adds paired celestial angle bounds and alpha. Used to create a blended skybox transition as time passes.")
	public void addAlpha(final MinMaxBounds angle, final MinMaxBounds alpha)
	{
		this.alphaModifiers.put(angle, alpha);
	}

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {

		if (this.singleTexture != null)
		{
			this.renderSingleTexture(partialTicks, world, mc);
		}
		else
		{
			this.renderSeparateTextures(partialTicks, world, mc);
		}
	}
	
	
	public void renderSeparateTextures(float partialTicks, WorldClient world, Minecraft mc) {
		
		GlStateManager.disableFog();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL11.GL_ALWAYS, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
		GlStateManager.pushMatrix();
		final float celestialAngle = world.getCelestialAngle(partialTicks);
		float alpha = 1.0F;
		
		for (final Entry<MinMaxBounds, MinMaxBounds> entry : this.alphaModifiers.entrySet())
		{
//			Topography.instance.getLog().info("looping " + entry.getKey().min + " " + entry.getKey().max + " " + celestialAngle);
			if (entry.getKey().test(celestialAngle))
			{
//				Topography.instance.getLog().info("In Bounds");
				final MinMaxBounds key = entry.getKey();
				final MinMaxBounds value = entry.getValue();
				
				if (value.min != null && value.max != null)
				{
//					Topography.instance.getLog().info("Neither null");
					float diff = key.max - key.min;
					float distIntoRange = celestialAngle - key.min;
					float percent = distIntoRange / diff;
					
					if (value.min > value.max)
					{
//						Topography.instance.getLog().info("Min greater than max");
						float alphaDiff = value.min - value.max;
						alpha = value.min - alphaDiff * percent;
//						Topography.instance.getLog().info(alpha);
					}
					else
					{
//						Topography.instance.getLog().info("Max greater than min");
						float alphaDiff = value.max - value.min;
						alpha = value.min + alphaDiff * percent;
//						Topography.instance.getLog().info(alpha);
					}
					break;
				}
			}
		}
		//Time rotation
		GlStateManager.rotate(celestialAngle * 360.0F, 0.0F, 0.0F, 1.0F);
		
		//Bottom
		{
			this.bottomTexture.bind();

			double xStartPos = 0 / this.bottomTexture.getWidth();
			double yStartPos = 0 / this.bottomTexture.getHeight();
			double xEndPos = this.bottomTexture.getWidth() / this.bottomTexture.getWidth();
			double yEndPos = this.bottomTexture.getHeight() / this.bottomTexture.getHeight();

			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();

			tessellator.draw();
		}
		
		//Top
		{
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			this.topTexture.bind();

			double xStartPos = 0 / this.topTexture.getWidth();
			double yStartPos = 0 / this.topTexture.getHeight();
			double xEndPos = this.topTexture.getWidth() / this.topTexture.getWidth();
			double yEndPos = this.topTexture.getHeight() / this.topTexture.getHeight();

			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();

			tessellator.draw();
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		}
		
		//North
		{
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			this.northTexture.bind();

			double xStartPos = 0 / this.northTexture.getWidth();
			double yStartPos = 0 / this.northTexture.getHeight();
			double xEndPos = this.northTexture.getWidth() / this.northTexture.getWidth();
			double yEndPos = this.northTexture.getHeight() / this.northTexture.getHeight();
	
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	
			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
	
			tessellator.draw();
		}
		
		//South
		{
			GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			this.southTexture.bind();

			double xStartPos = 0 / this.southTexture.getWidth();
			double yStartPos = 0 / this.southTexture.getHeight();
			double xEndPos = this.southTexture.getWidth() / this.southTexture.getWidth();
			double yEndPos = this.southTexture.getHeight() / this.southTexture.getHeight();
	
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	
			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
	
			tessellator.draw();
		}
		
		//East
		{
			GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
			this.eastTexture.bind();

			double xStartPos = 0 / this.eastTexture.getWidth();
			double yStartPos = 0 / this.eastTexture.getHeight();
			double xEndPos = this.eastTexture.getWidth() / this.eastTexture.getWidth();
			double yEndPos = this.eastTexture.getHeight() / this.eastTexture.getHeight();
	
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	
			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
	
			tessellator.draw();
		}
		
		//West
		{
			GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			this.westTexture.bind();

			double xStartPos = 0 / this.westTexture.getWidth();
			double yStartPos = 0 / this.westTexture.getHeight();
			double xEndPos = this.westTexture.getWidth() / this.westTexture.getWidth();
			double yEndPos = this.westTexture.getHeight() / this.westTexture.getHeight();
	
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	
			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
	
			tessellator.draw();
		}
		
		GlStateManager.popMatrix();

		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
//        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.alphaFunc(516, 0.5F);
//		GlStateManager.disableAlpha();
		GlStateManager.enableFog();
        GlStateManager.enableTexture2D();
        GlStateManager.color(0.0F, 0.0F, 0.0F);
	}
	
	
	public void renderSingleTexture(float partialTicks, WorldClient world, Minecraft mc) {
		
		GlStateManager.disableFog();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL11.GL_ALWAYS, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.depthMask(false);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
		GlStateManager.pushMatrix();
		final float celestialAngle = world.getCelestialAngle(partialTicks);
		float alpha = 1.0F;
		
		for (final Entry<MinMaxBounds, MinMaxBounds> entry : this.alphaModifiers.entrySet())
		{
//			Topography.instance.getLog().info("looping " + entry.getKey().min + " " + entry.getKey().max + " " + celestialAngle);
			if (entry.getKey().test(celestialAngle))
			{
//				Topography.instance.getLog().info("In Bounds");
				final MinMaxBounds key = entry.getKey();
				final MinMaxBounds value = entry.getValue();
				
				if (value.min != null && value.max != null)
				{
//					Topography.instance.getLog().info("Neither null");
					float diff = key.max - key.min;
					float distIntoRange = celestialAngle - key.min;
					float percent = distIntoRange / diff;
					
					if (value.min > value.max)
					{
//						Topography.instance.getLog().info("Min greater than max");
						float alphaDiff = value.min - value.max;
						alpha = value.min - alphaDiff * percent;
//						Topography.instance.getLog().info(alpha);
					}
					else
					{
//						Topography.instance.getLog().info("Max greater than min");
						float alphaDiff = value.max - value.min;
						alpha = value.min + alphaDiff * percent;
//						Topography.instance.getLog().info(alpha);
					}
					break;
				}
			}
		}
		//Time rotation
		GlStateManager.rotate(celestialAngle * 360.0F, 0.0F, 0.0F, 1.0F);
		final double width = this.singleTexture.getWidth() / 4;
		final double height = this.singleTexture.getHeight() / 3;
		
		//Bottom
		{
			this.singleTexture.bind();
			
			double xStartPos = width / this.singleTexture.getWidth();
	        double yStartPos = height * 2 / this.singleTexture.getHeight();
	        double xEndPos = width * 2 / this.singleTexture.getWidth();
	        double yEndPos = height * 3 / this.singleTexture.getHeight();

			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();

			tessellator.draw();
		}
		
		//Top
		{
			GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
			
			double xStartPos = width / this.singleTexture.getWidth();
	        double yStartPos = 0 / this.singleTexture.getHeight();
	        double xEndPos = width * 2 / this.singleTexture.getWidth();
	        double yEndPos = height / this.singleTexture.getHeight();

			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();

			tessellator.draw();
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		}
		
		//North
		{
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			
			double xStartPos = width / this.singleTexture.getWidth();
	        double yStartPos = height / this.singleTexture.getHeight();
	        double xEndPos = width * 2 / this.singleTexture.getWidth();
	        double yEndPos = height * 2 / this.singleTexture.getHeight();
//	        Topography.instance.getLog().info(xStartPos + " " + yStartPos + " " + xEndPos + " " + yEndPos + " " + width + " " + height);
	
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	
			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
	
			tessellator.draw();
		}
		
		//South
		{
			GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			
			double xStartPos = width * 3 / this.singleTexture.getWidth();
	        double yStartPos = height * 1 / this.singleTexture.getHeight();
	        double xEndPos = width * 4 / this.singleTexture.getWidth();
	        double yEndPos = height * 2 / this.singleTexture.getHeight();
	
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	
			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
	
			tessellator.draw();
		}
		
		//East
		{
			GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
			
			double xStartPos = width * 2 / this.singleTexture.getWidth();
	        double yStartPos = height * 1 / this.singleTexture.getHeight();
	        double xEndPos = width * 3 / this.singleTexture.getWidth();
	        double yEndPos = height * 2 / this.singleTexture.getHeight();
	
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	
			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
	
			tessellator.draw();
		}
		
		//West
		{
			GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
			
			double xStartPos = 0 / this.singleTexture.getWidth();
	        double yStartPos = height * 1 / this.singleTexture.getHeight();
	        double xEndPos = width / this.singleTexture.getWidth();
	        double yEndPos = height * 2 / this.singleTexture.getHeight();
	
			bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
	
			// Top left corner
			bufferbuilder.pos(-100.0D, -100.0D, 100.0D).tex(xStartPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Top right corner
			bufferbuilder.pos(100.0D, -100.0D, 100.0D).tex(xEndPos, yEndPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom right corner
			bufferbuilder.pos(100.0D, -100.0D, -100.0D).tex(xEndPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
			// Bottom left corner
			bufferbuilder.pos(-100.0D, -100.0D, -100.0D).tex(xStartPos, yStartPos).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
	
			tessellator.draw();
		}
		
		GlStateManager.popMatrix();

		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
//        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.alphaFunc(516, 0.5F);
//		GlStateManager.disableAlpha();
		GlStateManager.enableFog();
        GlStateManager.enableTexture2D();
        GlStateManager.color(0.0F, 0.0F, 0.0F);
	}

}
