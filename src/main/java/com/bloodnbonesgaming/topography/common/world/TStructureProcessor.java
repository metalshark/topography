package com.bloodnbonesgaming.topography.common.world;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;

public abstract class TStructureProcessor extends StructureProcessor {

	@Nullable
	public Template.BlockInfo process(IWorldReader reader, BlockPos pos, BlockPos pos2, BlockInfo blockInfo, BlockInfo blockInfo2, PlacementSettings settings, Template template, List<Template.EntityInfo> extraEntities) {
		return this.process(reader, pos, pos2, blockInfo, blockInfo2, settings, template);
	}
}
