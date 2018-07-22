package com.bloodnbonesgaming.topography;

import java.util.Map.Entry;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class StructureHelper
{
    public static BlockPos getSpawn(final Template template)
    {
        for (final Entry<BlockPos, String> data : template.getDataBlocks(new BlockPos(0, 0, 0), new PlacementSettings()).entrySet())
        {
            if (data.getValue().equals("SPAWN_POINT"))
            {
                return data.getKey();
            }
        }
        return new BlockPos(-1, 128, -1);
    }
}
