package com.bloodnbonesgaming.topography.world;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.EndCityGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.MansionGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.MineshaftGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.NetherBridgeGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.OceanMonumentGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.ScatteredFeatureGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.StrongholdGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.structure.VillageGenerator;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public class StructureHandler
{
    private NetherBridgeGenerator netherFortress;
    private EndCityGenerator endCity;
    private MansionGenerator mansion;
    private MineshaftGenerator mineshaft;
    private OceanMonumentGenerator monument;
    private ScatteredFeatureGenerator scattered;
    private StrongholdGenerator stronghold;
    private VillageGenerator village;
    
    
    public void generateStructures(final World world, final int chunkX, final int chunkZ, final ChunkPrimer primer)
    {
        if (this.netherFortress != null)
        {
            this.netherFortress.generate(world, chunkX, chunkZ, primer);
        }
        
        if (this.endCity != null)
        {
        	this.endCity.generate(world, chunkX, chunkZ, primer);
        }
        
        if (this.mineshaft != null)
        {
            this.mineshaft.generate(world, chunkX, chunkZ, primer);
        }

        if (this.village != null)
        {
            this.village.generate(world, chunkX, chunkZ, primer);
        }

        if (this.stronghold != null)
        {
            this.stronghold.generate(world, chunkX, chunkZ, primer);
        }

        if (this.scattered != null)
        {
            this.scattered.generate(world, chunkX, chunkZ, primer);
        }

        if (this.monument != null)
        {
            this.monument.generate(world, chunkX, chunkZ, primer);
        }

        if (this.mansion != null)
        {
            this.mansion.generate(world, chunkX, chunkZ, primer);
        }
    }
    
    public void populateStructures(final World world, final Random rand, final ChunkPos chunkPos)
    {
        if (this.netherFortress != null)
        {
            this.netherFortress.generateStructure(world, rand, chunkPos);
        }
        
        if (this.endCity != null)
        {
        	this.endCity.generateStructure(world, rand, chunkPos);
        }
        
        if (this.mineshaft != null)
        {
            this.mineshaft.generateStructure(world, rand, chunkPos);
        }

        if (this.village != null)
        {
            this.village.generateStructure(world, rand, chunkPos);
        }

        if (this.stronghold != null)
        {
            this.stronghold.generateStructure(world, rand, chunkPos);
        }

        if (this.scattered != null)
        {
            this.scattered.generateStructure(world, rand, chunkPos);
        }

        if (this.monument != null)
        {
            this.monument.generateStructure(world, rand, chunkPos);
        }

        if (this.mansion != null)
        {
            this.mansion.generateStructure(world, rand, chunkPos);
        }
    }
    
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, final World world, BlockPos pos, final List<Biome.SpawnListEntry> spawns)
    {
    	if (creatureType == EnumCreatureType.MONSTER)
        {
        	if (this.netherFortress != null)
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
            
            if (this.scattered != null && this.scattered.isSwampHut(pos))
            {
                return this.scattered.getMonsters();
            }

            if (this.monument != null && this.monument.isPositionInStructure(world, pos))
            {
                return this.monument.getMonsters();
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
	    	case "Stronghold": {
	    		return this.stronghold != null ? this.stronghold.getNearestStructurePos(worldIn, position, findUnexplored) : null;
	    	}
	    	case "Mansion": {
	    		return this.mansion != null ? this.mansion.getNearestStructurePos(worldIn, position, findUnexplored) : null;
	    	}
	    	case "Monument": {
	    		return this.monument != null ? this.monument.getNearestStructurePos(worldIn, position, findUnexplored) : null;
	    	}
	    	case "Village": {
	    		return this.village != null ? this.village.getNearestStructurePos(worldIn, position, findUnexplored) : null;
	    	}
	    	case "Mineshaft": {
	    		return this.mineshaft != null ? this.mineshaft.getNearestStructurePos(worldIn, position, findUnexplored) : null;
	    	}
	    	case "Temple": {
	    		return this.scattered != null ? this.scattered.getNearestStructurePos(worldIn, position, findUnexplored) : null;
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
	    	case "Stronghold": {
	    		return this.stronghold != null ? this.stronghold.isInsideStructure(pos) : false;
	    	}
	    	case "Mansion": {
	    		return this.mansion != null ? this.mansion.isInsideStructure(pos) : false;
	    	}
	    	case "Monument": {
	    		return this.monument != null ? this.monument.isInsideStructure(pos) : false;
	    	}
	    	case "Village": {
	    		return this.village != null ? this.village.isInsideStructure(pos) : false;
	    	}
	    	case "Mineshaft": {
	    		return this.mineshaft != null ? this.mineshaft.isInsideStructure(pos) : false;
	    	}
	    	case "Temple": {
	    		return this.scattered != null ? this.scattered.isInsideStructure(pos) : false;
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
        
        if (this.mineshaft != null)
        {
            this.mineshaft.generate(world, x, z, (ChunkPrimer)null);
        }

        if (this.village != null)
        {
            this.village.generate(world, x, z, (ChunkPrimer)null);
        }

        if (this.stronghold != null)
        {
            this.stronghold.generate(world, x, z, (ChunkPrimer)null);
        }

        if (this.scattered != null)
        {
            this.scattered.generate(world, x, z, (ChunkPrimer)null);
        }

        if (this.monument != null)
        {
            this.monument.generate(world, x, z, (ChunkPrimer)null);
        }

        if (this.mansion != null)
        {
            this.mansion.generate(world, x, z, (ChunkPrimer)null);
        }
    }
    
    public boolean generateStructures(World world, Random rand, Chunk chunkIn, int x, int z)
    {
        boolean flag = false;

        if (this.monument != null && chunkIn.getInhabitedTime() < 3600L)
        {
            flag |= this.monument.generateStructure(world, rand, new ChunkPos(x, z));
        }

        return flag;
    }
    
    
    public void generateNetherFortress(final int frequency, final int totalArea, final int randomArea)
    {
        this.netherFortress = new NetherBridgeGenerator(frequency, totalArea, randomArea);
    }
    
    public void generateEndCity(final int frequency, final int totalArea, final int randomArea)
    {
        this.endCity = new EndCityGenerator(frequency, totalArea, randomArea, new MinMaxBounds(0F, 0F));
    }
    
    public void generateMineshaft()
    {
    	this.mineshaft = new MineshaftGenerator();
    }
    
    public void generateVillage()
    {
    	this.village = new VillageGenerator();
    }
    
    public void generateStronghold()
    {
    	this.stronghold = new StrongholdGenerator();
    }
    
    public void generateTemple()
    {
    	this.scattered = new ScatteredFeatureGenerator();
    }
    
    public void generateMonument()
    {
    	this.monument = new OceanMonumentGenerator();
    }
    
    public void generateMansion(final IGenerator generator)
    {
    	this.mansion = new MansionGenerator(generator);
    }
}
