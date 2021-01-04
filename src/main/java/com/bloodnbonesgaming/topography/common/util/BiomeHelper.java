package com.bloodnbonesgaming.topography.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.Topography;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.AlwaysTrueRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeHelper {
	
	public static Biome getBiome(String location) throws Exception {
		try {
			return Topography.proxy.getRegistries().getRegistry(Registry.BIOME_KEY).getOrThrow(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, new ResourceLocation(location)));
		} catch (Exception e) {
			throw new Exception("Could not get biome: " + location + " " + e);
		}
		
//		Biome biome = ForgeRegistries.BIOMES.getValue(new ResourceLocation(location));
//		
//		if (biome == null) {
//			throw new Exception("Biome not found");
//		}
//		return biome;
	}
	
	public static Biome getBiome(RegistryKey<Biome> key) throws Exception {
		return Topography.proxy.getRegistries().getRegistry(Registry.BIOME_KEY).getOrThrow(key);
	}
	
	public static List<Biome> allBiomes() {
		List<Biome> biomes = new ArrayList<Biome>();
		for (Entry<RegistryKey<Biome>, Biome> entry : Topography.proxy.getRegistries().getRegistry(Registry.BIOME_KEY).getEntries()) {
			biomes.add(entry.getValue());
		}
		return biomes;
//		Collection<Biome> biomes = ForgeRegistries.BIOMES.getValues();
//		return biomes;
	}
	
	public static List<Biome> forBiomes(String... biomes) throws Exception {
		List<Biome> biomeList = new ArrayList<Biome>();
		
		for (String string : biomes) {
			Biome biome = getBiome(string);
			
			if (biome != null) {
				biomeList.add(biome);
			}
		}
		return biomeList;
	}
	
	public static List<Biome> forBiomes(Biome... biomes) {
		List<Biome> biomeList = new ArrayList<Biome>();
		
		for (Biome biome : biomes) {
			if (biome != null) {
				biomeList.add(biome);
			}
		}
		return biomeList;
	}
	
	public static List<Biome> forOverworld() throws Exception {
		List<Biome> biomeList = new ArrayList<Biome>();
		Set<RegistryKey<Biome>> biomeSet = BiomeDictionary.getBiomes(Type.OVERWORLD);
		
		for (RegistryKey<Biome> key : biomeSet) {
			biomeList.add(BiomeHelper.getBiome(key));
		}
		return biomeList;
	}
	
	public static List<Biome> withoutRivers(List<Biome> biomes) {
		List<Biome> biomeList = new ArrayList<Biome>();
		
		for (Biome biome : biomes) {
			RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
			
			if (!BiomeDictionary.hasType(key, BiomeDictionary.Type.RIVER)) {
				biomeList.add(biome);
			}
		}
		return biomeList;
	}
	
	public static List<Biome> withoutOceans(List<Biome> biomes) {
		List<Biome> biomeList = new ArrayList<Biome>();
		
		for (Biome biome : biomes) {
			RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
			
			if (!BiomeDictionary.hasType(key, BiomeDictionary.Type.OCEAN)) {
				biomeList.add(biome);
			}
		}
		return biomeList;
	}
	
	public static List<Biome> withoutTypes(List<Biome> biomes, BiomeDictionary.Type... types) {
		List<Biome> biomeList = new ArrayList<Biome>();
		
		biomeLoop:
		for (Biome biome : biomes) {
			RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
			
			for (BiomeDictionary.Type type : types) {
				if (BiomeDictionary.hasType(key, type)) {
					continue biomeLoop;
				}
			}
			biomeList.add(biome);
		}
		return biomeList;
	}
	
	public static List<Biome> withoutBiomes(List<Biome> biomes, String... toRemove) throws Exception {
		List<Biome> biomeList = new ArrayList<Biome>();
		//Add all the original biomes
		biomeList.addAll(biomes);
		//Get biomes to remove
		List<Biome> biomesToRemove = BiomeHelper.forBiomes(toRemove);
		//Remove biomes from list
		biomeList.removeAll(biomesToRemove);
		return biomeList;
	}
	
//	public static void addFeature(BiomeLoadingEvent event, GenerationStage.Decoration stage, Supplier<ConfiguredFeature<?, ?>> feature) {
//		event.getGeneration().getFeatures(stage).add(feature);
//	}
	
//	public ConfiguredFeature<?, ?> withConfiguration(Feature<?> feature, IFeatureConfig config) {
//	      return new ConfiguredFeature<?, ?>(feature, config);
//	   }
//	
//	public static void addOre(BiomeLoadingEvent event, Supplier<ConfiguredFeature<?, ?>> ore) {
//		event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).add(() -> {
//			return Feature.ORE.withConfiguration(new OreFeatureConfig(AlwaysTrueRuleTest.INSTANCE, Blocks.GOLD_BLOCK.getDefaultState(), 8)).range(16).square();
//		});
//		
//		ForgeRegistries.FEATURES.getValue(new ResourceLocation("ore")).func_225566_b_(new OreFeatureConfig(AlwaysTrueRuleTest.INSTANCE, BlockHelper.getState("gold_block"), 8));
//	}
	
	public static void addOre(BiomeLoadingEvent event, Supplier<ConfiguredFeature<?, ?>> ore) {
		event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(ore);
	}
	
	public static ConfiguredFeature<?, ?> buildOreGen(BlockState blockState, int clusterSize, int minHeight, int maxHeight) {
		return Feature.ORE.withConfiguration(new OreFeatureConfig(AlwaysTrueRuleTest.INSTANCE, blockState, clusterSize)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight))).square();
		//return Feature.ORE.withConfiguration(new OreFeatureConfig(AlwaysTrueRuleTest.INSTANCE, blockState, clusterSize)).range(maxHeight).square();
	}
	
	public static void setSurfaceGenerator(BiomeLoadingEvent event) {
//		SurfaceBuilder<SurfaceBuilderConfig> builder = SurfaceBuilder.field_237189_ad_;
//		event.getGeneration().withSurfaceBuilder(builder.func_242929_a(new SurfaceBuilderConfig(BlockHelper.getState("minecraft:sand"), BlockHelper.getState("minecraft:sandstone"), BlockHelper.getState("minecraft:diamond_block"))));
	}
	
	public static boolean test(ResourceLocation location, BiomeDictionary.Type... types) {
		if (location != null) {
			RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, location);
			
			for (BiomeDictionary.Type type : types) {
				if (BiomeDictionary.hasType(key, type)) {
					return true;
				}
			}
		}
		return false;
	}
}
