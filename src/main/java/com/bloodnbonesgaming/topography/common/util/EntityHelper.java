package com.bloodnbonesgaming.topography.common.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityHelper {
	
	public static String getStringID(Entity entity) {
		return ForgeRegistries.ENTITIES.getKey(entity.getType()).toString();
	}
	
	public static ResourceLocation getID(Entity entity) {
		return ForgeRegistries.ENTITIES.getKey(entity.getType());
	}
	
	public static boolean test(Entity entity, String id) {
		return ForgeRegistries.ENTITIES.getKey(entity.getType()).toString().equals(id);
	}
	
	public static boolean test(Entity entity, ResourceLocation id) {
		return ForgeRegistries.ENTITIES.getKey(entity.getType()).equals(id);
	}
	
	public static void addDrop(LivingDropsEvent event, String entityID, String itemID, int count) {
		EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
		
		if (event.getEntity().getType() == type) {
			event.getDrops().add(ItemHelper.buildEntity(event.getEntity().world, event.getEntity().getPosition(), ItemHelper.buildStack(itemID, count)));
		}
	}
	
	public static void addDrop(LivingDropsEvent event, String entityID, String itemID, int count, String nbt) throws CommandSyntaxException {
		EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
		
		if (event.getEntity().getType() == type) {
			event.getDrops().add(ItemHelper.buildEntity(event.getEntity().world, event.getEntity().getPosition(), ItemHelper.buildStack(itemID, count, nbt)));
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
			event.getDrops().add(ItemHelper.buildEntity(event.getEntity().world, event.getEntity().getPosition(), stack));
		}
	}
}
