package com.bloodnbonesgaming.topography.common.world.gen;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import com.bloodnbonesgaming.topography.Topography;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Blockreader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.INoiseGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.spawner.WorldEntitySpawner;

public class ChunkGeneratorLayersFlat extends ChunkGenerator {

	public static final Codec<ChunkGeneratorLayersFlat> codec = RecordCodecBuilder.create((p_236091_0_) -> {
		return p_236091_0_.group(BiomeProvider.CODEC.fieldOf("biome_source").forGetter((p_236096_0_) -> {
	        return p_236096_0_.biomeProvider;
	    }), DimensionSettings.field_236098_b_.fieldOf("settings").forGetter((p_236090_0_) -> {
			return p_236090_0_.settings;
		}), Codec.LONG.fieldOf("seed").stable().forGetter((p_236093_0_) -> {
			return p_236093_0_.seed;
		})).apply(p_236091_0_, p_236091_0_.stable(ChunkGeneratorLayersFlat::new));
	});

	private final long seed;
	private final Supplier<DimensionSettings> settings;
	private final INoiseGenerator surfaceDepthNoise;
	private final BlockState baseTerrainBlock = Blocks.STONE.getDefaultState();
	private final BlockState baseFluidBlock = Blocks.WATER.getDefaultState();
	private int seaLevel = 63;
	private final BlockState[] layers = new BlockState[256];
	private boolean buildSurface = false;

	public ChunkGeneratorLayersFlat(BiomeProvider biomeProvider, Supplier<DimensionSettings> settings, long seed) {
		super(biomeProvider, biomeProvider, settings.get().getStructures(), seed);
		Topography.getLog().info("CG " + seed);
		this.seed = seed;
		this.settings = settings;
		SharedSeedRandom randomSeed = new SharedSeedRandom(seed);
		this.surfaceDepthNoise = (INoiseGenerator) (settings.get().getNoise().func_236178_i_()
				? new PerlinNoiseGenerator(randomSeed, IntStream.rangeClosed(-3, 0))
				: new OctavesNoiseGenerator(randomSeed, IntStream.rangeClosed(-3, 0)));
		
	}
	
	//Script method
	public ChunkGeneratorLayersFlat addLayers(int min, int max, BlockState state) {
		for (int i = min; i <= max; i++) {
			layers[i] = state;
		}
		return this;
	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		return codec;
	}

	@Override
	public ChunkGenerator func_230349_a_(long seed) {
		return new ChunkGeneratorLayersFlat(this.biomeProvider.getBiomeProvider(seed), this.settings,
				seed);
	}

	@Override
	public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {
		if (this.buildSurface) {
			// Biome block replacement
			ChunkPos chunkpos = p_225551_2_.getPos();
			int i = chunkpos.x;
			int j = chunkpos.z;
			SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
			sharedseedrandom.setBaseChunkSeed(i, j);
			ChunkPos chunkpos1 = p_225551_2_.getPos();
			int k = chunkpos1.getXStart();
			int l = chunkpos1.getZStart();
			double d0 = 0.0625D;
			BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

			for (int i1 = 0; i1 < 16; ++i1) {
				for (int j1 = 0; j1 < 16; ++j1) {
					int k1 = k + i1;
					int l1 = l + j1;
					int i2 = p_225551_2_.getTopBlockY(Heightmap.Type.WORLD_SURFACE_WG, i1, j1) + 1;
					double d1 = this.surfaceDepthNoise
							.noiseAt((double) k1 * 0.0625D, (double) l1 * 0.0625D, 0.0625D, (double) i1 * 0.0625D) * 15.0D;
					// p_225551_1_.getBiome(blockpos$mutable.setPos(k + i1, i2, l +
					// j1)).buildSurface(sharedseedrandom, p_225551_2_, k1, l1, i2, d1,
					// this.baseTerrainBlock, this.baseFluidBlock, this.getSeaLevel(),
					// p_225551_1_.getSeed());
					Biome biome = p_225551_1_.getBiome(blockpos$mutable.setPos(k + i1, i2, l + j1));
					ISurfaceBuilderConfig config = biome.getGenerationSettings().getSurfaceBuilderConfig();
					buildSurface(sharedseedrandom, p_225551_2_, biome, k1, l1, i2, d1, this.baseTerrainBlock, this.baseFluidBlock, config
							.getTop(), config.getUnder(), this.getSeaLevel());
				}
			}
		}
	}

