package com.bloodnbonesgaming.topography.common;

import java.util.List;
import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.commands.ModCommands;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.GlobalConfig;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.world.gen.ScriptFeature;
import com.bloodnbonesgaming.topography.common.world.gen.feature.RegionFeatureRedirector;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureRedirectorConfig;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

public class CommonEventHandler {
	
//	@SubscribeEvent(priority = EventPriority.HIGHEST)
//	public void OnServerAboutToStart(FMLServerAboutToStartEvent event) {
//		
//		String line = FileHelper.readLineFromFile(event.getServer().anvilConverterForAnvilFile.getWorldDir().toString() + "/topography.txt");//TODO Check file existence first
//		
////		if (line != null) {
//			Topography.getLog().info("Read preset: " + line);
//			long seed = ((ServerWorldInfo)event.getServer().serverConfig).generatorSettings.getSeed();
//			Topography.getLog().info("CE " + seed);
//			boolean generateStructures = true;
//			
//			//Reload everything for the world starting
////			ConfigurationManager.init();
////			ConfigurationManager.getGlobalConfig().setPreset(line);
//			Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
//			
//			if (preset != null) {
//				Topography.getLog().info("Using preset: " + preset.internalID);
//				Impl impl = event.getServer().field_240767_f_;
//				
//				RegistryHelper.UpdateRegistries(impl);
////				preset.readDimensionDefs();
//				//Make new registry instead of reusing the current one^
//				//DynamicRegistries.Impl impl = DynamicRegistries.func_239770_b_();
//				
//				//SimpleRegistry<Dimension> registry = DimensionType.getDefaultSimpleRegistry(impl.getRegistry(Registry.DIMENSION_TYPE_KEY), impl.getRegistry(Registry.BIOME_KEY), impl.getRegistry(Registry.NOISE_SETTINGS_KEY), seed);
//				SimpleRegistry<Dimension> registry = ((ServerWorldInfo)event.getServer().serverConfig).generatorSettings.func_236224_e_();
//				for (ResourceLocation location : registry.keySet()) {
//					Topography.getLog().info("key: " + location);//No overworld? When is it added?
//				}
//				
//				for (Entry<ResourceLocation, DimensionDef> entry : preset.defs.entrySet()) {
//					ResourceLocation location = entry.getKey();
//					RegistryKey<Dimension> key = RegistryKey.getOrCreateKey(Registry.DIMENSION_KEY, location);
//					Dimension oldDim = registry.getValueForKey(key);
//					ChunkGenerator chunkGen = entry.getValue().getChunkGenerator(seed, impl.getRegistry(Registry.BIOME_KEY));
//					
//					if (chunkGen == null) {
////						continue;
//						if (oldDim != null) {
//							chunkGen = oldDim.getChunkGenerator();//Does this need a new copy with the new seed? Shouldn't the seed be the same?
//						} else {
//							//TODO Add default chunk generator
//							chunkGen = new ChunkGeneratorVoid(new SingleBiomeProvider(impl.getRegistry(Registry.BIOME_KEY).getOrThrow(Biomes.PLAINS)), () -> { return new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745, 0.9999999814507745, 80.0, 160.0), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false);}, seed);
//							//chunkGen = new ChunkGeneratorVoid(new SingleBiomeProvider(impl.getRegistry(Registry.BIOME_KEY).getOrThrow(Biomes.PLAINS)), () -> { return new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false);}, seed) ;
//						}
//					}
//
//					Supplier<DimensionType> typeSupplier;
//					
//					if (oldDim != null) {
//						typeSupplier = oldDim.getDimensionTypeSupplier();
//					} else {
//						DimensionType dimType = new DimensionTypeTopography(preset, OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, 256, ColumnFuzzedBiomeMagnifier.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), new ResourceLocation("overworld"), 0.0F);
//						event.getServer().field_240767_f_.getRegistry(Registry.DIMENSION_TYPE_KEY).register(RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, entry.getKey()), dimType, Lifecycle.stable());
//						typeSupplier = () -> {
//							//return DimensionType.func_236019_a_();
//							return dimType;
//						};
//					}
//					Topography.getLog().info("Registering dimension: " + key.getLocation());
//					registry.register(key, new Dimension(typeSupplier, chunkGen), Lifecycle.stable());
//				}
//				((ServerWorldInfo)event.getServer().serverConfig).generatorSettings = new DimensionGeneratorSettings(seed,
//						generateStructures, false, registry);
//				
//				
////				for (Entry<RegistryKey<Biome>, Biome> entry : impl.getRegistry(Registry.BIOME_KEY).getEntries()) {
////					entry.getValue().getGenerationSettings().getFeatures().get(GenerationStage.Decoration.UNDERGROUND_ORES.ordinal()).add(() -> {
////						return Feature.ORE.withConfiguration(new OreFeatureConfig(AlwaysTrueRuleTest.INSTANCE, Blocks.GOLD_BLOCK.getDefaultState(), 8)).func_242733_d(16).func_242728_a();
////					});
////				}
//			}
////		}
//		
////		Topography.getLog().info("ServerAboutToStart");
////		long seed = ((ServerWorldInfo)event.getServer().field_240768_i_).field_237343_c_.func_236221_b_();
////		boolean generateStructures = false;
////		
////		SimpleRegistry<Dimension> registry = DimensionType.func_236022_a_(seed);
////        registry.register(Dimension.field_236053_b_, new Dimension(() -> {
////            return DimensionType.func_236019_a_();
////        }, new ChunkGeneratorVoid(new SingleBiomeProvider(Biomes.PLAINS), new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false, Optional.empty()), seed)));
////   	  	
////        ((ServerWorldInfo)event.getServer().field_240768_i_).field_237343_c_ = new DimensionGeneratorSettings(seed, generateStructures, false, registry);
////
////		Topography.getLog().info("Settings Replaced");
//		
//	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onBiomeLoading(BiomeLoadingEvent event) {
		for (Decoration stage : Decoration.values()) {
			event.getGeneration().getFeatures(stage).add(() -> {
				return RegionFeatureRedirector.INSTANCE.withConfiguration(new RegionFeatureRedirectorConfig(stage));
				//return FeatureHelper.buildConfiguredFeature("topography:region_feature_redirector", NoFeatureConfig.field_236559_b_);
			});
		}
		
