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
	
	public static ItemStack buildItemStack(String itemID, int count) {
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemID));
		return new ItemStack(item, count);
	}
	
	public static ItemStack buildItemStack(String itemID, int count, String nbt) throws CommandSyntaxException {
		ItemStack stack = buildItemStack(itemID, count);
		stack.setTag(buildNBT(nbt));
		return stack;
	}
	
	public static ItemStack buildItemStack(Item item, int count) {
		return new ItemStack(item, count);
	}
	
	public static ItemStack buildItemStack(Item item, int count, String nbt) throws CommandSyntaxException {
		ItemStack stack = buildItemStack(item, count);
		stack.setTag(buildNBT(nbt));
		return stack;
	}
	
	public static ItemEntity buildItemEntity(World world, double x, double y, double z, String itemID, int count) {
		ItemStack stack = buildItemStack(itemID, count);
		return buildItemEntity(world, x, y, z, stack);
	}
	
	public static ItemEntity buildItemEntity(World world, double x, double y, double z, ItemStack stack) {
		return new ItemEntity(world, x, y, z, stack);
	}
	
	public static ItemEntity buildItemEntity(World world, BlockPos pos, String itemID, int count) {
		ItemStack stack = buildItemStack(itemID, count);
		return buildItemEntity(world, pos, stack);
	}
	
	public static ItemEntity buildItemEntity(World world, BlockPos pos, ItemStack stack) {
		return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
	}
	
	public static CompoundNBT buildNBT(String json) throws CommandSyntaxException {
		return JsonToNBT.getTagFromJson(json);
	}
}
