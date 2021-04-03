package com.bloodnbonesgaming.topography.common.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class WorldHelper {
	
	public static BlockState getState(IWorld world, int x, int y, int z) {
		return getState(world, new BlockPos(x, y, z));
	}
	
	public static BlockState getState(IWorld world, double x, double y, double z) {
		return getState(world, new BlockPos(x, y, z));
	}
	
	public static BlockState getState(IWorld world, BlockPos pos) {
		return world.getBlockState(pos);
	}
	
	public static String getStringID(World world) {
		return world.getDimensionKey().getLocation().toString();
	}
	
	public static ResourceLocation getID(World world) {
		return world.getDimensionKey().getLocation();
	}
	
	public static boolean test(IWorld world, String id) {
		if (world instanceof World) {
			return ((World)world).getDimensionKey().getLocation().toString().equals(id);
		}
		return false;
	}
	
	public static boolean test(IWorld world, ResourceLocation id) {
		if (world instanceof World) {
			return ((World)world).getDimensionKey().getLocation().equals(id);
		}
		return false;
	}
}
