package com.bloodnbonesgaming.topography.common.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemHelper {
	
	public static ItemStack buildStack(String itemID) {
		return buildStack(itemID, 1);
	}
	
	public static ItemStack buildStack(String itemID, int count) {
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemID));
		return new ItemStack(item, count);
	}
	
	public static ItemStack buildStack(String itemID, String nbt) throws CommandSyntaxException {
		return buildStack(itemID, 1, nbt);
	}
	
	public static ItemStack buildStack(String itemID, int count, String nbt) throws CommandSyntaxException {
		ItemStack stack = buildStack(itemID, count);
		stack.setTag(buildNBT(nbt));
		return stack;
	}
	
	public static ItemStack buildStack(Item item) {
		return buildStack(item, 1);
	}
	
	public static ItemStack buildStack(Item item, int count) {
		return new ItemStack(item, count);
	}
	
	public static ItemStack buildStack(Item item, String nbt) throws CommandSyntaxException {
		return buildStack(item, 1, nbt);
	}
	
	public static ItemStack buildStack(Item item, int count, String nbt) throws CommandSyntaxException {
		ItemStack stack = buildStack(item, count);
		stack.setTag(buildNBT(nbt));
		return stack;
	}
	
	public static ItemEntity buildEntity(World world, double x, double y, double z, String itemID, int count) {
		ItemStack stack = buildStack(itemID, count);
		return buildEntity(world, x, y, z, stack);
	}
	
	public static ItemEntity buildEntity(World world, double x, double y, double z, ItemStack stack) {
		return new ItemEntity(world, x, y, z, stack);
	}
	
	public static ItemEntity buildEntity(World world, BlockPos pos, String itemID, int count) {
		ItemStack stack = buildStack(itemID, count);
		return buildEntity(world, pos, stack);
	}
	
	public static ItemEntity buildEntity(World world, BlockPos pos, ItemStack stack) {
		return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	}
	
	public static CompoundNBT buildNBT(String json) throws CommandSyntaxException {
		return JsonToNBT.getTagFromJson(json);
	}
}
