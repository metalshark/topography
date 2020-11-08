package com.bloodnbonesgaming.topography.common.config;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.server.ServerWorld;

public class CarverHandler {

	public boolean disableDefaultCarvers = false;
	public final Map<GenerationStage.Carving, List<ConfiguredCarver<?>>> carversPre = new LinkedHashMap<GenerationStage.Carving, List<ConfiguredCarver<?>>>();//Generates before defaults
//	public final List<ConfiguredCarver<?>> carversPre = new ArrayList<ConfiguredCarver<?>>();//Generates before defaults
	public boolean generateDefaultCarvers = false;
	public final Map<GenerationStage.Carving, List<ConfiguredCarver<?>>> carversPost = new LinkedHashMap<GenerationStage.Carving, List<ConfiguredCarver<?>>>();//Generates after defaults
//	public final List<ConfiguredCarver<?>> carversPost = new ArrayList<ConfiguredCarver<?>>();//Generates after defaults
	
	public boolean disableDefaultLiquidCarvers = false;
//	public final List<ConfiguredCarver<?>> liquidCarversPre = new ArrayList<ConfiguredCarver<?>>();//Generates before defaults
	public boolean generateDefaultLiquidCarvers = false;
//	public final List<ConfiguredCarver<?>> liquidCarversPost = new ArrayList<ConfiguredCarver<?>>();//Generates after defaults
	
	public CarverHandler() {
		this.carversPre.put(GenerationStage.Carving.AIR, new ArrayList<ConfiguredCarver<?>>());
		this.carversPost.put(GenerationStage.Carving.AIR, new ArrayList<ConfiguredCarver<?>>());
		this.carversPre.put(GenerationStage.Carving.LIQUID, new ArrayList<ConfiguredCarver<?>>());
		this.carversPost.put(GenerationStage.Carving.LIQUID, new ArrayList<ConfiguredCarver<?>>());
	}
	
	public boolean generate(ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk, GenerationStage.Carving stage, DimensionDef def) {
		if (stage == GenerationStage.Carving.AIR ? !disableDefaultCarvers : !disableDefaultLiquidCarvers) {
			generator.func_230350_a_(world.getSeed(), world.getBiomeManager(), chunk, stage);// Generate defaults
		}
		
		long seed = world.getSeed();
		BiomeManager biomemanager = world.getBiomeManager().copyWithProvider(generator.getBiomeProvider());
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		ChunkPos chunkpos = chunk.getPos();
		int j = chunkpos.x;
		int k = chunkpos.z;
		BitSet bitset = ((ChunkPrimer) chunk).getOrAddCarvingMask(stage);

		// Generate pre defaults
		for (int l = j - 8; l <= j + 8; ++l) {
			for (int i1 = k - 8; i1 <= k + 8; ++i1) {
				List<ConfiguredCarver<?>> list = this.carversPre.get(stage);
				ListIterator<ConfiguredCarver<?>> listiterator = list.listIterator();

				while (listiterator.hasNext()) {
					int j1 = listiterator.nextIndex();
					ConfiguredCarver<?> configuredcarver = listiterator.next();
					sharedseedrandom.setLargeFeatureSeed(1024 + seed + (long) j1, l, i1);
					if (configuredcarver.shouldCarve(sharedseedrandom, l, i1)) {
						configuredcarver
								.carveRegion(chunk, biomemanager::getBiome, sharedseedrandom, generator
										.getSeaLevel(), l, i1, j, k, bitset);
					}
				}
			}
		}

		if (stage == GenerationStage.Carving.AIR ? generateDefaultCarvers : generateDefaultLiquidCarvers) {
			generator.func_230350_a_(world.getSeed(), world.getBiomeManager(), chunk, stage);// Generate defaults

			// Generate post defaults
			for (int l = j - 8; l <= j + 8; ++l) {
				for (int i1 = k - 8; i1 <= k + 8; ++i1) {
					List<ConfiguredCarver<?>> list = this.carversPost.get(stage);
					ListIterator<ConfiguredCarver<?>> listiterator = list.listIterator();

					while (listiterator.hasNext()) {
						int j1 = listiterator.nextIndex();
						ConfiguredCarver<?> configuredcarver = listiterator.next();
						sharedseedrandom.setLargeFeatureSeed(2048 + seed + (long) j1, l, i1);
						if (configuredcarver.shouldCarve(sharedseedrandom, l, i1)) {
							configuredcarver
									.carveRegion(chunk, biomemanager::getBiome, sharedseedrandom, generator
											.getSeaLevel(), l, i1, j, k, bitset);
						}
					}
				}
			}
		}
		return true;
	}
}
