package com.bloodnbonesgaming.topography.world;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.NumberHelper;
import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
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
    
    
    public ChunkGeneratorSkyIslands(final World world)
    {
        this.worldSeed = world.getSeed();
        this.world = world;
        this.handler = ((BiomeProviderSkyIslands) this.world.getBiomeProvider()).getHandler();
        terrainNoise = new OpenSimplexNoiseGeneratorOctaves(world.getSeed());
    }

    @Override
    public Chunk generateChunk(int x, int z)
    {
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();

        this.mountainRand.setSeed((long)((int)Math.floor(x * 16D / this.handler.getRegionSize())) * 341873128712L + (long)((int)Math.floor(z * 16D / this.handler.getRegionSize())) * 132897987541L + this.worldSeed);
        
        this.generateIslands(x, z, chunkprimer);
        
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);

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
                        
                        if (Math.sqrt(xDistance + zDistance) <= maxFeatureRadius)
                        {
                            final SkyIslandType type = islandPos.getValue();
                            final Map<MinMaxBounds, IBlockState> boundsToState = type.getBoundsToStateMap();
                            
                            for (double y = 0; y < midHeight; y++)
                            {
                                final double skewNoise = this.largeNoiseArray[(int) ((x * 16 + z) * 256 + y)] * 2 - 1;
                                final double skewedNoise = this.terrainNoise.eval((realX + 16 * skewNoise) / noiseDistance, (realZ + 16 * skewNoise) / noiseDistance, 3, 0.5);
                                
                                final double bottomHeight = midHeight - skewedNoise * (maxNoiseDistance - noiseDistance * noise2);
                                final double topHeight = skewedNoise * ((maxNoiseDistance - noiseDistance * noise2) / 4.0);
                                
                                final int mid = (int) Math.floor(((topHeight + midHeight) - bottomHeight) / 2 + bottomHeight);
                                
                                final double distance = Math.floor(((topHeight + midHeight) - bottomHeight) / 2);
                                IBlockState state = type.getMainBlock();
                                //Bottom
                                {                                    
                                    for (final Entry<MinMaxBounds, IBlockState> bounds : boundsToState.entrySet())
                                    {
                                        if (bounds.getKey().test((float) (Math.floor(Math.abs(y - mid) + 1) / distance)))
                                        {
                                            state = bounds.getValue();
                                        }
                                    }
                                    
                                    if (bottomHeight < y)
                                    {
                                        primer.setBlockState((int) x, (int) y, (int) z, state);
                                    }
                                }
                                
                                //Top
                                {//                                                                        
                                    for (final Entry<MinMaxBounds, IBlockState> bounds : boundsToState.entrySet())
                                    {
                                        if (bounds.getKey().test((float) (Math.floor(Math.abs(y + midHeight - mid) + 1) / distance)))
                                        {
                                            state = bounds.getValue();
                                        }
                                    }
                                    
                                    if (topHeight > y)
                                    {
                                        primer.setBlockState((int) x, (int) (y + midHeight), (int) z, state);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            
        }
    }
    
    public void replaceBiomeBlocks(int chunkX, int chunkZ, ChunkPrimer primer, Biome[] biomesIn)
    {
        for (int x = 0; x < 16; x++)
        {
            x:
            for (int z = 0; z < 16; z++)
            {
                final BlockPos pos = new BlockPos(chunkX * 16 + x, 0, chunkZ * 16 + z);
                
                final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = this.handler.getIslandPositions(this.worldSeed, chunkX * 16, chunkZ * 16).entrySet().iterator();
                
                while (iterator.hasNext())
                {
                    final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set = iterator.next();
                    final SkyIslandData data = set.getKey();
                    final double minDistance = data.getRadius();
                    
                    for (final Entry<BlockPos, SkyIslandType> islandPos : set.getValue().entrySet())
                    {
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
        this.rand.setSeed(this.world.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * k + (long)z * l ^ this.world.getSeed());
        boolean flag = false;

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(true, this, this.world, this.rand, x, z, flag);

        final BlockPos pos = new BlockPos(i, 0, j);

        outer: for (final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set : this.handler.getIslandPositions(this.worldSeed, i, j).entrySet())
        {
            final SkyIslandData data = set.getKey();
            final double minDistance = data.getRadius();

            for (final Entry<BlockPos, SkyIslandType> islandPos : set.getValue().entrySet())
            {
                if (SkyIslandDataHandler.getDistance(pos, islandPos.getKey()) < minDistance + 16)
                {
                    final SkyIslandType type = islandPos.getValue();
                    Biome typeBiome = Biome.getBiome(type.getBiome());

                    if (type.isGenDecorations())
                    {
                        if (typeBiome != Biomes.VOID)
                        {
                            typeBiome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
                        }
                    }
                    if (type.genAnimals() && net.minecraftforge.event.terraingen.TerrainGen.populate(this, this.world, this.rand, x, z, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS))
                    {
                        WorldEntitySpawner.performWorldGenSpawning(this.world, typeBiome, i + 8, j + 8, 16, 16, this.rand);
                    }
                    break outer;
                }
            }
        }
        
        blockpos = blockpos.add(8, 0, 8);

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

        net.minecraftforge.event.ForgeEventFactory.onChunkPopulate(false, this, this.world, this.rand, x, z, flag);

        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }

    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return null;
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
        return false;
    }
}