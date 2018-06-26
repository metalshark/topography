package com.bloodnbonesgaming.randomgenskyislands.world;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.NumberHelper;
import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.randomgenskyislands.config.SkyIslandData;
import com.bloodnbonesgaming.randomgenskyislands.config.SkyIslandType;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

public class ChunkGeneratorSkyIslands implements IChunkGenerator
{
    final Random rand = new Random();
    final World world;
    final long worldSeed;
    Biome[] biomesForGeneration;
    protected final OpenSimplexNoiseGeneratorOctaves terrainNoise;
    final Random mountainRand = new Random();
    double[] smallNoiseArray = new double[825];
    double[] largeNoiseArray = new double[65536];
    

    protected NoiseGeneratorPerlin surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
    protected double[] depthBuffer = new double[256];
    
    
    final SkyIslandDataHandler handler;
    private final Random islandIndexRandom = new Random();
    
//    final List<BlockPos> largeIslands = new ArrayList<BlockPos>();
//    final List<BlockPos> mediumIslands = new ArrayList<BlockPos>();
//    final List<BlockPos> smallIslands = new ArrayList<BlockPos>();
//    final List<BlockPos> tinyIslands = new ArrayList<BlockPos>();
//    
//    final double largeRadius = 100;
//    final double mediumRadius = 50;
//    final double smallRadius = 25;
//    final double tinyRadius = 10;
//    
//    final int largeIslandCount = 1;
//    final int mediumIslandCount = 32;
//    final int smallIslandCount = 64;
//    final int tinyIslandCount = 128;
    
    
    public ChunkGeneratorSkyIslands(final World world)
    {
        this.worldSeed = world.getSeed();
        this.world = world;
        this.handler = ((BiomeProviderSkyIslands) this.world.getBiomeProvider()).getHandler();
        terrainNoise = new OpenSimplexNoiseGeneratorOctaves(world.getSeed());
        
////      final SkyIslandType end = new SkyIslandType();
////      end.setMainBlock(Blocks.END_STONE.getDefaultState());
////      end.setGenBiomeBlocks(false);
////      end.setGenDecorations(false);
//      
////      final SkyIslandType obsidian = new SkyIslandType();
////      obsidian.setMainBlock(Blocks.OBSIDIAN.getDefaultState());
////      obsidian.setGenBiomeBlocks(false);
////      obsidian.setGenDecorations(false);
//      
////      final SkyIslandType lava = new SkyIslandType();
////      lava.setMainBlock(Blocks.LAVA.getDefaultState());
////      lava.setGenBiomeBlocks(false);
////      lava.setGenDecorations(false);
//      
//      final SkyIslandType forest = new SkyIslandType();
//      forest.setBiome(Biome.getIdForBiome(Biomes.FOREST));
//      
//      final SkyIslandType cold_taiga = new SkyIslandType();
//      cold_taiga.setBiome(Biome.getIdForBiome(Biomes.COLD_TAIGA));
//      
//      final SkyIslandType desert = new SkyIslandType();
//      desert.setBiome(Biome.getIdForBiome(Biomes.DESERT));
//      
//      final SkyIslandType mesa = new SkyIslandType();
//      mesa.setBiome(Biome.getIdForBiome(Biomes.MESA));
//      
//      final SkyIslandType jungle = new SkyIslandType();
//      jungle.setBiome(Biome.getIdForBiome(Biomes.JUNGLE));
//      jungle.setGenDecorations(false);
//      
//      final SkyIslandType extreme_hills = new SkyIslandType();
//      extreme_hills.setBiome(Biome.getIdForBiome(Biomes.EXTREME_HILLS));
//      
//      
//      final SkyIslandData large = new SkyIslandData();
//      large.setCount(1);
//      large.setRadius(100);
//      large.setRandomIslands(false);
//      large.addType(forest);
//      large.addType(cold_taiga);
//      large.addType(desert);
//      large.addType(mesa);
//      large.addType(jungle);
//      large.addType(extreme_hills);
////      large.addType(end);
////      large.addType(obsidian);
////      large.addType(lava);
//      this.skyIslandData.add(large);
//      
//      final SkyIslandData medium = new SkyIslandData();
//      medium.setCount(32);
//      medium.setRadius(50);
//      medium.setRandomIslands(false);
//      medium.addType(forest);
//      medium.addType(cold_taiga);
//      medium.addType(desert);
//      medium.addType(mesa);
//      medium.addType(jungle);
//      medium.addType(extreme_hills);
////      medium.addType(end);
////      medium.addType(obsidian);
////      medium.addType(lava);
//      this.skyIslandData.add(medium);
//      
//      final SkyIslandData small = new SkyIslandData();
//      small.setCount(64);
//      small.setRadius(25);
//      small.setRandomIslands(false);
//      small.addType(forest);
//      small.addType(cold_taiga);
//      small.addType(desert);
//      small.addType(mesa);
//      small.addType(jungle);
//      small.addType(extreme_hills);
////      small.addType(end);
////      small.addType(obsidian);
////      small.addType(lava);
//      this.skyIslandData.add(small);
//      
//      final SkyIslandData tiny = new SkyIslandData();
//      tiny.setCount(128);
//      tiny.setRadius(10);
//      tiny.setRandomIslands(false);
//      tiny.addType(forest);
//      tiny.addType(cold_taiga);
//      tiny.addType(desert);
//      tiny.addType(mesa);
//      tiny.addType(jungle);
//      tiny.addType(extreme_hills);
////      tiny.addType(end);
////      tiny.addType(obsidian);
////      tiny.addType(lava);
//      this.skyIslandData.add(tiny);
    }

