package com.bloodnbonesgaming.topography.world.generator;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public interface IStructureHandler {
	
	public abstract void generateStructures(final World world, final int chunkX, final int chunkZ, final ChunkPrimer primer);
    
    public abstract void populateStructures(final World world, final Random rand, final ChunkPos chunkPos);
    
    public default List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, final World world, BlockPos pos, final List<Biome.SpawnListEntry> spawns) {
    	return spawns;
    }
    
    public abstract BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored);

    public abstract boolean isInsideStructure(World worldIn, String structureName, BlockPos pos);
    
    public abstract void recreateStructures(final World world, Chunk chunkIn, int x, int z);
    
    public default boolean generateStructures(World world, Random rand, Chunk chunkIn, int x, int z) {
    	return false;
    }
}
