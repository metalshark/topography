package com.bloodnbonesgaming.topography.common.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.Template.EntityInfo;
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
	
	public static void modifyAttribute(Entity entity, String attribute, String name, double amount, AttributeModifier.Operation operation) throws Exception {
		if (!(entity instanceof LivingEntity)) {
			throw new Exception("Can only modify attribute of living entities");
		}
		LivingEntity living = (LivingEntity)entity;
		Attribute att = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
		if (!(entity instanceof LivingEntity)) {
			throw new Exception("Attribute " + attribute + " is not registered");
		}
		ModifiableAttributeInstance ins = living.getAttribute(att);
		
		if (ins != null) {
			ins.applyPersistentModifier(new AttributeModifier(name, amount, operation));
		}
	}
	
	public static void healToMax(LivingEntity entity) {
		entity.setHealth(entity.getMaxHealth());
	}
	
	public BlockPos getPos(Entity entity) {
		return entity.getPosition();
	}
	
	public int getLight(Entity entity) {
		return Util.World.getLight(getWorld(entity), getPos(entity));
	}
	
	public World getWorld(Entity entity) {
		return entity.world;
	}
	
	public EntityInfo buildEntityInfo(Vector3d pos, BlockPos blockPos, CompoundNBT nbt) {
		return new EntityInfo(pos, blockPos, nbt);
	}
	
	public EntityInfo buildEntityInfo(BlockPos pos, BlockPos blockPos, CompoundNBT nbt) {
		return new EntityInfo(Vector3d.copy(pos), blockPos, nbt);
	}
	
	public CompoundNBT buildNBT(String str) throws CommandSyntaxException {
		return JsonToNBT.getTagFromJson(str);
	}
}
