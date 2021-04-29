package com.bloodnbonesgaming.topography.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.FileHelper;
import com.bloodnbonesgaming.topography.common.util.Functions.QuadFunction;
import com.bloodnbonesgaming.topography.common.util.Functions.TriFunction;
import com.bloodnbonesgaming.topography.common.util.Functions.VarArgTriFunction;
import com.bloodnbonesgaming.topography.common.util.IOHelper;
import com.bloodnbonesgaming.topography.common.world.gen.GenerationHandler;
import com.bloodnbonesgaming.topography.common.world.gen.GenerationHandler.EnumGenerationPhase;
import com.bloodnbonesgaming.topography.common.world.gen.IGenerator;
import com.bloodnbonesgaming.topography.common.world.gen.feature.RegionFeature;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureConfig;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.registries.ForgeRegistries;

public class DimensionDef {

	private final Invocable js;
	public Template spawnStructure;
	public int spawnStructureHeight = 64;
	public final CarverHandler carverHandler = new CarverHandler();
	public final GenerationHandler genHandler = new GenerationHandler();
	public final Map<GenerationStage.Decoration, List<ConfiguredFeature<?, ?>>> features = new HashMap<GenerationStage.Decoration, List<ConfiguredFeature<?, ?>>>();
	//Script event subscribers
	private final Map<String, List<Consumer<Event>>> scriptEventSubscribers = new HashMap<String, List<Consumer<Event>>>();
	private String guiBackground = null;
	public final Map<String, StructureSeparationSettings> structureSpacingMap = new HashMap<String, StructureSeparationSettings>();
	private DimensionType  dimensionType = null;
	public final Map<Decoration, List<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>> regionFeatures = new HashMap<Decoration, List<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>>();
	private float minGamma = 0;
	private float maxGamma = 1;

	public DimensionDef(Invocable js) {
		this.js = js;
	}

	public ChunkGenerator getChunkGenerator(long seed, Registry<Biome> biomeRegistry, Registry<DimensionSettings> dimensionSettingsRegistry) {
		try {
			return (ChunkGenerator) this.js.invokeFunction("buildChunkGenerator", seed, biomeRegistry, dimensionSettingsRegistry);
		} catch (NoSuchMethodException e) {
			//Ignore
		}
		catch (Exception e) {
			Topography.getLog().error("Exception building ChunkGenerator: ", e);
		}
		return null;//Null should use default
	}

	public void onRegistryUpdated(long seed) {
		try {
			this.js.invokeFunction("onRegistryUpdated", seed);
		} catch (Exception e) {
			Topography.getLog().error("Exception running onRegistryUpdated: " + e.getMessage());
		}
	}
	
	public DimensionDef setSpawnStructure(String path, int height) {
		this.spawnStructure = IOHelper.loadStructureTemplate(path);
		this.spawnStructureHeight = height;
		return this;
	}
	
	public DimensionDef addCarver(String location, ProbabilityConfig config) {
		WorldCarver<?> carver = ForgeRegistries.WORLD_CARVERS.getValue(new ResourceLocation(location));

		if (carver != null) {
			ConfiguredCarver<?> configuredCarver = new ConfiguredCarver(carver, config);
			
			if (!carverHandler.generateDefaultCarvers) {
				List<ConfiguredCarver<?>> list;
				
				if (carverHandler.carversPre.containsKey(GenerationStage.Carving.AIR)) {
					list = carverHandler.carversPre.get(GenerationStage.Carving.AIR);
				} else {
					list = new ArrayList<ConfiguredCarver<?>>();
					carverHandler.carversPre.put(GenerationStage.Carving.AIR, list);
				}
				list.add(configuredCarver);
			} else {
				List<ConfiguredCarver<?>> list;
				
				if (carverHandler.carversPost.containsKey(GenerationStage.Carving.AIR)) {
					list = carverHandler.carversPost.get(GenerationStage.Carving.AIR);
				} else {
					list = new ArrayList<ConfiguredCarver<?>>();
					carverHandler.carversPost.put(GenerationStage.Carving.AIR, list);
				}
				list.add(configuredCarver);
			}
		} else {
			//TODO Throw some kind of error
		}
		return this;
	}
	
