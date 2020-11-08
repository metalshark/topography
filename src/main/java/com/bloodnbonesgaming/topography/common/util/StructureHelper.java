package com.bloodnbonesgaming.topography.common.util;

import java.util.Map.Entry;

import net.minecraft.block.Blocks;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;

public class StructureHelper {

	public static BlockPos getSpawn(final Template template)
    {
//        for (final Entry<BlockPos, String> data : template.getDataBlocks(new BlockPos(0, 0, 0), new PlacementSettings()).entrySet())
//        {
//            if (data.getValue().equals("SPAWN_POINT"))
//            {
//                return data.getKey();
//            }
//        }
        
        for (final BlockInfo entry : template.func_215386_a(new BlockPos(0, 0, 0), new PlacementSettings(), Blocks.STRUCTURE_BLOCK, false)) {
        	if (entry.state.getBlock() == Blocks.STRUCTURE_BLOCK && entry.nbt != null) {
        		String mode = entry.nbt.getString("mode");
        		
        		if (!mode.isEmpty() && StructureMode.valueOf(mode) == StructureMode.DATA) {
        			String meta = entry.nbt.getString("metadata");
        			
        			if (meta.equalsIgnoreCase("spawn") || meta.equalsIgnoreCase("spawn_point")) {
        				return entry.pos;
        			}
        		}
        	}
        }
        
        return new BlockPos(-1, 128, -1);
    }
	
	
	
//    public Map<BlockPos, String> getDataBlocks(BlockPos pos, PlacementSettings placementIn)
//    {
//        Map<BlockPos, String> map = Maps.<BlockPos, String>newHashMap();
//        StructureBoundingBox structureboundingbox = placementIn.getBoundingBox();
//
//        for (Template.BlockInfo template$blockinfo : this.blocks)
//        {
//            BlockPos blockpos = transformedBlockPos(placementIn, template$blockinfo.pos).add(pos);
//
//            if (structureboundingbox == null || structureboundingbox.isVecInside(blockpos))
//            {
//                IBlockState iblockstate = template$blockinfo.blockState;
//
//                if (iblockstate.getBlock() == Blocks.STRUCTURE_BLOCK && template$blockinfo.tileentityData != null)
//                {
//                    TileEntityStructure.Mode tileentitystructure$mode = TileEntityStructure.Mode.valueOf(template$blockinfo.tileentityData.getString("mode"));
//
//                    if (tileentitystructure$mode == TileEntityStructure.Mode.DATA)
//                    {
//                        map.put(blockpos, template$blockinfo.tileentityData.getString("metadata"));
//                    }
//                }
//            }
//        }
//
//        return map;
//    }
}
