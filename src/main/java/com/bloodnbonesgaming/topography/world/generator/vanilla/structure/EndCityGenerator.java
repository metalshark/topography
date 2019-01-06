package com.bloodnbonesgaming.topography.world.generator.vanilla.structure;

import java.util.Random;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureEndCityPieces;
import net.minecraft.world.gen.structure.StructureStart;

public class EndCityGenerator extends MapGenEndCity
{	
	public final int frequency;
	public final int totalArea;
	public final int randomArea;
	public final boolean requiresIsland = false;//TODO Make this an option
	public final MinMaxBounds height;
    
    public EndCityGenerator(final int frequency, final int totalArea, final int randomArea, final MinMaxBounds height)
    {
    	super(null);
		this.frequency = frequency;
		this.totalArea = totalArea;
		this.randomArea = randomArea;
		this.height = height;
	}

    public String getStructureName()
    {
        return "EndCity";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX / totalArea;
        int j = chunkZ / totalArea;
        this.rand.setSeed((long)(i ^ j * totalArea) ^ this.world.getSeed());
        int randValue = totalArea / 2 > 0 ? totalArea / 2 : 1;
        
        if (chunkX % totalArea == this.rand.nextInt(randValue) + randomArea && chunkZ % totalArea == this.rand.nextInt(randValue) + randomArea)
        {
            this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
            return this.frequency > 1 ? this.rand.nextInt(this.frequency) == 0 : true;
        }
        return false;
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new EndCityGenerator.Start(this.world, this.rand, chunkX, chunkZ);
    }

    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, 20, 11, 10387313, true, 100, findUnexplored);
    }

    public static class Start extends MapGenEndCity.Start
        {
    	static {
    		MapGenStructureIO.registerStructure(EndCityGenerator.Start.class, "EndCity");
    	}
    	
            private boolean isSizeable;

            public Start()
            {
            }

            public Start(World worldIn, Random random, int chunkX, int chunkZ)
            {
                super(worldIn, null, random, chunkX, chunkZ);
            }

            @Override
            public void create(World worldIn, ChunkGeneratorEnd chunkProvider, Random rnd, int chunkX, int chunkZ)
            {
                Random random = new Random((long)(chunkX + chunkZ * 10387313));
                Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
                
                int i = 60;

                if (i < 60)
                {
                    this.isSizeable = false;
                }
                else
                {
                    BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
                    StructureEndCityPieces.startHouseTower(worldIn.getSaveHandler().getStructureTemplateManager(), blockpos, rotation, this.components, rnd);
                    this.updateBoundingBox();
                    this.isSizeable = true;
                }
            }

            /**
             * currently only defined for Villages, returns true if Village has more than 2 non-road components
             */
            public boolean isSizeableStructure()
            {
                return this.isSizeable;
            }
        }
}