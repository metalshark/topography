package com.bloodnbonesgaming.topography.common.world.gen.feature.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bloodnbonesgaming.topography.common.util.IOHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

public class StructureFeatureConfig extends SquareRegionFeatureConfig implements ISquareConfig {

	public static final Codec<StructureFeatureConfig> CODEC = RecordCodecBuilder.create((builder) -> {
		return builder.group(Codec.INT.fieldOf("region_size").forGetter((config) -> {
	    	return config.regionSize;
		}), Codec.INT.fieldOf("min_spacing").forGetter((config) -> {
			return config.minSpacing;
		}), Codec.INT.fieldOf("position_attempt_count").forGetter((config) -> {
			return config.positionAttemptCount;
		}), Codec.INT.fieldOf("height").forGetter((config) -> {
			return config.height;
		}), Codec.STRING.fieldOf("structure_path").forGetter((config) -> {
			return config.path;
		})).apply(builder, StructureFeatureConfig::new);
	});

	public StructureFeatureConfig(int regionSize, int minSpacing, int positionAttemptCount, int height, String path) {
		super(regionSize, minSpacing, positionAttemptCount, 0, 0);
		this.path = path;
		Template template = getTemplate();
		this.sizeX = template.getSize().getX();
		this.sizeZ = template.getSize().getZ();
		this.height = height;
	}

	public final String path;
	public final int height;
	public final List<StructureProcessor> processors = new ArrayList<StructureProcessor>();
	private Template structure = null;
	
	public Template getTemplate() {
		if (structure == null) {
			structure = IOHelper.loadStructureTemplate(path);
		}
		return structure;
	}
	
	public RegionFeatureConfig addProcessor(StructureProcessor processor) {
		this.processors.add(processor);
		return this;
	}
	
	public RegionFeatureConfig removeBlocks(BlockState... states) {
		List<Block> blocks = new ArrayList<Block>();
		
		for (int i = 0; i < states.length; i++) {
			blocks.add(states[i].getBlock());
		}
		this.processors.add(new BlockIgnoreStructureProcessor(blocks));
		return this;
	}
	
	public RegionFeatureConfig removeBlocks(Block... blocks) {
		List<Block> list = new ArrayList<Block>();
		Collections.addAll(list, blocks);
		this.processors.add(new BlockIgnoreStructureProcessor(list));
		return this;
	}
}
