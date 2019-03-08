package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;

public class VanillaCaveGenerator extends MapGenCaves implements IGenerator {
	
	@Override
	public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random) {
		
		this.generate(world, chunkX, chunkZ, primer);
	}
}
