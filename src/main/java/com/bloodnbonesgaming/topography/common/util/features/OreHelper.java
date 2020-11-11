package com.bloodnbonesgaming.topography.common.util.features;

import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.common.world.gen.feature.VerticalOre;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.AlwaysTrueRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class OreHelper {
	
	public static RuleTest BASE_STONE_OVERWORLD = OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD;
	public static RuleTest NETHERRACK = OreFeatureConfig.FillerBlockType.NETHERRACK;
	public static RuleTest BASE_STONE_NETHER = OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER;
	public static RuleTest ALWAYS_TRUE = AlwaysTrueRuleTest.INSTANCE;
	
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
	
	public static ConfiguredFeature<?, ?> buildOreForOverworldStone(BlockState blockState, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square, int chance) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		feature = FeatureHelper.count(feature, clusterCount);
		feature = FeatureHelper.chance(feature, chance);
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildOreForNetherrack(BlockState blockState, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square, int chance) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		feature = FeatureHelper.count(feature, clusterCount);
		feature = FeatureHelper.chance(feature, chance);
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildOreForNetherStone(BlockState blockState, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square, int chance) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		feature = FeatureHelper.count(feature, clusterCount);
		feature = FeatureHelper.chance(feature, chance);
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildOre(BlockState blockState, RuleTest test, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square, int chance) {
		ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(test, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		feature = FeatureHelper.count(feature, clusterCount);
		feature = FeatureHelper.chance(feature, chance);
		return feature;
	}
	
	public static ConfiguredFeature<?, ?> buildVerticalOre(BlockState blockState, RuleTest test, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square, int chance) {
		ConfiguredFeature<?, ?> feature = VerticalOre.INSTANCE.withConfiguration(new OreFeatureConfig(test, blockState, clusterSize));
		feature = FeatureHelper.heightRange(feature, minHeight, maxHeight);
		if (square) {
			feature = FeatureHelper.square(feature);
		}
		if (clusterCount > 1) {
			feature = FeatureHelper.count(feature, clusterCount);
		}
		if (chance > 0) {
			feature = FeatureHelper.chance(feature, chance);
		}
		return feature;
	}
}
