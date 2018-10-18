package com.bloodnbonesgaming.topography.client.renderer;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.topography.client.renderer.sky.SkyRenderObject;
import com.bloodnbonesgaming.topography.client.renderer.sky.SkyboxRenderer;
import com.bloodnbonesgaming.topography.client.renderer.sky.SunRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

public class SkyRendererCustom  extends IRenderHandler {

	private final List<SkyRenderObject> renderObjects = new ArrayList<SkyRenderObject>();
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		
		for (final SkyRenderObject render : this.renderObjects)
		{
			render.render(partialTicks, world, mc);
		}
	}
	
	public SkyboxRenderer addSkybox(final String topTexture, final String bottomTexture, final String northTexture, final String southTexture, final String eastTexture, final String westTexture)
	{
		final SkyboxRenderer skybox = new SkyboxRenderer(topTexture, bottomTexture, northTexture, southTexture, eastTexture, westTexture);
		this.renderObjects.add(skybox);
		return skybox;
	}
	
	public SunRenderer addSunMoon()
	{
		final SunRenderer sun = new SunRenderer();
		this.renderObjects.add(sun);
		return sun;
	}

}
