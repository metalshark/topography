package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.common.util.Functions.HepFunction;
import com.bloodnbonesgaming.topography.common.world.TStructureProcessor;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.Template.EntityInfo;

public class BlockReplacement extends TStructureProcessor {
	

	public static final BlockReplacement INSTANCE = new BlockReplacement();
	public static final Codec<StructureProcessor> CODEC = Codec.<StructureProcessor>unit(() -> {
		return INSTANCE;
	}).stable();
	public static IStructureProcessorType<?> TYPE;
	private final HepFunction<BlockInfo, BlockPos, BlockState, CompoundNBT, Random, List<EntityInfo>, BlockInfo, BlockPos> function;
	
	public BlockReplacement(HepFunction<BlockInfo, BlockPos, BlockState, CompoundNBT, Random, List<EntityInfo>, BlockInfo, BlockPos> function) {
		this.function = function;
	}
	
	public BlockReplacement() {
		this(null);
	}
	
	@Override
	public BlockInfo process(IWorldReader reader, BlockPos pos, BlockPos pos2, BlockInfo blockInfo, BlockInfo blockInfo2, PlacementSettings settings, Template template, List<EntityInfo> extraEntities) {
		if (this.function == null)
			return blockInfo2;
		return this.function.apply(blockInfo2, blockInfo2.pos, blockInfo2.state, blockInfo2.nbt, settings.getRandom(null), extraEntities, blockInfo.pos);
	}

	@Override
	protected IStructureProcessorType<?> getType() {
		return TYPE;
	}

	public static void register() {
		TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, "topography_block_replacement", () -> {
			return CODEC;
		});
	}
}
