package com.bloodnbonesgaming.topography.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.FileHelper;

public class GlobalConfig {
	
	public final Map<String, Preset> presets = new ConcurrentHashMap<String, Preset>();
	private Preset currentPreset = null;
	private String guiBackground = null;
	
	public Preset getPreset() {
		synchronized(presets) {
			return currentPreset;
		}
	}
	
	public void setPreset(String preset) {
		synchronized(presets) {
			this.currentPreset = this.presets.get(preset);
			Topography.getLog().info("Preset set to: " + (currentPreset != null ? currentPreset.internalID : "null"));
		}
	}
	
	public void init() {
		read();
	}
	
	public void clean() {
		
	}
	
	private void read() {
		Topography.getLog().info("Reading Javascript");
		final File scriptFolder = new File(ModInfo.CONFIG_FOLDER);
		
		if (!scriptFolder.exists())
		{
			scriptFolder.mkdirs();
			scriptFolder.mkdir();
		}
		
		final ScriptEngineManager factory = new ScriptEngineManager(null);
		
		final File scriptFile = new File(ModInfo.CONFIG_FOLDER + "Topography.js");    	
		
		ScriptEngine engine = factory.getEngineByName("nashorn");
		
		try (BufferedReader reader = FileHelper.openReader(scriptFile))
		{
			engine.put("Config", this);
			engine.eval("var ForgeEvents = Java.type(\"com.bloodnbonesgaming.topography.common.util.ForgeEvents\")");
			engine.eval("var Preset = Java.type(\"com.bloodnbonesgaming.topography.common.config.Preset\")");
			engine.eval("var GenerationStage = Java.type(\"net.minecraft.world.gen.GenerationStage\")");
			engine.eval("var Feature = Java.type(\"net.minecraft.world.gen.feature.Feature\")");
			engine.eval("var BlockHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.BlockHelper\")");
			engine.eval("var BiomeHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.BiomeHelper\")");
			engine.eval("var OreFeatureConfig = Java.type(\"net.minecraft.world.gen.feature.OreFeatureConfig\")");
			engine.eval("var AlwaysTrueRuleTest = Java.type(\"net.minecraft.world.gen.feature.template.AlwaysTrueRuleTest\")");
			engine.eval("var ResourceLocation = Java.type(\"net.minecraft.util.ResourceLocation\")");
			engine.eval("var OreFeatureConfig = Java.type(\"net.minecraft.world.gen.feature.OreFeatureConfig\")");
			engine.eval("var ForgeRegistries = Java.type(\"net.minecraftforge.registries.ForgeRegistries\")");
			engine.eval("var FeatureHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.features.FeatureHelper\")");
			engine.eval("var OreHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.features.OreHelper\")");
			engine.eval("var EntityHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.EntityHelper\")");
			engine.eval("var ItemHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.ItemHelper\")");
			engine.eval("var Consumer = Java.type(\"java.util.function.Consumer\")");
			engine.eval("var BiomeDictionary = Java.type(\"net.minecraftforge.common.BiomeDictionary\")");
			engine.eval(reader);
			
			Invocable invocable = (Invocable)engine;
			invocable.invokeFunction("registerPresets");
			invocable.invokeFunction("setConfigOptions");
			
			
		} catch (final Exception e)
		{
			e.printStackTrace();
			Topography.getLog().error(e.getMessage());
		}
	}
	
	public Preset registerPreset(Preset preset) {
		synchronized(presets) {
			Topography.getLog().info("Registering preset: " + preset.internalID);
			this.presets.put(preset.internalID, preset);
			return preset;
		}
	}
	
	public Preset registerPreset(String internalID, String displayName, String imageLocation, String description) {
		synchronized(presets) {
			Topography.getLog().info("Registering preset: " + internalID);
			Preset preset = new Preset(internalID).displayName(displayName).image(imageLocation).description(description);
			this.presets.put(internalID, preset);
			return preset;
		}
	}
	
	public void setGuiBackground(String location) {
		this.guiBackground = location;
	}
	
	public String getGuiBackground() {
		return this.guiBackground;
	}
}
