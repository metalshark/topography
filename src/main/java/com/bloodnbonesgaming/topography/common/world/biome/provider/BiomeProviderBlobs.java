package com.bloodnbonesgaming.topography.common.world.biome.provider;

import java.util.List;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.BiomeHelper;
import com.bloodnbonesgaming.topography.common.util.noise.NoiseUtil;
import com.bloodnbonesgaming.topography.common.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.biome.provider.SingleBiomeProvider;

import com.google.common.collect.ImmutableList;

public class BiomeProviderBlobs extends BiomeProvider {

	public static final Codec<BiomeProviderBlobs> CODEC = RecordCodecBuilder.create((builder) -> {
	      return builder.group(Codec.LONG.fieldOf("seed").stable().forGetter((provider) -> {
	         return provider.seed;
	      })).apply(builder, builder.stable(BiomeProviderBlobs::new));
	   });
	private final OpenSimplexNoiseGeneratorOctaves simplex;
	private final long seed;
	
	public BiomeProviderBlobs(long seed) {
		this(BiomeHelper.allBiomes(), seed);
	}

	public BiomeProviderBlobs(List<Biome> biomes, long seed) {
		super(ImmutableList.copyOf(biomes));
		this.seed = seed;
		simplex = new OpenSimplexNoiseGeneratorOctaves(seed);
	}

	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		double minNoise = 0.35;
		double val = simplex.eval(x / 128d, y / 32d, z / 128d, 4, 0.5);
		
		double heightReduction;
		
		//Reduce noise result as y gets further from 128
		if (y >= 128) {
			heightReduction = (y - 128) / 128D;
		} else {
			heightReduction = (128 - y) / 128D;
		}
		heightReduction *= (1 - minNoise);
		
		try {
			if (val - heightReduction >= minNoise) {
				return BiomeHelper.getBiome("plains");
			} else {
				return BiomeHelper.getBiome("the_void");
			}
		} catch(Exception e) {
			Topography.getLog().error("Error getting biome for gen: ", e);
		}
		return null;
	}

	@Override
	protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
		return BiomeProviderBlobs.CODEC;
	}

	@Override
	public BiomeProvider getBiomeProvider(long seed) {
		return this;
	}

}
