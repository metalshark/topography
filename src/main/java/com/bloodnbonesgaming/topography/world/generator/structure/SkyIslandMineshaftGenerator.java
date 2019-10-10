package com.bloodnbonesgaming.topography.world.generator.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGeneratorV2;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public class SkyIslandMineshaftGenerator extends MapGenMineshaft {
	
	private final SkyIslandGeneratorV2 skyIslandGenerator;
	
	public SkyIslandMineshaftGenerator(final SkyIslandGeneratorV2 generator) {
		this.skyIslandGenerator = generator;
	}
	
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		
		StructureStart start = super.getStructureStart(chunkX, chunkZ);
		
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
                    if (island.getValue().shouldGenerateMineshafts())
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
	
	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		
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
                	if (island.getValue().shouldGenerateMineshafts())
                    {
                    	if (this.rand.nextInt(Math.max(1, island.getValue().getMineshaftChance())) == 0) {
                        	return true;
                    	}
                    }
                	return false;
                }
            }
        }
    	return false;
    }
}
