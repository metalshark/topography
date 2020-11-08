package com.bloodnbonesgaming.topography.common.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityHelper {
	
	public static void addDrop(LivingDropsEvent event, String entityID, String itemID, int count) {
		EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
		
		if (event.getEntity().getType() == type) {
			event.getDrops().add(ItemHelper.buildItemEntity(event.getEntity().world, event.getEntity().getPosition(), ItemHelper.buildItemStack(itemID, count)));
		}
	}
	
	public static void addDrop(LivingDropsEvent event, String entityID, String itemID, int count, String nbt) throws CommandSyntaxException {
		EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
		
		if (event.getEntity().getType() == type) {
			event.getDrops().add(ItemHelper.buildItemEntity(event.getEntity().world, event.getEntity().getPosition(), ItemHelper.buildItemStack(itemID, count, nbt)));
		}
	}
	
	public static void addDrop(LivingDropsEvent event, String entityID, ItemEntity itemEntity) {
		EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
		
		if (event.getEntity().getType() == type) {
			event.getDrops().add(itemEntity);
		}
	}
	
	public static void addDrop(LivingDropsEvent event, String entityID, ItemStack stack) {
		EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
		
		if (event.getEntity().getType() == type) {
			event.getDrops().add(ItemHelper.buildItemEntity(event.getEntity().world, event.getEntity().getPosition(), stack));
		}
	}
}