	public DimensionDef disableDefaultCarvers() {
		carverHandler.disableDefaultCarvers = true;
		return this;
	}
	
	public DimensionDef addDefaultCarvers() {
		carverHandler.generateDefaultCarvers = true;
		return this;
	}
	
	public DimensionDef addLiquidCarver(String location, ProbabilityConfig config) {
		WorldCarver<?> carver = ForgeRegistries.WORLD_CARVERS.getValue(new ResourceLocation(location));

		if (carver != null) {
			ConfiguredCarver<?> configuredCarver = new ConfiguredCarver(carver, config);
			
			if (!carverHandler.generateDefaultLiquidCarvers) {
				List<ConfiguredCarver<?>> list;
				
				if (carverHandler.carversPre.containsKey(GenerationStage.Carving.LIQUID)) {
					list = carverHandler.carversPre.get(GenerationStage.Carving.LIQUID);
				} else {
					list = new ArrayList<ConfiguredCarver<?>>();
					carverHandler.carversPre.put(GenerationStage.Carving.LIQUID, list);
				}
				list.add(configuredCarver);
			} else {
				List<ConfiguredCarver<?>> list;
				
				if (carverHandler.carversPost.containsKey(GenerationStage.Carving.LIQUID)) {
					list = carverHandler.carversPost.get(GenerationStage.Carving.LIQUID);
				} else {
					list = new ArrayList<ConfiguredCarver<?>>();
					carverHandler.carversPost.put(GenerationStage.Carving.LIQUID, list);
				}
				list.add(configuredCarver);
			}
		} else {
			//TODO Throw some kind of error
		}
		return this;
	}
	
	public DimensionDef disableDefaultLiquidCarvers() {
		carverHandler.disableDefaultLiquidCarvers = true;
		return this;
	}
	
	public DimensionDef addDefaultLiquidCarvers() {
		carverHandler.generateDefaultLiquidCarvers = true;
		return this;
	}
	
	public boolean generateCarvers(ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk, GenerationStage.Carving stage) {
		return carverHandler.generate(world, generator, chunks, chunk, stage, this);
	}
	
	public DimensionDef addGenerator(GenerationHandler.EnumGenerationPhase phase, Collection<Biome> biomes, IGenerator generator) {
		phase.addGenerator(this.genHandler, biomes, generator);
		return this;
	}
	
	public DimensionDef addGenerator(GenerationHandler.EnumGenerationPhase phase, Collection<Biome> biomes, Object... args) {
		phase.addGenerator(this.genHandler, biomes, args);
		return this;
	}
	
	public DimensionDef disableDefault(GenerationHandler.EnumGenerationPhase phase, Collection<Biome> biomes) {
		phase.disableDefault(genHandler, biomes);
		return this;
	}
	
	public DimensionDef addDefault(GenerationHandler.EnumGenerationPhase phase, Collection<Biome> biomes) {
		phase.addDefault(genHandler, biomes);
		return this;
	}
	
	public boolean generate(GenerationHandler.EnumGenerationPhase phase, ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk) {
		return phase.generate(genHandler, world, generator, chunks, chunk);
	}
	
	public DimensionDef addOre(ConfiguredFeature<?, ?> feature, GenerationStage.Decoration stage) {
		if (!this.features.containsKey(stage)) {
			this.features.put(stage, new ArrayList<ConfiguredFeature<?, ?>>());
		}
		this.features.get(stage).add(feature);
		return this;
	}
	
