package com.bloodnbonesgaming.topography.common.world.gen;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;

public interface IGenerator {
	
	public abstract void generate(IWorld world, IChunk chunk, SharedSeedRandom rand, long seed);
}