		//Topography.getLog().info("BiomeLoadingEvent " + event.getName());
		try {
			GlobalConfig global = ConfigurationManager.getGlobalConfig();
			
			if (global != null) {
				Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
				
				if (preset != null) {
//					BiomeHelper.addOre(event, BlockHelper.getState("minecraft:gold_block"), 8, 16);
//					List<Consumer<Event>> events = preset.scriptEventSubscribers.get(ForgeEvents.BiomeLoadingEvent);
//					
//					if (events != null) {
//						for (Consumer<Event> scriptEvent : events) {
//							scriptEvent.accept(event);
//						}
//					}
//					preset.fireEventSubscribers(ForgeEvents.BiomeLoadingEvent, event);
					//preset.fireEventSubscribers("BiomeLoadingEvent", event);
									
//					for (Supplier<StructureFeature<?, ?>> structure : event.getGeneration().getStructures()) {
//						ResourceLocation location = RegistryHelper.getStructureRegistry().getKey(structure.get().field_236268_b_);
//						
//						if (new ResourceLocation("mineshaft").equals(location)) {
//							
//						}
////					}
//					Iterator<Supplier<StructureFeature<?, ?>>> iterator = event.getGeneration().getStructures().iterator();
//					
//					while (iterator.hasNext()) {
//						Supplier<StructureFeature<?, ?>> supplier = iterator.next();
//						ResourceLocation location = ForgeRegistries.STRUCTURE_FEATURES.getKey(supplier.get().field_236268_b_);
//						
//						if (new ResourceLocation("mineshaft").equals(location)) {
//							iterator.remove();
//						}
//					}
				}
				
				//Adds a feature object for every decoration stage which calls all features added via script. This has to be done, as features must be added before the CreateWorld gui is opened.
				for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
					List<Supplier<ConfiguredFeature<?, ?>>> features = event.getGeneration().getFeatures(stage);
					features.add(() -> {
						return new ScriptFeature(NoFeatureConfig.field_236558_a_, stage).withConfiguration(new NoFeatureConfig());
					});
				}
			}
		}
		catch(Exception e) {
			Topography.getLog().error("Script error: ", e);
		}
//		event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> {
//			return Feature.ORE.withConfiguration(new OreFeatureConfig(AlwaysTrueRuleTest.INSTANCE, Blocks.GOLD_BLOCK.getDefaultState(), 8)).func_242733_d(16).func_242728_a();
//		});
//		BiomeHelper.addFeature(event, GenerationStage.Decoration.UNDERGROUND_ORES, () -> {
//			return Feature.ORE.withConfiguration(new OreFeatureConfig(AlwaysTrueRuleTest.INSTANCE, Blocks.GOLD_BLOCK.getDefaultState(), 8)).func_242733_d(16).func_242728_a();
//		});
	}
	
	@SubscribeEvent
	public void onServerStopped(FMLServerStoppedEvent event) {
		ConfigurationManager.getGlobalConfig().clean();;
	}
	
	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent event) {
		ModCommands.registerCommands(event.getDispatcher());
	}
	
	@SubscribeEvent
	public void onDataPackRegistriesReload(AddReloadListenerEvent event) {
		//event.addListener(listener);
	}
	
	@SubscribeEvent
    public void onGetPotentialSpawns(final WorldEvent.PotentialSpawns event)
    {
		if (event.getWorld().getChunk(event.getPos()).isEmptyBetween(0, 255)) {
			event.setCanceled(true);
		}
    }
}