	public DimensionDef registerEventHandler(String eventType, Consumer<Event> event) {
		if (!this.scriptEventSubscribers.containsKey(eventType)) {
			this.scriptEventSubscribers.put(eventType, new ArrayList<Consumer<Event>>());
		}
		this.scriptEventSubscribers.get(eventType).add(event);
		return this;
	}
	
	public void fireEventSubscribers(String eventType, Event event) {
		if (this.scriptEventSubscribers.containsKey(eventType)) {
			for (Consumer<Event> consumer : this.scriptEventSubscribers.get(eventType)) {
				consumer.accept(event);
			}
		}
	}
	
	public DimensionDef setGuiBackground(String location) {
		this.guiBackground = location;
		return this;
	}
	
	public String getGuiBackground() {
		return this.guiBackground;
	}
	
	public DimensionDef setStructureSpacing(String location, int spacing, int separation, int salt) {
		this.structureSpacingMap.put(location, new StructureSeparationSettings(spacing, separation, salt));
		return this;
	}
	
	public Supplier<DimensionType> getDimensionType() {
		if (this.dimensionType == null) return null; 
		return () -> {
			return this.dimensionType;
		};
	}
	
	public DimensionDef setDimensionType(DimensionType supplier) {
		this.dimensionType = supplier;
		return this;
	}
	
	public DimensionDef addRegionFeature(GenerationStage.Decoration stage, ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>> feature) {
		if (!this.regionFeatures.containsKey(stage)) {
			this.regionFeatures.put(stage, new ArrayList<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>());
		}
		this.regionFeatures.get(stage).add(feature);
		return this;
	}
	
	public DimensionDef minGamma(double gamma) {
		this.minGamma = (float) gamma;
		return this;
	}
	
	public DimensionDef maxGamma(double gamma) {
		this.maxGamma = (float) gamma;
		return this;
	}
	
	public float getMinGamma() {
		return this.minGamma;
	}
	
	public float getMaxGamma() {
		return this.maxGamma;
	}

