package com.bloodnbonesgaming.topography.util;

import net.minecraft.util.math.BlockPos;

public class MathUtil {

	public static double getDistance(final BlockPos pos, final BlockPos pos2)
    {
        double d0 = pos.getX() - pos2.getX();
        double d2 = pos.getZ() - pos2.getZ();
        return Math.sqrt(d0 * d0 + d2 * d2);
    }

	public static double getDistanceSq(final BlockPos pos, final BlockPos pos2)
    {
        double d0 = pos.getX() - pos2.getX();
        double d2 = pos.getZ() - pos2.getZ();
        return d0 * d0 + d2 * d2;
    }
}
