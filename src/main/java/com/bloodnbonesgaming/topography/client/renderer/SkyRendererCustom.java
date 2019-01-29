package com.bloodnbonesgaming.topography.client.renderer;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.client.renderer.sky.SkyRenderObject;
import com.bloodnbonesgaming.topography.client.renderer.sky.SkyboxRenderer;
import com.bloodnbonesgaming.topography.client.renderer.sky.SunRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

@ScriptClassDocumentation(documentationFile = ModInfo.DOCUMENTATION_FOLDER + "SkyRendererCustom", classExplaination = 
"This file is for the SkyRendererCustom. This is for making a custom sky renderer.")
public class SkyRendererCustom  extends IRenderHandler {

	private final List<SkyRenderObject> renderObjects = new ArrayList<SkyRenderObject>();
	
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		
		for (final SkyRenderObject render : this.renderObjects)
		{
			render.render(partialTicks, world, mc);
		}
	}
	
	@ScriptMethodDocumentation(args = "String", usage = "texture location", notes = "Adds a SkyboxRenderer to the sky renderer with the provided texture location and returns it. The texture is used to generate a skybox in the standard format.")
	public SkyboxRenderer addSkybox(final String singleTexture)
	{
		final SkyboxRenderer skybox = new SkyboxRenderer(singleTexture);
		this.renderObjects.add(skybox);
		return skybox;
	}
	
	public SkyboxRenderer addSkybox(final String topTexture, final String bottomTexture, final String northTexture, final String southTexture, final String eastTexture, final String westTexture)
	{
		final SkyboxRenderer skybox = new SkyboxRenderer(topTexture, bottomTexture, northTexture, southTexture, eastTexture, westTexture);
		this.renderObjects.add(skybox);
		return skybox;
	}
	
	@ScriptMethodDocumentation(usage = "", notes = "Adds the vanilla sun and moon to the sky renderer. Currently cannot set a custom sun/moon texture except by making it part of the skybox texture.")
	public SunRenderer addSunMoon()
	{
		final SunRenderer sun = new SunRenderer();
		this.renderObjects.add(sun);
		return sun;
	}

}
