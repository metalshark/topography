package com.bloodnbonesgaming.topography.common.util.features;

import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.AlwaysTrueRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class OreHelper {
	
	public static void addOre(BiomeLoadingEvent event, Supplier<ConfiguredFeature<?, ?>> ore) {
		//event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(ore);
		FeatureHelper.addFeature(event, Decoration.UNDERGROUND_ORES, ore);
	}
	
	public static void clearOre(BiomeLoadingEvent event) {
		FeatureHelper.clearFeatures(event, Decoration.UNDERGROUND_ORES);
	}
	
	public static ConfiguredFeature<?, ?> buildOreForOverworldStone(BlockState blockState, int clusterSize, int minHeight, int maxHeight, boolean square) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildOreForNetherrack(BlockState blockState, int clusterSize, int minHeight, int maxHeight, boolean square) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildOreForNetherStone(BlockState blockState, int clusterSize, int minHeight, int maxHeight, boolean square) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildOreForOverworldStone(BlockState blockState, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		feature = FeatureHelper.count(feature, clusterCount);
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildOreForNetherrack(BlockState blockState, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		feature = FeatureHelper.count(feature, clusterCount);
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildOreForNetherStone(BlockState blockState, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		feature = FeatureHelper.count(feature, clusterCount);
		return feature;
	}
}
