package com.bloodnbonesgaming.topography.world.generator.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGeneratorV2;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

public class SkyIslandStrongholdSimpleGenerator extends SkyIslandStrongholdGenerator {

	public SkyIslandStrongholdSimpleGenerator(SkyIslandGeneratorV2 generator) {
		super(generator);
	}
	
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
		SkyIslandStrongholdSimpleGenerator.Start start;

        for (start = new SkyIslandStrongholdSimpleGenerator.Start(this.world, this.rand, chunkX, chunkZ); start.getComponents().isEmpty(); start = new SkyIslandStrongholdSimpleGenerator.Start(this.world, this.rand, chunkX, chunkZ))
        {
            ;
        }
		
		final ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);

		final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = this.skyIslandGenerator.getIslandPositions(this.world.getSeed(), chunkX * 16, chunkZ * 16).entrySet().iterator();
        
        while (iterator.hasNext())
        {
            final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> islands = iterator.next();
            
            final Iterator<Entry<BlockPos, SkyIslandType>> positions = islands.getValue().entrySet().iterator();
            
            while (positions.hasNext())
            {
                final Entry<BlockPos, SkyIslandType> island = positions.next();
                
                final BlockPos pos = island.getKey();
                
                if (chunkPos.equals(new ChunkPos(pos)))
                {
                	if (island.getValue().shouldGenerateStrongholds())
                	{
                    	int j = pos.getY() - start.getBoundingBox().maxY + start.getBoundingBox().getYSize() / 2 - (int) Math.floor(islands.getKey().getBottomHeight() / 2);
                		start.getBoundingBox().offset(0, j, 0);
                    	
                    	for (StructureComponent structurecomponent : start.getComponents())
                        {
                            structurecomponent.offset(0, j, 0);
                        }
                	}
                	return start;
                }
            }
        }
		return start;
    }

	public static class Start extends StructureStart {
    	static {
    		MapGenStructureIO.registerStructure(SkyIslandStrongholdSimpleGenerator.Start.class, "TopographySimpleStronghold");
    		MapGenStructureIO.registerStructureComponent(SkyIslandStrongholdSimpleGenerator.Stairs2.class, "TopographySimpleStrongholdStairs2");
    	}
    	public Start()
        {
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ)
        {
        	super(chunkX, chunkZ);
            StructureStrongholdPieces.prepareStructurePieces();
            StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new SkyIslandStrongholdSimpleGenerator.Stairs2(0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            this.components.add(structurestrongholdpieces$stairs2);
            structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
            List<StructureComponent> list = structurestrongholdpieces$stairs2.pendingChildren;

            while (!list.isEmpty())
            {
                int i = random.nextInt(list.size());
                StructureComponent structurecomponent = list.remove(i);
            	structurecomponent.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
            }

            this.updateBoundingBox();
            this.markAvailableHeight(worldIn, random, 10);
        }
    }
    
	public static class Stairs2 extends StructureStrongholdPieces.Stairs2 {

	    public Stairs2() {
        }

        public Stairs2(int p_i2083_1_, Random p_i2083_2_, int p_i2083_3_, int p_i2083_4_) {
            super(p_i2083_1_, p_i2083_2_, p_i2083_3_, p_i2083_4_);
        }
        
        @Override
        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        	
            StructureStrongholdPieces.strongComponentType = StructureStrongholdPieces.PortalRoom.class;
            this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)componentIn, listIn, rand, 1, 1);
        }
    }
}
