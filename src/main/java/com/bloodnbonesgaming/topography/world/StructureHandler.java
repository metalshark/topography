package com.bloodnbonesgaming.topography.world;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.NetherBridgeGenerator;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class StructureHandler
{
    private NetherBridgeGenerator netherFortress;
    
    private boolean genEndCity = false;
    private MapGenEndCity endCity;
    
    
    public void generateStructures(final World world, final int chunkX, final int chunkZ, final ChunkPrimer primer)
    {
        if (this.netherFortress != null)
        {
            this.netherFortress.generate(world, chunkX, chunkZ, primer);
        }
        if (this.genEndCity)
        {
        	this.makeEndCityGenerator(world);
        	this.endCity.generate(world, chunkX, chunkZ, primer);
        }
    }
    
    public void populateStructures(final World world, final Random rand, final ChunkPos chunkPos)
    {
        if (this.netherFortress != null)
        {
            this.netherFortress.generateStructure(world, rand, chunkPos);
        }
        if (this.genEndCity)
        {
        	this.makeEndCityGenerator(world);
        	this.endCity.generateStructure(world, rand, chunkPos);
        }
    }
    
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, final World world, BlockPos pos, final List<Biome.SpawnListEntry> spawns)
    {
        if (this.netherFortress != null)
        {
            if (creatureType == EnumCreatureType.MONSTER)
            {
                if (this.netherFortress.isInsideStructure(pos))
                {
                    return this.netherFortress.getSpawnList();
                }

                if (this.netherFortress.isPositionInStructure(world, pos) && world.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICK)
                {
                    return this.netherFortress.getSpawnList();
                }
            }
        }
        return spawns;
    }
    
    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
    {
    	switch (structureName)
    	{
	    	case "Fortress": {
	    		return this.netherFortress != null ? this.netherFortress.getNearestStructurePos(worldIn, position, findUnexplored) : null;
	    	}
	    	case "EndCity": {
	    		return this.endCity != null ? this.endCity.getNearestStructurePos(worldIn, position, findUnexplored) : null;
	    	}
	    	default: {
	    		return null;
	    	}
    	}
    }

    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
    {
    	switch (structureName)
    	{
		    case "Fortress": {
				return this.netherFortress != null ? this.netherFortress.isInsideStructure(pos) : false;
			}
			case "EndCity": {
				return this.endCity != null ? this.endCity.isInsideStructure(pos) : false;
			}
			default: {
				return false;
			}
    	}
    }

    /**
     * Recreates data about structures intersecting given chunk (used for example by getPossibleCreatures), without
     * placing any blocks. When called for the first time before any chunk is generated - also initializes the internal
     * state needed by getPossibleCreatures.
     */
    public void recreateStructures(final World world, Chunk chunkIn, int x, int z)
    {
        if (this.netherFortress != null)
        {
            this.netherFortress.generate(world, x, z, (ChunkPrimer)null);
        }
    }
    
    
    public void generateNetherFortress(final int frequency)
    {
        this.netherFortress = new NetherBridgeGenerator(frequency);
    }
    
    public void generateEndCity()
    {
    	this.genEndCity = true;
    }
    
    
    
    //Builds the end city generator, since it requires a World object
    private void makeEndCityGenerator(final World world)
    {
    	if (this.endCity == null)
    	{
    		this.endCity = new MapGenEndCity(new ChunkGeneratorEnd(world, world.getWorldInfo().isMapFeaturesEnabled(), world.getSeed(), new BlockPos(100, 50, 0)));
    	}
    }
}
