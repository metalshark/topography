package com.bloodnbonesgaming.topography.common.world.gen;

import java.util.List;
import java.util.Random;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.BiomeHelper;
import com.bloodnbonesgaming.topography.common.util.noise.NoiseUtil;
import com.bloodnbonesgaming.topography.common.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.common.world.gen.layer.RandomBiomeBaseLayer;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
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
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseGenerator;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.PerlinNoiseGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.spawner.WorldEntitySpawner;

public class ChunkGeneratorBlobs extends ChunkGenerator {

	public static final Codec<ChunkGeneratorBlobs> codec = RecordCodecBuilder.create((p_236091_0_) -> {
		return p_236091_0_.group(DimensionSettings.field_236098_b_.fieldOf("settings").forGetter((p_236090_0_) -> {
			return p_236090_0_.settings;
		}), RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((overworldProvider) -> {
			return overworldProvider.biomeRegistry;
		}), Codec.LONG.fieldOf("seed").stable().forGetter((p_236093_0_) -> {
			return p_236093_0_.seed;
		})).apply(p_236091_0_, p_236091_0_.stable(ChunkGeneratorBlobs::new));
	});

	private final long seed;
	private final Supplier<DimensionSettings> settings;
	private final OpenSimplexNoiseGeneratorOctaves simplex;
	private final double[] terrainNoiseArray = new double[65536];
	private final INoiseGenerator surfaceDepthNoise;
	private final BlockState baseTerrainBlock = Blocks.STONE.getDefaultState();
	private final BlockState baseFluidBlock = Blocks.WATER.getDefaultState();
	private final Registry<Biome> biomeRegistry;
	private int seaLevel = 63;

	private ChunkGeneratorBlobs(Supplier<DimensionSettings> settings, Registry<Biome> biomeRegistry, long seed) {
		this(Lists.newArrayList(biomeRegistry.getByValue(1)), biomeRegistry, settings, seed);
	}

	public ChunkGeneratorBlobs(List<Biome> biomes, Registry<Biome> biomeRegistry, Supplier<DimensionSettings> settings, long seed) {
		this(new BP(biomes, seed, 4, biomeRegistry), biomeRegistry, settings, seed);
	}

	private ChunkGeneratorBlobs(BiomeProvider biomeProvider, Registry<Biome> biomeRegistry, Supplier<DimensionSettings> settings, long seed) {
		super(biomeProvider, biomeProvider, settings.get().getStructures(), seed);
		Topography.getLog().info("CG " + seed);
		this.seed = seed;
		this.settings = settings;
		this.biomeRegistry = biomeRegistry;
		simplex = new OpenSimplexNoiseGeneratorOctaves(seed);
		SharedSeedRandom randomSeed = new SharedSeedRandom(seed);
		this.surfaceDepthNoise = (INoiseGenerator) (settings.get().getNoise().func_236178_i_()
				? new PerlinNoiseGenerator(randomSeed, IntStream.rangeClosed(-3, 0))
				: new OctavesNoiseGenerator(randomSeed, IntStream.rangeClosed(-3, 0)));
	}

	@Override
	protected Codec<? extends ChunkGenerator> func_230347_a_() {
		return codec;
	}

	@Override
	public ChunkGenerator func_230349_a_(long seed) {
		return new ChunkGeneratorBlobs(this.biomeProvider.getBiomeProvider(seed), this.biomeRegistry, this.settings,
				seed);
	}

