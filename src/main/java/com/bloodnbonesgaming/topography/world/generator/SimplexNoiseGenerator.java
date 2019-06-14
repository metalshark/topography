package com.bloodnbonesgaming.topography.world.generator;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class SimplexNoiseGenerator implements IGenerator {
	
	@Override
	public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random) {
		
		OpenSimplexNoiseGeneratorOctaves noise = new OpenSimplexNoiseGeneratorOctaves(world.getSeed());
		
		
	}
}
