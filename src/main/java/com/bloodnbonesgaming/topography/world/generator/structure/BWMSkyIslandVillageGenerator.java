package com.bloodnbonesgaming.topography.world.generator.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGeneratorV2;

import betterwithmods.common.world.BWMapGenVillage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.MapGenVillage;

public class BWMSkyIslandVillageGenerator extends BWMapGenVillage {
	
	public static MapGenVillage getVillageGenerator(final MapGenVillage village, final SkyIslandGeneratorV2 generator)
	{
		if (village instanceof BWMapGenVillage && !(village instanceof BWMSkyIslandVillageGenerator)) {
			Topography.instance.getLog().info("Using BWM villages");
			return new BWMSkyIslandVillageGenerator(generator);
		}
		else
		{
			Topography.instance.getLog().info("Not BWM? " + village.getClass().getName());
		}
		return village;
	}
	
	private final SkyIslandGeneratorV2 skyIslandGenerator;
	
	public BWMSkyIslandVillageGenerator(final SkyIslandGeneratorV2 generator) {
		this.skyIslandGenerator = generator;
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
                	if (island.getValue().shouldGenerateVillages())
                	{
                    	if (this.rand.nextInt(Math.max(1, island.getValue().getVillageChance())) == 0) {
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
