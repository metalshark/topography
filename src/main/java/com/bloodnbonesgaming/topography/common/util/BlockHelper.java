package com.bloodnbonesgaming.topography.common.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockHelper {

	public static Block getBlock(String location) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location));
	}
	
	public static BlockState getState(String location) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location)).getDefaultState();
	}
	
//	public static BlockState getState(String location) {
//		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location)).getDefaultState().with(property, value);
//	}
}