	@Override
	public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {
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
		// base terrain
		// & structure shit? Structure pieces mess with the terrain gen somehow. Looks
		// like it can change whether liquid or solid is placed. Probably to make sure
		// lakes don't generate under houses?

		ChunkPrimer chunkprimer = (ChunkPrimer) chunk;
		Heightmap heightmap = chunkprimer.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
		Heightmap heightmap1 = chunkprimer.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

		Mutable mutable = new BlockPos.Mutable();
		double minNoise = 0.5;
		NoiseUtil.Simplex.Five_ThirtyThree.generateChunk(terrainNoiseArray, seed, chunk.getPos().getXStart(), chunk
				.getPos().getZStart(), 128, 32, 4, 0.5);

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 256; y++) {

					double val = terrainNoiseArray[(x * 16 + z) * 256 + y];

					double heightReduction;

					// Reduce noise result as y gets further from 128
					if (y >= 128) {
						heightReduction = (y - 128) / 128D;
					} else {
						heightReduction = (128 - y) / 128D;
					}
					heightReduction *= (1 - minNoise);

					if (val - heightReduction >= minNoise) {
						chunk.setBlockState(mutable.setPos(x, y, z), baseTerrainBlock, false);
						heightmap.update(x, y, z, baseTerrainBlock);
						heightmap1.update(x, y, z, baseTerrainBlock);
					}
				}
			}
		}
	}

	public int getGroundHeight() {
		return 128;
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
		double minNoise = 0.5;

		for (int y = 255; y >= 0; y--) {
			double val = simplex.eval(x / 128d, y / 32d, z / 128d, 4, 0.5);
			double heightReduction;

			// Reduce noise result as y gets further from 128
			if (y >= 128) {
				heightReduction = (y - 128) / 128D;
			} else {
				heightReduction = (128 - y) / 128D;
			}
			heightReduction *= (1 - minNoise);

			BlockState state;

			if (val - heightReduction >= minNoise) {
				state = this.baseTerrainBlock;
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
	public ChunkGeneratorBlobs setSeaLevel(int level) {
		this.seaLevel = level;
		return this;
	}

	public static class BP extends BiomeProvider {
		public static final Codec<BP> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(Codec.LONG.fieldOf("seed").stable().forGetter((provider) -> {
				return provider.seed;
			}), RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((provider) -> {
				return provider.biomeRegistry;
			})).apply(instance, (arg0, arg1) -> {
				try {
					return new BP(arg0, arg1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			});
		});
		private final long seed;
		private final int biomeSize;
		private final Registry<Biome> biomeRegistry;
		private final Layer layerGen;
		private final OpenSimplexNoiseGeneratorOctaves simplex;
		private final double[] terrainNoiseArray = new double[65536];

		public BP(long seed, Registry<Biome> biomeRegistry) throws Exception {
			this(BiomeHelper.forBiomes("plains"), seed, 4, biomeRegistry);
		}

		public BP(List<Biome> biomes, long seed, int biomeSize, Registry<Biome> biomeRegistry) {
			super(biomes);
			this.seed = seed;
			this.biomeSize = biomeSize;
			this.biomeRegistry = biomeRegistry;
			simplex = new OpenSimplexNoiseGeneratorOctaves(seed);
			this.layerGen = buildLayerGen(seed, biomes, biomeRegistry, biomeSize);
		}

		@Override
		public Biome getNoiseBiome(int x, int ignoredY, int z) {
			return this.layerGen.func_242936_a(this.biomeRegistry, x, z);
		}

		@Override
		protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
			return CODEC;
		}

		@Override
		public BiomeProvider getBiomeProvider(long seed) {
			return new BP(biomes, seed, biomeSize, biomeRegistry);
		}

		public static Layer buildLayerGen(long seed, List<Biome> biomes, Registry<Biome> biomeRegistry, int biomeSize) {
			IAreaFactory<LazyArea> iareafactory = buildLayers(biomes, biomeRegistry, biomeSize, seed, (p_227473_2_) -> {
				return new LazyAreaLayerContext(25, seed, p_227473_2_);
			});
			return new Layer(iareafactory);
		}

		private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildLayers(List<Biome> biomes, Registry<Biome> biomeRegistry, int biomeSize, long seed, LongFunction<C> seedHandler) {
			IAreaFactory<T> iareafactory = new RandomBiomeBaseLayer(biomes, biomeRegistry).apply(seedHandler.apply(1L));
			iareafactory = LayerUtil.repeat(2001L, ZoomLayer.NORMAL, iareafactory, biomeSize, seedHandler);
			iareafactory = new VoidLayer(seed).apply(seedHandler.apply(1L), iareafactory);
			return iareafactory;
		}

		static {

		}

		public static class VoidLayer implements IAreaTransformer1 {

			private final OpenSimplexNoiseGeneratorOctaves simplex;
			private final long seed;
			private final double[] terrainNoiseArray = new double[65536];

			public VoidLayer(long seed) {
				simplex = new OpenSimplexNoiseGeneratorOctaves(seed);
				this.seed = seed;
			}

			@Override
			public int getOffsetX(int x) {
				return 0;
			}

			@Override
			public int getOffsetZ(int z) {
				return 0;
			}

			@Override
			public int apply(IExtendedNoiseRandom<?> context, IArea area, int x, int z) {
				int parent = area.getValue(x, z);
				x = x << 2;
				z = z << 2;

				for (int y = 0; y < 256; y++) {// The noise would normally be only generated every 8 positions and
												// interpolated
					double val = simplex.eval(x / 128d, y / 32d, z / 128d, 4, 0.5);
					double minNoise = 0.47;
					double heightReduction;

					// Reduce noise result as y gets further from 128
					if (y >= 128) {
						heightReduction = (y - 128) / 128D;
					} else {
						heightReduction = (128 - y) / 128D;
					}
					heightReduction *= (1 - minNoise);

					if (val - heightReduction >= minNoise) {
						return parent;// Return parent if there's a block
					}
				}
				return 127;
			}

		}
	}

}
