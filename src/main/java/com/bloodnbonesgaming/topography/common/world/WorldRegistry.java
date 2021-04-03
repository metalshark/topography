package com.bloodnbonesgaming.topography.common.world;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.common.world.gen.feature.structure.FortressStructureTopo;

import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldRegistry {
	
	public static final List<Structure<?>> structureCache = new ArrayList<Structure<?>>();
	private static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, ModInfo.MODID);
	
//	public static final RegistryObject<FortressStructureTopo> FORTRESS = STRUCTURES.register("tfortress", () -> {
//		return new FortressStructureTopo(NoFeatureConfig.field_236558_a_);
//	});
	public static final RegistryObject<FortressStructureTopo> FORTRESS = registerStructure("tfortress", new FortressStructureTopo(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.UNDERGROUND_STRUCTURES);
	public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> FORTRESS_FEATURE = null;
	
	public static void init() {
		STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	private static <T extends Structure<?>> RegistryObject<T> registerStructure(String name, T structure, GenerationStage.Decoration stage) {
		Structure.NAME_STRUCTURE_BIMAP.put(ModInfo.MODID + ":" + name, structure);
		Structure.STRUCTURE_DECORATION_STAGE_MAP.put(structure, stage);
		
		structureCache.add(structure);
		return STRUCTURES.register(name, () -> structure);
	}
}
