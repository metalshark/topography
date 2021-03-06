package com.bloodnbonesgaming.topography.common.util.features;

import java.util.Iterator;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class FeatureHelper {

	public static void addFeature(BiomeLoadingEvent event, GenerationStage.Decoration stage, Supplier<ConfiguredFeature<?, ?>> feature) {
		event.getGeneration().getFeatures(stage).add(feature);
//		((Feature<OreFeatureConfig>)ForgeRegistries.FEATURES.getValue(new ResourceLocation("ore"))).withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlockHelper.getState("diamond_block"), 8));
//		configure(ForgeRegistries.FEATURES.getValue(new ResourceLocation("ore")), null);
		
//		new ConfiguredFeature(ForgeRegistries.FEATURES.getValue(new ResourceLocation("ore")), new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, BlockHelper.getState("diamond_block"), 8));
	}
	
	public static void addOre(BiomeLoadingEvent event, Supplier<ConfiguredFeature<?, ?>> ore) {
		FeatureHelper.addFeature(event, Decoration.UNDERGROUND_ORES, ore);
	}
	
	public static void clearFeatures(BiomeLoadingEvent event, GenerationStage.Decoration stage) {
		event.getGeneration().getFeatures(stage).clear();
	}
	
	public static ConfiguredFeature buildConfiguredFeature(String location, IFeatureConfig config) {
		return new ConfiguredFeature(ForgeRegistries.FEATURES.getValue(new ResourceLocation(location)), config);
	}
	
	public static ConfiguredFeature heightRange(ConfiguredFeature feature, int min, int max) {
		return feature.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(min, 0, max - min)));
	}
	
	public static ConfiguredFeature square(ConfiguredFeature feature) {
		return feature.withPlacement(Placement.SQUARE.configure(NoPlacementConfig.INSTANCE));
	}
	
	public static ConfiguredFeature chance(ConfiguredFeature feature, int chance) {
		return feature.withPlacement(Placement.CHANCE.configure(new ChanceConfig(chance)));
	}
	
	public static ConfiguredFeature count(ConfiguredFeature feature, int count) {		
		return feature.withPlacement(Placement.COUNT.configure(new FeatureSpreadConfig(FeatureSpread.func_242252_a(count))));
	}
	
	public static void removeStructure(BiomeLoadingEvent event, String id) {
		ResourceLocation toRemove = new ResourceLocation(id);
		Iterator<Supplier<StructureFeature<?, ?>>> iterator = event.getGeneration().getStructures().iterator();
		
		while (iterator.hasNext()) {
			Supplier<StructureFeature<?, ?>> supplier = iterator.next();
			ResourceLocation location = ForgeRegistries.STRUCTURE_FEATURES.getKey(supplier.get().field_236268_b_);
			
			if (toRemove.equals(location)) {
				iterator.remove();
			}
		}
	}
	
//	public static void configure(Feature<?> feature, IFeatureConfig config) {
//		feature.withConfiguration(config);
//	}
}
