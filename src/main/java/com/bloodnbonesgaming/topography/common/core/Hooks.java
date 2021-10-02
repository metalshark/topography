package com.bloodnbonesgaming.topography.common.core;

import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.util.FileHelper;
import com.bloodnbonesgaming.topography.common.util.Util;
import com.bloodnbonesgaming.topography.common.util.Functions.QuinFunction;
import com.bloodnbonesgaming.topography.common.world.gen.GenerationHandler.EnumGenerationPhase;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.SaveFormat.LevelSave;

public class Hooks {
	
	public static boolean onChunkStatusSurface(ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk) {
		
		return false;
	}

	public static boolean onChunkStatusNoise(ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk) {
		Preset preset = ConfigurationManager.getGlobalConfig().getPreset();

		if (preset != null) {
			//RegistryKey<DimensionType>
			DimensionDef def = preset.defs.get(world.getDimensionKey().getLocation());

			if (def != null) {

				return def.generate(EnumGenerationPhase.NOISE, world, generator, chunks, chunk);
			}
		}
		return false;
	}

	public static boolean onChunkStatusCarvers(ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk) {

		Preset preset = ConfigurationManager.getGlobalConfig().getPreset();

		if (preset != null) {
			DimensionDef def = preset.defs.get(world.getDimensionKey().getLocation());
			//Topography.getLog().info("Carver world location: " + world.getDimensionKey().getLocation() + " " + def != null);

			if (def != null) {

				return def.generate(EnumGenerationPhase.CARVERS, world, generator, chunks, chunk);
				//return def.carverHandler.generate(world, generator, chunks, chunk, GenerationStage.Carving.AIR, def);
			}
		}
		return false;
	}

	public static boolean onChunkStatusLiquidCarvers(ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk) {
		Preset preset = ConfigurationManager.getGlobalConfig().getPreset();

		if (preset != null) {
			DimensionDef def = preset.defs.get(world.getDimensionKey().getLocation());

			if (def != null) {

				return def.generate(EnumGenerationPhase.LIQUID_CARVERS, world, generator, chunks, chunk);
				//return def.carverHandler.generate(world, generator, chunks, chunk, GenerationStage.Carving.LIQUID, def);
			}
		}
		return false;
	}
	
	public static DynamicRegistries.Impl getRegistryForLoadWorld(Minecraft minecraft, String worldName) {
		try (LevelSave save = minecraft.getSaveLoader().getLevelSave(worldName)) {
			String line = FileHelper.readLineFromFile(save.getWorldDir().toString() + "/topography.txt");// TODO Check file existence first
			ConfigurationManager.getGlobalConfig().initPresets();
			ConfigurationManager.getGlobalConfig().setPreset(line);
		} catch (Exception e) {

		}
		DynamicRegistries.Impl registry = DynamicRegistries.func_239770_b_();
		Util.Registries.UpdateRegistries(registry);
		try {
			Preset preset = ConfigurationManager.getGlobalConfig().getPreset();

			if (preset != null) {
				preset.readDimensionDefs();
			}
		} catch (Exception e) {

		}
		return registry;
	}
	
	public static <T extends Entity> boolean testSpawnRule(EntityType<T> type, IServerWorld world, SpawnReason reason, BlockPos pos, Random rand) {
		Preset preset = ConfigurationManager.getGlobalConfig().getPreset();

		if (preset != null) {
			DimensionDef def = preset.defs.get(world.getWorld().getDimensionKey().getLocation());

			if (def != null) {
				QuinFunction<EntityType<?>, ServerWorld, SpawnReason, BlockPos, Random, Boolean> func = def.getSpawnRule(type.getRegistryName().toString());

				if (func != null) {
					return func.apply(type, (ServerWorld) world, reason, pos, rand);
				}
			}
		}
		return false;
	}
	
	public static <T extends Entity> boolean overrideSpawnRules(EntityType<T> type, IServerWorld world, SpawnReason reason, BlockPos pos, Random rand) {
		Preset preset = ConfigurationManager.getGlobalConfig().getPreset();

		if (preset != null) {
			DimensionDef def = preset.defs.get(world.getWorld().getDimensionKey().getLocation());

			if (def != null) {
				QuinFunction<EntityType<?>, ServerWorld, SpawnReason, BlockPos, Random, Boolean> func = def.getSpawnRule(type.getRegistryName().toString());

				if (func != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean test(EntityType<?> type, IServerWorld world, SpawnReason reason, BlockPos pos, Random rand) {
		if (overrideSpawnRules(type, world, reason, pos, rand)) {
			testSpawnRule(type, world, reason, pos, rand);
		}
		return false;
	}
}
