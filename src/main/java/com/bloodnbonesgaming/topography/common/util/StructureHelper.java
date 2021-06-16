package com.bloodnbonesgaming.topography.common.util;

import net.minecraft.block.Blocks;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;

public class StructureHelper {

	public static BlockPos getSpawn(final Template template)
    {
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
}