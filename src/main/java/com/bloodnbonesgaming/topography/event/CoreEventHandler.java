package com.bloodnbonesgaming.topography.event;

import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.bnbgamingcore.events.StructureVillageFillBlocksDownEvent;
import com.bloodnbonesgaming.bnbgamingcore.events.StructureVillageGetAverageGroundLevelEvent;
import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandDataV2;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.util.MathUtil;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGeneratorV2;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CoreEventHandler {
	
	@SubscribeEvent
	public void onVillageGetAverageGroundLevel(StructureVillageGetAverageGroundLevelEvent event) {
		
		
		Map<SkyIslandData, Map<BlockPos, SkyIslandType>> islandPositions = null;
		
		final ConfigurationManager manager = ConfigurationManager.getInstance();
    	
    	if (manager != null)
    	{
        	final ConfigPreset preset = manager.getPreset();
        	
        	if (preset != null)
            {
            	final DimensionDefinition dimensionDef = preset.getDefinition(event.world.provider.getDimension());
            	
            	if (dimensionDef != null)
            	{
            		for (final IGenerator generator : dimensionDef.getGenerators())
                    {
                        if (generator instanceof SkyIslandGeneratorV2)
                        {
                            final SkyIslandGeneratorV2 islandGenerator = (SkyIslandGeneratorV2) generator;
                            
                            islandPositions = islandGenerator.getIslandPositions(event.world.getSeed(), event.piece.getBoundingBox().minX, event.piece.getBoundingBox().minZ);
                            break;
                        }
                    }

            		if (islandPositions != null) {
            			
            			int highestHeight = 0;

                		int i = 0;
                        int j = 0;
                        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                        for (int k = event.piece.getBoundingBox().minZ; k <= event.piece.getBoundingBox().maxZ; ++k)
                        {
                            for (int l = event.piece.getBoundingBox().minX; l <= event.piece.getBoundingBox().maxX; ++l)
                            {
                            	int islandMid = 0;
                            	double shortestDistance = 99999999;
                            	BlockPos closestIsland = null;
                                blockpos$mutableblockpos.setPos(l, 64, k);
                                outer:
                                for (Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> set : islandPositions.entrySet()) {
                                	for (Entry<BlockPos, SkyIslandType> innerSet : set.getValue().entrySet()) {
                                		if (set.getKey() instanceof SkyIslandDataV2) {
                                			SkyIslandDataV2 data = (SkyIslandDataV2)set.getKey();
                                			double distance = MathUtil.getDistance(blockpos$mutableblockpos, innerSet.getKey());
                                			
                                    		if (distance < data.getHorizontalRadius() * 2 + 25 && distance < shortestDistance) {
                                    			shortestDistance = distance;
                                    			closestIsland = innerSet.getKey();
                                    			islandMid = innerSet.getKey().getY() + (int)((SkyIslandDataV2)set.getKey()).getWaterHeight();
                                    			
                                    			if (shortestDistance <= 25) {
                                        			break outer;
                                    			}
                                    		}
                                		}
                                	}
                                }

                                if (event.boundingBox.isVecInside(blockpos$mutableblockpos))
                                {
//                                	if (closestIsland != null) {
//                                		MutableBlockPos mutable = new MutableBlockPos();
//                                		
//                                    	if (MathUtil.getDistance(closestIsland, mutable.setPos(event.boundingBox.minX, 0, event.boundingBox.minZ)) > 10) {
//                                    		if (MathUtil.getDistance(closestIsland, mutable.setPos(event.boundingBox.minX, 0, event.boundingBox.maxZ)) > 10) {
//                                    			if (MathUtil.getDistance(closestIsland, mutable.setPos(event.boundingBox.maxX, 0, event.boundingBox.minZ)) > 10) {
//                                    				if (MathUtil.getDistance(closestIsland, mutable.setPos(event.boundingBox.maxX, 0, event.boundingBox.maxZ)) > 10) {
//                                    					event.averageGroundLevel = -1;
//                                                    	return;
//                                                	}
//                                            	}
//                                        	}
//                                    	}
//                                	}
                                	
                                	int topSolid = event.world.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY();
                                	if (topSolid > highestHeight) {
                                		highestHeight = topSolid;
                                	}
                                    i += Math.max(topSolid, islandMid);
                                    ++j;
                                }
                            }
                        }
//                        if (highestHeight == 0) {
//                        	event.averageGroundLevel = -1;
//                        	return;
//                        }
                        
                        if (j == 0)
                        {
                        	event.averageGroundLevel = -2;
                        	return;
                        }
                        else
                        {
                        	event.averageGroundLevel = i / j;
                        	return;
                        }
            		}
            	}
            }
    	}
	}

	@SubscribeEvent
	public void onVillageBuildBlocksDown(StructureVillageFillBlocksDownEvent event) {
		Village piece = event.piece;
		StructureBoundingBox box = event.boundingBox;
		int x = event.x;
		int y = event.y;
		int z = event.z;
		World world = event.world;
		int i = this.getXWithOffset(piece, x, z);
        int j = this.getYWithOffset(piece, y);
        int k = this.getZWithOffset(piece, x, z);
        
//        boolean onlyAir = true;
//        
//        for (int ii = j; ii > 0; ii--) {
//        	
//        	if (!world.isAirBlock(new BlockPos(i, ii, k))) {
//        		//event.setCanceled(true);
//        		return;
//            }
////        	if (box.isVecInside(new BlockPos(i, ii, k)))
////            {
////                if (!world.isAirBlock(new BlockPos(i, ii, k))) {
////            		//event.setCanceled(true);
////            		return;
////                }
////            }
//        }
        
        if (box.isVecInside(new BlockPos(i, j, k)))
        {
            while (world.isAirBlock(new BlockPos(i, j, k)) && j > 1)
            {
            	//world.setBlockState(new BlockPos(i, j, k), event.state, 2);
            	
                --j;
            }
//            if ((!world.isAirBlock(new BlockPos(i, j, k)) && !world.getBlockState(new BlockPos(i, j, k)).getMaterial().isLiquid())) {
//        		event.setCanceled(false);
//            	return;
//            }
        }
        if (j <= 1) {
    		event.setCanceled(true);
        }
        
		//event.setCanceled(true);
	}
	
	protected int getXWithOffset(Village piece, int x, int z)
    {
        EnumFacing enumfacing = piece.getCoordBaseMode();

        if (enumfacing == null)
        {
            return x;
        }
        else
        {
            switch (enumfacing)
            {
                case NORTH:
                case SOUTH:
                    return piece.getBoundingBox().minX + x;
                case WEST:
                    return piece.getBoundingBox().maxX - z;
                case EAST:
                    return piece.getBoundingBox().minX + z;
                default:
                    return x;
            }
        }
    }

    protected int getYWithOffset(Village piece, int y)
    {
        return piece.getCoordBaseMode() == null ? y : y + piece.getBoundingBox().minY;
    }

    protected int getZWithOffset(Village piece, int x, int z)
    {
        EnumFacing enumfacing = piece.getCoordBaseMode();

        if (enumfacing == null)
        {
            return z;
        }
        else
        {
            switch (enumfacing)
            {
                case NORTH:
                    return piece.getBoundingBox().maxZ - z;
                case SOUTH:
                    return piece.getBoundingBox().minZ + z;
                case WEST:
                case EAST:
                    return piece.getBoundingBox().minZ + x;
                default:
                    return z;
            }
        }
    }
}
