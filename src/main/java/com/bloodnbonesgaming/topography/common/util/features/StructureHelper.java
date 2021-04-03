package com.bloodnbonesgaming.topography.common.util.features;

import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureHelper {
	
	public static void AddStructure(BiomeLoadingEvent event, Supplier<StructureFeature<?, ?>> structure) {
		event.getGeneration().getStructures().add(structure);
	}
	
	public static void AddStructure(BiomeLoadingEvent event, String location, IFeatureConfig config) {
		event.getGeneration().getStructures().add(() -> new StructureFeature(ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation(location)), config));
	}
	
//	public static StructureFeature<?, ?> buildFortress() {
//		return FortressStructureTopo.INSTANCE.withConfiguration(NoFeatureConfig.field_236559_b_);
//	}
	
	public static void addFortress(BiomeLoadingEvent event) {
		Topography.getLog().info("FortressStructureTopo addFortress");
//		event.getGeneration().getStructures().add(()-> {
//			return FortressStructureTopo.INSTANCE.withConfiguration(NoFeatureConfig.field_236559_b_);
//		});
//		event.getGeneration().getStructures().add(()-> {
//			return WorldRegistry.FORTRESS.get().withConfiguration(NoFeatureConfig.field_236559_b_);
//		});
		event.getGeneration().getStructures().add(()-> {
			
			return new StructureFeature(ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation(ModInfo.MODID, "tfortress")), NoFeatureConfig.field_236559_b_);
//			return WorldRegistry.FORTRESS.get().withConfiguration(NoFeatureConfig.field_236559_b_);
		});
//		if (WorldRegistry.FORTRESS_FEATURE == null) {
//			WorldRegistry.FORTRESS_FEATURE = WorldRegistry.registerStructureFeature(WorldRegistry.FORTRESS.get().getRegistryName().getPath(), WorldRegistry.FORTRESS.get().withConfiguration(NoFeatureConfig.field_236559_b_));
//		}
//		event.getGeneration().getStructures().add(() -> { return WorldRegistry.FORTRESS_FEATURE; });
	}
}