    @Override
    public Chunk generateChunk(int x, int z)
    {
//        this.islandPositions.clear();
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        

        this.mountainRand.setSeed((long)((int)Math.floor(x * 16D / this.handler.getRegionSize())) * 341873128712L + (long)((int)Math.floor(z * 16D / this.handler.getRegionSize())) * 132897987541L + this.worldSeed);
        
//        this.generateMountainFeatures(x, z, chunkprimer);
//        this.generateMediumIslands(x, z, chunkprimer);
//        this.generateSmallIslands(x, z, chunkprimer);
//        this.generateTinyIslands(x, z, chunkprimer);
        this.generateIslands(x, z, chunkprimer);
        
        
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);


//        largeIslands.clear();
//        mediumIslands.clear();
//        smallIslands.clear();
//        tinyIslands.clear();

        if (net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, chunkprimer, this.world))
        {
            this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration);
        }

        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }
    
    private void generateNoise(final double[] array, final int arraySizeX, final int arraySizeY, final int arraySizeZ, final int x, final int y, final int z, final int xCoordinateScale, final int yCoordinateScale, final int zCoordinateScale)
    {
        for (int xI = 0; xI < arraySizeX; xI++)
        {
            for (int zI = 0; zI < arraySizeZ; zI++)
            {
                for (int yI = 0; yI < arraySizeY; yI++)
                {
                    int index = (xI * arraySizeX + zI) * arraySizeY + yI;
                    double noise = this.terrainNoise.eval((x + xI * xCoordinateScale) / 128.0, (y + yI * yCoordinateScale) / 32.0, (z + zI * zCoordinateScale) / 128.0, 3, 0.5);
                    
                    array[index] = noise;
                }
            }
        }
    }
    
    public void generateIslands(final int chunkX, final int chunkZ, final ChunkPrimer primer)
    {
        this.generateNoise(this.smallNoiseArray, 5, 33, 5, chunkX * 16, 0, chunkZ * 16, 4, 8, 4);
        NumberHelper.interpolate(this.smallNoiseArray, this.largeNoiseArray, 5, 33, 5, 4, 8, 4);
        
        final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = this.handler.getIslandPositions(this.worldSeed, chunkX * 16, chunkZ * 16).entrySet().iterator();
        
//        for (final Entry<SkyIslandData, List<BlockPos>> entry : )
        while (iterator.hasNext())
        {
            final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> entry = iterator.next();
            final SkyIslandData data = entry.getKey();
            final int chunkBlockX = chunkX * 16;
            final int chunkBlockZ = chunkZ * 16;
            
            for (final Entry<BlockPos, SkyIslandType> islandPos : entry.getValue().entrySet())
            {
                final int featureCenterX = islandPos.getKey().getX();
                final int featureCenterZ = islandPos.getKey().getZ();
                final int midHeight = islandPos.getKey().getY();
                final int maxFeatureRadius = data.getRadius();
                
                for (double x = 0; x < 16; x++)
                {
                    final double realX = x + chunkBlockX;
                    final double xDistance = Math.pow(Math.abs(featureCenterX - realX), 2);
                    
                    for (double z = 0; z < 16; z++)
                    {
                        final double realZ = z + chunkBlockZ;
                        
                        final double zDistance = Math.pow(Math.abs(featureCenterZ - realZ), 2);
                        
                        final double maxNoiseDistance = (maxFeatureRadius - Math.sqrt(xDistance + zDistance)) * 1.5;
                        
                        final double noiseDistance = maxFeatureRadius * 0.32;
                        
                        final double noise2 = this.terrainNoise.eval((realX) / noiseDistance, (realZ) / noiseDistance, 3, 0.5);
                        
                        final SkyIslandType type = islandPos.getValue();
                        
                        if (Math.sqrt(xDistance + zDistance) <= maxFeatureRadius)
                        {
                            for (double y = 0; y < midHeight; y++)
                            {
                                //Top
                                {
                                    final double skewNoise = this.largeNoiseArray[(int) ((x * 16 + z) * 256 + y)] * 2 - 1;
//                                    
                                    final double skewedNoise = this.terrainNoise.eval((realX + 16 * skewNoise) / noiseDistance, (realZ + 16 * skewNoise) / noiseDistance, 3, 0.5);
//                                    final double skewedNoise = this.terrainNoise.eval((realX) / 32.0, (realZ) / 32.0, 3, 0.5);
                                    
                                    final double height = midHeight - skewedNoise * (maxNoiseDistance - noiseDistance * noise2);
                                    
                                    if (height < y)
                                    {
                                        primer.setBlockState((int) x, (int) y, (int) z, type.getMainBlock());
                                    }
                                }
                                
                                //Bottom
                                {
                                    final double skewNoise = this.largeNoiseArray[(int) ((x * 16 + z) * 256 + y)] * 2 - 1;
//                                    
                                    final double skewedNoise = this.terrainNoise.eval((realX + 16 * skewNoise) / noiseDistance, (realZ + 16 * skewNoise) / noiseDistance, 3, 0.5);
//                                    final double skewedNoise = this.terrainNoise.eval((realX) / 32.0, (realZ) / 32.0, 3, 0.5);
                                    
                                    final double height = skewedNoise * ((maxNoiseDistance - noiseDistance * noise2) / 4.0);
                                    
                                    if (height > y)
                                    {
                                        primer.setBlockState((int) x, (int) (y + midHeight), (int) z, type.getMainBlock());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            
        }
        
//        for (final SkyIslandData data : this.skyIslandData)
//        {
//            countLoop:
//            for (int i = 0; i < data.getCount(); i++)
//            {
//                final double maxFeatureRadius = data.getRadius();
//                final double midHeight = maxFeatureRadius + this.mountainRand.nextInt((int) (220 - (maxFeatureRadius * 2)));
//                
//                final int regionCenterX = ((int)Math.floor(chunkX * 16D / regionSize)) * regionSize + regionSize / 2;
//                final int regionCenterZ = ((int)Math.floor(chunkZ * 16D / regionSize)) * regionSize + regionSize / 2;
//                
//                final int randomSpace = (int) (regionSize - maxFeatureRadius * 2);
//                
//                final int featureCenterX = this.mountainRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
//                final int featureCenterZ = this.mountainRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;
//                
//                final BlockPos pos = new BlockPos(featureCenterX, 0, featureCenterZ);
//                
//                for (final Entry<Double, List<BlockPos>> set : this.islandPositions.entrySet())
//                {
//                    final double minDistance = set.getKey() + maxFeatureRadius + 25;
//                    
//                    for (final BlockPos islandPos : set.getValue())
//                    {
//                        if (pos.getDistance(islandPos.getX(), islandPos.getY(), islandPos.getZ()) < minDistance)
//                        {
//                            continue countLoop;
//                        }
//                    }
//                }
//                
//                if (!this.islandPositions.containsKey(maxFeatureRadius))
//                {
//                    this.islandPositions.put(maxFeatureRadius, new ArrayList<BlockPos>());
//                }
//                final List<BlockPos> positions = this.islandPositions.get(maxFeatureRadius);
//                positions.add(pos);
//                
//
//                
//                
//            }
//        }
    }
    
    public void replaceBiomeBlocks(int chunkX, int chunkZ, ChunkPrimer primer, Biome[] biomesIn)
    {
//        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double)(x * 16), (double)(z * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);
//
//        for (int i = 0; i < 16; ++i)
//        {
//            for (int j = 0; j < 16; ++j)
//            {
//                Biome biome = biomesIn[j + i * 16];
//                this.genBiomeTerrainBlocks(biome, this.world, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
//            }
//        }
        
        for (int x = 0; x < 16; x++)
        {
            x:
            for (int z = 0; z < 16; z++)
            {
                final BlockPos pos = new BlockPos(chunkX * 16 + x, 0, chunkZ * 16 + z);
                
                final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = this.handler.getIslandPositions(this.worldSeed, chunkX * 16, chunkZ * 16).entrySet().iterator();
                
                while (iterator.hasNext())
//                for (final Entry<SkyIslandData, List<BlockPos>> set : this.handler.getIslandPositions(this.worldSeed, chunkX * 16, chunkZ * 16).entrySet())
                {
                    final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set = iterator.next();
                    final SkyIslandData data = set.getKey();
                    int islandCount = -1;
                    final double minDistance = data.getRadius();
                    
                    for (final Entry<BlockPos, SkyIslandType> islandPos : set.getValue().entrySet())
                    {
                        islandCount++;
                        if (SkyIslandDataHandler.getDistance(pos, islandPos.getKey()) <= minDistance)
                        {
                            final SkyIslandType type = islandPos.getValue();
                            
                            if (type.isGenBiomeBlocks())
                            {
                                Biome biome = Biome.getBiome(type.getBiome());
                                
                                if (biome != Biomes.VOID)
                                {
                                    this.genBiomeTerrainBlocks(biome, this.world, this.rand, primer, chunkX * 16 + x, chunkZ * 16 + z, islandPos.getKey().getY(), 16);
                                }
                            }
                            continue x;
                        }
                    }
                }
            }
        }
    }
    
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    protected static final IBlockState RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
    protected static final IBlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    protected static final IBlockState ICE = Blocks.ICE.getDefaultState();
    protected static final IBlockState WATER = Blocks.WATER.getDefaultState();
    
    public void genBiomeTerrainBlocks(Biome biome, World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, int islandMid, double noiseVal)
    {
        int i = islandMid;
        IBlockState iblockstate = biome.topBlock;
        IBlockState iblockstate1 = biome.fillerBlock;
        int j = -1;
        int k = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = z & 15;
        int i1 = x & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1)
        {
            IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

            if (iblockstate2.getMaterial() == Material.AIR)
            {
                j = -1;
            }
            else if (iblockstate2 == Blocks.STONE.getDefaultState())
            {
                if (j == -1)
                {
                    if (k <= 0)
                    {
                        iblockstate = AIR;
                        iblockstate1 = Blocks.STONE.getDefaultState();
                    }
                    else if (j1 >= i - 4 && j1 <= i + 1)
                    {
                        iblockstate = biome.topBlock;
                        iblockstate1 = biome.fillerBlock;
                    }

                    if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
                    {
                        if (biome.getFloatTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F)
                        {
                            iblockstate = ICE;
                        }
                        else
                        {
                            iblockstate = WATER;
                        }
                    }

                    j = k;

                    if (j1 >= i - 1)
                    {
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
                    }
                    else if (j1 < i - 7 - k)
                    {
                        iblockstate = AIR;
                        iblockstate1 = Blocks.STONE.getDefaultState();
                        chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                    }
                    else
                    {
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                    }
                }
                else if (j > 0)
                {
                    --j;
                    chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                    if (j == 0 && iblockstate1.getBlock() == Blocks.SAND && k > 1)
                    {
                        j = rand.nextInt(4) + Math.max(0, j1 - 63);
                        iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                    }
                }
            }
        }
    }

    @Override
    public void populate(int x, int z)
    {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
        boolean flag = false;

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

//        if (this.populateLakes)
//        {
//            if (biome != Biomes.DESERT && biome != Biomes.DESERT_HILLS && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0)
//                if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE))
//                {
//                    int i1 = this.rand.nextInt(16) + 8;
//                    int j1 = this.rand.nextInt(256);
//                    int k1 = this.rand.nextInt(16) + 8;
//                    (new WorldGenLakes(Blocks.WATER)).generate(this.world, this.rand, blockpos.add(i1, j1, k1));
//                }
//        }
//
//        if (this.populateLavaLakes)
//        {
//            if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes)
//                if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA))
//                {
//                    int i2 = this.rand.nextInt(16) + 8;
//                    int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
//                    int k3 = this.rand.nextInt(16) + 8;
//
//                    if (l2 < this.world.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0)
//                    {
//                        (new WorldGenLakes(Blocks.LAVA)).generate(this.world, this.rand, blockpos.add(i2, l2, k3));
//                    }
//                }
//        }

//        if (this.decorateBiomes)
//        {
//            biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
//        }
        final BlockPos pos = new BlockPos(i, 0, j);
        
        outer:
            for (final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set : this.handler.getIslandPositions(this.worldSeed, i, j).entrySet())
        {
                final SkyIslandData data = set.getKey();
            int islandCount = -1;
            final double minDistance = data.getRadius();
            
            for (final Entry<BlockPos, SkyIslandType> islandPos : set.getValue().entrySet())
            {
                islandCount++;
                if (SkyIslandDataHandler.getDistance(pos, islandPos.getKey()) < minDistance + 16)
                {
                    
                            final SkyIslandType type = islandPos.getValue();
                            
                            
                            if (type.isGenDecorations())
                            {
                                Biome typeBiome = Biome.getBiome(type.getBiome());
                                
                                if (typeBiome != Biomes.VOID)
                                {
                                    typeBiome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
                                }
                            }
                            break outer;
                        
                }
            }
        }
        
        
//        if (this.populateAnimals)
//        {
//            if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS))
//            WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
//        }
        blockpos = blockpos.add(8, 0, 8);

//        if (this.populateIce)
//        {
            if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE))
            {
                for (int k2 = 0; k2 < 16; ++k2)
                {
                    for (int j3 = 0; j3 < 16; ++j3)
                    {
                        BlockPos blockpos1 = this.world.getPrecipitationHeight(blockpos.add(k2, 0, j3));
                        BlockPos blockpos2 = blockpos1.down();

                        if (this.world.canBlockFreezeWater(blockpos2))
                        {
                            this.world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
                        }

                        if (this.world.canSnowAt(blockpos1, true))
                        {
                            this.world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
                        }
                    }
                }
            }
//        }//Forge: End ICE

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        // TODO Auto-generated method stub
        return false;
    }
}