package com.bloodnbonesgaming.topography.common.util;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockHelper {

	public static Block getBlock(String location) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location));
	}
	
	public Block getBlock(BlockState state) {
		return state.getBlock();
	}
	
	public static BlockState getState(String location) {
		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location)).getDefaultState();
	}
	
//	public static BlockState getState(String location) {
//		return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location)).getDefaultState().with(property, value);
//	}
	
	public BlockInfo buildRandomSpawnerBlockInfo(BlockInfo original, PlacementSettings settings) {
		MobSpawnerTileEntity tile = buildRandomSpawner(settings.getRandom(null));
		return new BlockInfo(original.pos, Blocks.SPAWNER.getDefaultState(), getNBT(tile));
	}
	
	public CompoundNBT getNBT(TileEntity tile) {
		return tile.serializeNBT();
	}
	
	public BlockInfo buildBlockInfo(BlockPos pos, BlockState state, CompoundNBT nbt) {
		return new BlockInfo(pos, state, nbt);
	}
	
	public MobSpawnerTileEntity buildRandomSpawner(Random rand) {
		MobSpawnerTileEntity tile = new MobSpawnerTileEntity();
		tile.getSpawnerBaseLogic().setEntityType(DungeonHooks.getRandomDungeonMob(rand));
		return tile;
	}
	
	public MobSpawnerTileEntity buildSpawner(String entity) {
		MobSpawnerTileEntity tile = new MobSpawnerTileEntity();
		tile.getSpawnerBaseLogic().setEntityType(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entity)));
		return tile;
	}
	
	public CompoundNBT setChestLoot(BlockState state, CompoundNBT nbt, Random rand, String table) {
		LockableLootTileEntity tile = (LockableLootTileEntity) LockableLootTileEntity.readTileEntity(state, nbt);
		tile.setLootTable(new ResourceLocation(table), rand.nextLong());
		return tile.serializeNBT();
	}
	
	public CompoundNBT buildChestLoot(Random rand, String table) {
		ChestTileEntity tile = new ChestTileEntity();
		tile.setLootTable(new ResourceLocation(table), rand.nextLong());
		return tile.serializeNBT();
	}
}
