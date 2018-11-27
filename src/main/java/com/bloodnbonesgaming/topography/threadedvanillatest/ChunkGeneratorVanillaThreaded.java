package com.bloodnbonesgaming.topography.threadedvanillatest;

import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

public class ChunkGeneratorVanillaThreaded extends ChunkGeneratorOverworld {

	public ChunkGeneratorVanillaThreaded(World worldIn, long seed, boolean mapFeaturesEnabledIn,
			String generatorOptions) {
		super(worldIn, seed, mapFeaturesEnabledIn, generatorOptions);
		
		this.rand.setSeed(seed);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
	}

}
