package com.bloodnbonesgaming.topography.common.world.gen;

import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.GenerationStage.Carving;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;

public class ChunkGeneratorVoid extends ChunkGenerator {

	public static final Codec<ChunkGeneratorVoid> codec = RecordCodecBuilder.<ChunkGeneratorVoid>create((p_236091_0_) -> {
		return p_236091_0_.group(BiomeProvider.CODEC.fieldOf("biome_source").forGetter((p_236096_0_) -> {
			return p_236096_0_.biomeProvider;
		}), DimensionSettings.field_236098_b_.fieldOf("settings").forGetter((p_236090_0_) -> {
			return p_236090_0_.settings;
		}), Codec.LONG.fieldOf("seed").stable().forGetter((p_236093_0_) -> {
			return p_236093_0_.seed;
		})).apply(p_236091_0_, p_236091_0_.stable(ChunkGeneratorVoid::new));
	}).stable();

	private final long seed;
	private final Supplier<DimensionSettings> settings;

	public ChunkGeneratorVoid(BiomeProvider biomeProvider, Supplier<DimensionSettings> settings, long seed) {
		super(biomeProvider, biomeProvider, settings.get().getStructures(), seed);
		this.seed = seed;
		this.settings = settings;
	}

//	public ChunkGeneratorVoid(BiomeProvider biomeProvider, DimensionSettings settings, long seed) {
//		this(biomeProvider, biomeProvider, () -> {return settings;}, seed);
//	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		return codec;
	}

	@Override
	public ChunkGenerator func_230349_a_(long seed) {
		return new ChunkGeneratorVoid(this.biomeProvider.getBiomeProvider(seed), this.settings, seed);
	}

	@Override
	public void generateSurface(WorldGenRegion p_225551_1_, IChunk chunk) {
		// Biome block replacement
	}

	@Override
	public void func_230352_b_(IWorld world, StructureManager p_230352_2_, IChunk chunk) {
		// structure shit? Structure pieces mess with the terrain gen somehow. Looks
		// like it can change whether liquid or solid is placed. Probably to make sure
		// lakes don't generate under houses?
		// base terrain
	}

	@Override
	public int getHeight(int x, int z, Type heightmapType) {
		// no fucking idea
		return 0;
	}

	@Override
	public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
		// Some weird structure crap
		return new Blockreader(new BlockState[0]);
	}

//	@Override
//	public void func_235954_a_(StructureManager p_235954_1_, IChunk p_235954_2_, TemplateManager p_235954_3_, long p_235954_4_) {
//		// Generates structure starts - For some reason this locks on locate command. I
//		// assume there's another method I need to override
//	}

	@Override
	public void func_230350_a_(long p_230350_1_, BiomeManager p_230350_3_, IChunk p_230350_4_, Carving p_230350_5_) {
		// carvers/liquid carvers

//		BiomeManager biomemanager = p_230350_3_.copyWithProvider(this.biomeProvider);
//		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
//		int i = 8;
//		ChunkPos chunkpos = p_230350_4_.getPos();
//		int j = chunkpos.x;
//		int k = chunkpos.z;
//		Biome biome = this.biomeProvider.getNoiseBiome(chunkpos.x << 2, 0, chunkpos.z << 2);
//		BitSet bitset = ((ChunkPrimer) p_230350_4_).func_230345_b_(p_230350_5_);
//
//		for (int l = j - 8; l <= j + 8; ++l) {
//			for (int i1 = k - 8; i1 <= k + 8; ++i1) {
//				List<ConfiguredCarver<?>> list = biome.getCarvers(p_230350_5_);
//				ListIterator<ConfiguredCarver<?>> listiterator = list.listIterator();
//
//				while (listiterator.hasNext()) {
//					int j1 = listiterator.nextIndex();
//					ConfiguredCarver<?> configuredcarver = listiterator.next();
//					sharedseedrandom.setLargeFeatureSeed(p_230350_1_ + (long) j1, l, i1);
//					if (configuredcarver.shouldCarve(sharedseedrandom, l, i1)) {
//						configuredcarver.func_227207_a_(p_230350_4_, biomemanager::getBiome, sharedseedrandom, this
//								.func_230356_f_(), l, i1, j, k, bitset);
//					}
//				}
//			}
//		}
	}

	@Override
	public void func_230354_a_(WorldGenRegion p_230354_1_) {
		// initial animal world spawning
	}
}