	public static DimensionDef read(String path, ScriptEngineManager factory) throws Exception {
		final File scriptFile = new File(ModInfo.CONFIG_FOLDER + path + ".js");

		ScriptEngine engine = factory.getEngineByName("nashorn");

		try (BufferedReader reader = FileHelper.openReader(scriptFile)) {
			engine.eval("var Topography = Java.type(\"com.bloodnbonesgaming.topography.Topography\")");
			engine.eval("var RegistryHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.RegistryHelper\")");
			engine.eval("var BlockHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.BlockHelper\")");
			engine.eval("var BiomeHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.BiomeHelper\")");
			engine.eval("var DimensionHelper = Java.type(\"com.bloodnbonesgaming.topography.common.util.DimensionHelper\")");
			engine.eval("var ChunkGeneratorVoid = Java.type(\"com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorVoid\");");
			engine.eval("var ChunkGeneratorSimplexSkylands = Java.type(\"com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorSimplexSkylands\");");
			engine.eval("var ChunkGeneratorLayersFlat = Java.type(\"com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorLayersFlat\");");
			engine.eval("var SingleBiomeProvider = Java.type(\"net.minecraft.world.biome.provider.SingleBiomeProvider\");");
			engine.eval("var MultiBiomeProvider = Java.type(\"com.bloodnbonesgaming.topography.common.world.biome.provider.MultiBiomeProvider\");");
			engine.eval("var DimensionSettings = Java.type(\"net.minecraft.world.gen.DimensionSettings\");");
			engine.eval("var DimensionStructuresSettings = Java.type(\"net.minecraft.world.gen.settings.DimensionStructuresSettings\");");
			engine.eval("var NoiseSettings = Java.type(\"net.minecraft.world.gen.settings.NoiseSettings\");");
			engine.eval("var ScalingSettings = Java.type(\"net.minecraft.world.gen.settings.ScalingSettings\");");
			engine.eval("var SlideSettings = Java.type(\"net.minecraft.world.gen.settings.SlideSettings\");");
			engine.eval("var Optional = Java.type(\"java.util.Optional\");");
			engine.eval("var ProbabilityConfig = Java.type(\"net.minecraft.world.gen.feature.ProbabilityConfig\")");
			engine.eval("var CellNoiseGenerator = Java.type(\"com.bloodnbonesgaming.topography.common.world.gen.CellNoiseGenerator\")");
			engine.eval("var ChunkGeneratorNoiseTopo = Java.type(\"com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorNoiseTopo\")");
			engine.eval("var GenerationPhase = Java.type(\"com.bloodnbonesgaming.topography.common.world.gen.GenerationHandler$EnumGenerationPhase\")");
			engine.eval("var GenerationStage = Java.type(\"net.minecraft.world.gen.GenerationStage\")");
			engine.eval("var BiomeDictionary = Java.type(\"net.minecraftforge.common.BiomeDictionary\")");
			engine.eval("var ForgeRegistries = Java.type(\"net.minecraftforge.registries.ForgeRegistries\")");
			engine.eval("var ResourceLoction = Java.type(\"net.minecraft.util.ResourceLocation\")");
			engine.eval("var ForgeEvents = Java.type(\"com.bloodnbonesgaming.topography.common.util.ForgeEvents\")");
			engine.eval("var Util = Java.type(\"com.bloodnbonesgaming.topography.common.util.Util\")");
			DimensionDef def = new DimensionDef((Invocable) engine);
			engine.put("setSpawnStructure", (BiFunction<String, Integer, DimensionDef>)def::setSpawnStructure);
			engine.put("addCarver", (BiFunction<String, ProbabilityConfig, DimensionDef>)def::addCarver);
			engine.put("addLiquidCarver", (BiFunction<String, ProbabilityConfig, DimensionDef>)def::addLiquidCarver);
			engine.put("addDefaultCarvers", (Supplier<DimensionDef>)def::addDefaultCarvers);
			engine.put("addDefaultLiquidCarvers", (Supplier<DimensionDef>)def::addDefaultLiquidCarvers);
			engine.put("disableDefaultCarvers", (Supplier<DimensionDef>)def::disableDefaultCarvers);
			engine.put("disableDefaultLiquidCarvers", (Supplier<DimensionDef>)def::disableDefaultLiquidCarvers);
			engine.put("addGenerator", (TriFunction<EnumGenerationPhase, Collection<Biome>, IGenerator, DimensionDef>)def::addGenerator);
			engine.put("addGenerator", (VarArgTriFunction<EnumGenerationPhase, Collection<Biome>, Object, DimensionDef>)def::addGenerator);
			engine.put("disableDefault", (BiFunction<EnumGenerationPhase, Collection<Biome>, DimensionDef>)def::disableDefault);
			engine.put("addDefault", (BiFunction<EnumGenerationPhase, Collection<Biome>, DimensionDef>)def::addDefault);
			engine.put("addOre", (BiFunction<ConfiguredFeature<?, ?>, GenerationStage.Decoration, DimensionDef>)def::addOre);
			engine.put("registerEventHandler", (BiFunction<String, Consumer<Event>, DimensionDef>)def::registerEventHandler);
			engine.put("setStructureSpacing", (QuadFunction<String, Integer, Integer, Integer, DimensionDef>)def::setStructureSpacing);
			engine.put("setDimensionType", (Function<DimensionType, DimensionDef>)def::setDimensionType);
			engine.put("addRegionFeature", (BiFunction<GenerationStage.Decoration, ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>, DimensionDef>)def::addRegionFeature);
			engine.put("minGamma", (Function<Double, DimensionDef>)def::minGamma);
			engine.put("maxGamma", (Function<Double, DimensionDef>)def::maxGamma);
			engine.eval(reader);

			return def;
//			return new DimensionDef((Invocable) engine);

		} catch (final Exception e) {
			throw (e);
		}
	}
}
