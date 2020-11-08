package com.bloodnbonesgaming.topography.common.world.biome.provider.test;

import net.minecraft.util.IObjectIntIterable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeContainer;
import net.minecraft.world.biome.provider.BiomeProvider;

public class BiomeContainerPassthrough extends BiomeContainer {
	
	private final BiomeProvider provider;

	public BiomeContainerPassthrough(IObjectIntIterable<Biome> biomeRegistry, ChunkPos chunkPos, BiomeProvider provider) {
      super(biomeRegistry, chunkPos, provider);

      this.provider = provider;
   }

	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		return provider.getNoiseBiome(x, y, z);
	}
}
