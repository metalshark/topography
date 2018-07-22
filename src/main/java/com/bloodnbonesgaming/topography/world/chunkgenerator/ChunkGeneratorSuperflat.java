package com.bloodnbonesgaming.topography.world.chunkgenerator;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Nullable;

import com.bloodnbonesgaming.topography.config.definitions.SuperflatDefinition;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;

public class ChunkGeneratorSuperflat implements IChunkGenerator
{
    protected final Random rand;
    public final World worldObj;
    public Biome[] biomesForGeneration;
    
    private final SuperflatDefinition definition;

    public ChunkGeneratorSuperflat(World worldIn, long seed, final SuperflatDefinition definition)
    {
        this.worldObj = worldIn;
        this.rand = new Random(seed);
        this.definition = definition;
    }

    @Override
    public Chunk generateChunk(int chunkX, int chunkZ)
    {
        this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        
        for (int y = 0; y < 256; y++)
        {
            for (final Entry<MinMaxBounds, IBlockState> entry : this.definition.getLayers().entrySet())
            {
                if (entry.getKey().test(y))
                {
                    for (int x = 0; x < 16; x++)
                    {
                        for (int z = 0; z < 16; z++)
                        {
                            chunkprimer.setBlockState(x, y, z, entry.getValue());
                        }
                    }
                }
            }
        }
        
        
        
        
        this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomes(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, chunkX, chunkZ, chunkprimer, this.worldObj);

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, chunkX, chunkZ);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[i]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int x, int z)
    {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        this.rand.setSeed(this.worldObj.getSeed());
        long k = this.rand.nextLong() / 2L * 2L + 1L;
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)x * k + (long)z * l ^ this.worldObj.getSeed());

        ForgeEventFactory.onChunkPopulate(true, this, this.worldObj, this.rand, x, z, false);
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Pre(this.worldObj, this.rand, new BlockPos(i, 0, j)));
        MinecraftForge.EVENT_BUS.post(new DecorateBiomeEvent.Post(this.worldObj, this.rand, new BlockPos(i, 0, j)));
        ForgeEventFactory.onChunkPopulate(false, this, this.worldObj, this.rand, x, z, false);

        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        Biome biome = this.worldObj.getBiome(pos);

        return biome.getSpawnableList(creatureType);
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z)
    {
        
    }

    @Override
    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }
}