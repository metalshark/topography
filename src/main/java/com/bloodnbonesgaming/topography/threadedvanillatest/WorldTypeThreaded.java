package com.bloodnbonesgaming.topography.threadedvanillatest;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeThreaded extends WorldType {

	public WorldTypeThreaded(String name) {
		super(name);
	}
	
	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return super.getChunkGenerator(world, generatorOptions);
	}

}
