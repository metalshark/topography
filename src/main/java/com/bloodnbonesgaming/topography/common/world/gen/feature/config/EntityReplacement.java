package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import java.util.Random;

import com.bloodnbonesgaming.topography.common.util.Functions.QuinFunction;
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

public class EntityReplacement extends StructureProcessor {
	

	public static final EntityReplacement INSTANCE = new EntityReplacement();
	public static final Codec<StructureProcessor> CODEC = Codec.<StructureProcessor>unit(() -> {
		return INSTANCE;
	}).stable();
	public static IStructureProcessorType<?> TYPE;
	private final QuinFunction<BlockInfo, BlockPos, BlockState, CompoundNBT, Random, BlockInfo> function;
	
	public EntityReplacement(QuinFunction<BlockInfo, BlockPos, BlockState, CompoundNBT, Random, BlockInfo> function) {
		this.function = function;
	}
	
	public EntityReplacement() {
		this(null);
	}
	
	@Override
	public BlockInfo process(IWorldReader reader, BlockPos pos, BlockPos pos2, BlockInfo blockInfo, BlockInfo blockInfo2, PlacementSettings settings, Template template) {
		if (this.function == null)
			return blockInfo2;
		return this.function.apply(blockInfo2, blockInfo2.pos, blockInfo2.state, blockInfo2.nbt, settings.getRandom(null));
	}
	
	@Override
	public EntityInfo processEntity(IWorldReader world, BlockPos seedPos, EntityInfo rawEntityInfo, EntityInfo entityInfo, PlacementSettings placementSettings, Template template) {
		// TODO Auto-generated method stub
		return super.processEntity(world, seedPos, rawEntityInfo, entityInfo, placementSettings, template);
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