	protected void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, BlockState top, BlockState under, int sealevel) {
		BlockState blockstate = top;
		BlockState blockstate1 = under;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = -1;
		int j = (int) (noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
		int k = x & 15;
		int l = z & 15;

		for (int i1 = startHeight; i1 >= 0; --i1) {
			blockpos$mutable.setPos(k, i1, l);
			BlockState blockstate2 = chunkIn.getBlockState(blockpos$mutable);
			if (blockstate2.isAir()) {
				i = -1;
			} else if (blockstate2.isIn(defaultBlock.getBlock())) {
				if (i == -1) {
					if (j <= 0) {
						blockstate = Blocks.AIR.getDefaultState();
						blockstate1 = defaultBlock;
					} else if (i1 >= sealevel - 4 && i1 <= sealevel + 1) {
						blockstate = top;
						blockstate1 = under;
					}

					if (i1 < sealevel && (blockstate == null || blockstate.isAir())) {
						if (biomeIn.getTemperature(blockpos$mutable.setPos(x, i1, z)) < 0.15F) {
							blockstate = Blocks.ICE.getDefaultState();
						} else {
							blockstate = defaultFluid;
						}

						blockpos$mutable.setPos(k, i1, l);
					}

					i = j;
					if (i1 >= sealevel - 1) {
						chunkIn.setBlockState(blockpos$mutable, blockstate, false);
					} else {
						chunkIn.setBlockState(blockpos$mutable, blockstate1, false);
					}
				} else if (i > 0) {
					--i;
					chunkIn.setBlockState(blockpos$mutable, blockstate1, false);
					if (i == 0 && blockstate1.isIn(Blocks.SAND) && j > 1) {
						i = random.nextInt(4);
						blockstate1 = blockstate1.isIn(Blocks.RED_SAND) ? Blocks.RED_SANDSTONE.getDefaultState()
								: Blocks.SANDSTONE.getDefaultState();
					}
				}
			}
		}
	}

	@Override
	public void func_230352_b_(IWorld world, StructureManager structures, IChunk chunk) {
		ChunkPrimer chunkprimer = (ChunkPrimer) chunk;
		Heightmap heightmap = chunkprimer.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap heightmap1 = chunkprimer.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

		Mutable mutable = new BlockPos.Mutable();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {
					if (this.layers[y] != null) {
						chunk.setBlockState(mutable.setPos(x, y, z), this.layers[y], false);
						heightmap.update(x, y, z, this.layers[y]);
						heightmap1.update(x, y, z, this.layers[y]);
					}
				}
			}
		}
	}

	public int getGroundHeight() {
		return 64;
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmapType) {
		return this.getHeightOrFillBlockReader(x, z, (BlockState[]) null, heightmapType.getHeightLimitPredicate());
	}

	public IBlockReader func_230348_a_(int x, int z) {
		BlockState[] ablockstate = new BlockState[256];
		this.getHeightOrFillBlockReader(x, z, ablockstate, (Predicate<BlockState>) null);
		return new Blockreader(ablockstate);
	}

	public int getHeightOrFillBlockReader(int x, int z, @Nullable BlockState[] blocks, @Nullable Predicate<BlockState> heightLimitPredicate) {
		for (int y = 255; y >= 0; y--) {
			BlockState state;
			
			if (this.layers[y] != null) {
				state = layers[y];
			} else {
				state = Blocks.AIR.getDefaultState();
			}
			if (blocks != null) {
				blocks[y] = state;
			}
			if (heightLimitPredicate != null && heightLimitPredicate.test(state)) {
				return y + 1;
			}
		}

		return 0;
	}

	// Perform initial world gen animal spawning
	@Override
	public void func_230354_a_(WorldGenRegion region) {
		// This is probably a boolean for world gen spawning in the settings
		// if (!this.settings.get().func_236120_h_()) {
		int i = region.getMainChunkX();
		int j = region.getMainChunkZ();
		Biome biome = region.getBiome((new ChunkPos(i, j)).asBlockPos());
		SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
		sharedseedrandom.setDecorationSeed(region.getSeed(), i << 4, j << 4);
		WorldEntitySpawner.performWorldGenSpawning(region, biome, i, j, sharedseedrandom);
		// }
	}

	@Override
	public int getSeaLevel() {
		return this.seaLevel;
	}

	// Script methods
	public ChunkGeneratorLayersFlat setSeaLevel(int level) {
		this.seaLevel = level;
		return this;
	}
}
