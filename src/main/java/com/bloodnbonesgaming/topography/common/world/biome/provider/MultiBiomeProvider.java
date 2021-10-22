package com.bloodnbonesgaming.topography.common.world.biome.provider;

import java.util.List;
import java.util.function.LongFunction;

import com.bloodnbonesgaming.topography.common.world.gen.layer.RandomBiomeBaseLayer;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.ZoomLayer;

public class MultiBiomeProvider extends BiomeProvider {

//	public static final Codec<MultiBiomeProvider> CODEC = RecordCodecBuilder.create((builder) -> {
//	      return builder.group(Codec.LONG.fieldOf("seed").stable().forGetter((provider) -> {
//	         return provider.seed;
//	      }), Biome.BIOMES_CODEC.fieldOf("biomes").forGetter((provider) -> {
//	          return provider.biomes;
//	      })).apply(builder, builder.stable(MultiBiomeProvider::new));
//	   });
	public static final Codec<MultiBiomeProvider> CODEC = RecordCodecBuilder.<MultiBiomeProvider>create((instance) -> {
	      return instance.group(Codec.LONG.fieldOf("seed").stable().forGetter((provider) -> {
		         return provider.seed;
		      }), RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((provider) -> {
		          return provider.biomeRegistry;
		      })).apply(instance, (arg0, arg1) -> {
				try {
					return new MultiBiomeProvider(arg0, arg1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			});
	   }).stable();
	private final long seed;
	private final int biomeSize;
	private final Registry<Biome> biomeRegistry;
	private final Layer layerGen;

	public MultiBiomeProvider(long seed, Registry<Biome> biomeRegistry) throws Exception {
		this(Lists.newArrayList(biomeRegistry.getOrDefault(new ResourceLocation("plains"))), seed, 4, biomeRegistry);
	}

	public MultiBiomeProvider(List<Biome> biomes, long seed, int biomeSize, Registry<Biome> biomeRegistry) {
		super(biomes);
		this.seed = seed;
		this.biomeSize = biomeSize;
		this.biomeRegistry = biomeRegistry;
		this.layerGen = buildLayerGen(seed, biomes, biomeRegistry, biomeSize);
	}

	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
	      return this.layerGen.func_242936_a(this.biomeRegistry, x, z);
	}

	@Override
	protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
		return CODEC;
	}

	@Override
	public BiomeProvider getBiomeProvider(long seed) {
		return new MultiBiomeProvider(biomes, seed, biomeSize, biomeRegistry);
	}

	public static Layer buildLayerGen(long seed, List<Biome> biomes, Registry<Biome> biomeRegistry, int biomeSize) {
	      IAreaFactory<LazyArea> iareafactory = buildLayers(biomes, biomeRegistry, biomeSize, (p_227473_2_) -> {
	         return new LazyAreaLayerContext(25, seed, p_227473_2_);
	      });
	      return new Layer(iareafactory);
	   }
	
	private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildLayers(List<Biome> biomes, Registry<Biome> biomeRegistry, int biomeSize, LongFunction<C> seedHandler) {
		IAreaFactory<T> iareafactory = new RandomBiomeBaseLayer(biomes, biomeRegistry).apply(seedHandler.apply(1L));
		iareafactory = LayerUtil.repeat(2001L, ZoomLayer.NORMAL, iareafactory, biomeSize, seedHandler);
		return iareafactory;
	}
	
//	private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildLayers(boolean legacyBiomes, int biomeSize, int riverRarity, LongFunction<C> seedHandler) {
//	      IslandLayer.INSTANCE.apply(seedHandler.apply(1L));
//	      iareafactory = ZoomLayer.FUZZY.apply(seedHandler.apply(2000L), iareafactory);
//	      iareafactory = AddIslandLayer.INSTANCE.apply(seedHandler.apply(1L), iareafactory);
//	      iareafactory = ZoomLayer.NORMAL.apply(seedHandler.apply(2001L), iareafactory);
//	      iareafactory = AddIslandLayer.INSTANCE.apply(seedHandler.apply(2L), iareafactory);
//	      iareafactory = AddIslandLayer.INSTANCE.apply(seedHandler.apply(50L), iareafactory);
//	      iareafactory = AddIslandLayer.INSTANCE.apply(seedHandler.apply(70L), iareafactory);
//	      iareafactory = RemoveTooMuchOceanLayer.INSTANCE.apply(seedHandler.apply(2L), iareafactory);
		//Uses noise to spread ocean types
//	      IAreaFactory<T> iareafactory1 = OceanLayer.INSTANCE.apply(seedHandler.apply(2L));
		//Zoom ocean types
//	      iareafactory1 = LayerUtil.repeat(2001L, ZoomLayer.NORMAL, iareafactory1, 6, seedHandler);
//	      iareafactory = AddSnowLayer.INSTANCE.apply(seedHandler.apply(2L), iareafactory);
//	      iareafactory = AddIslandLayer.INSTANCE.apply(seedHandler.apply(3L), iareafactory);
//	      iareafactory = EdgeLayer.CoolWarm.INSTANCE.apply(seedHandler.apply(2L), iareafactory);
//	      iareafactory = EdgeLayer.HeatIce.INSTANCE.apply(seedHandler.apply(2L), iareafactory);
//	      iareafactory = EdgeLayer.Special.INSTANCE.apply(seedHandler.apply(3L), iareafactory);
//	      iareafactory = ZoomLayer.NORMAL.apply(seedHandler.apply(2002L), iareafactory);
//	      iareafactory = ZoomLayer.NORMAL.apply(seedHandler.apply(2003L), iareafactory);
//	      iareafactory = AddIslandLayer.INSTANCE.apply(seedHandler.apply(4L), iareafactory);
//	      iareafactory = AddMushroomIslandLayer.INSTANCE.apply(seedHandler.apply(5L), iareafactory);
//	      iareafactory = DeepOceanLayer.INSTANCE.apply(seedHandler.apply(4L), iareafactory);
//	      iareafactory = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, iareafactory, 0, seedHandler);
		//Starts river chain. Does nothing.
//	      IAreaFactory<T> lvt_6_1_ = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, iareafactory, 0, seedHandler);
//	      lvt_6_1_ = StartRiverLayer.INSTANCE.apply(seedHandler.apply(100L), lvt_6_1_);
//	      IAreaFactory<T> lvt_7_1_ = (new BiomeLayer(legacyBiomes)).apply(seedHandler.apply(200L), iareafactory);
//	      lvt_7_1_ = AddBambooForestLayer.INSTANCE.apply(seedHandler.apply(1001L), lvt_7_1_);
//	      lvt_7_1_ = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, lvt_7_1_, 2, seedHandler);
//	      lvt_7_1_ = EdgeBiomeLayer.INSTANCE.apply(seedHandler.apply(1000L), lvt_7_1_);
//	      IAreaFactory<T> lvt_8_1_ = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, lvt_6_1_, 2, seedHandler);
//	      lvt_7_1_ = HillsLayer.INSTANCE.apply(seedHandler.apply(1000L), lvt_7_1_, lvt_8_1_);
//	      lvt_6_1_ = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, lvt_6_1_, 2, seedHandler);
//	      lvt_6_1_ = LayerUtil.repeat(1000L, ZoomLayer.NORMAL, lvt_6_1_, riverRarity, seedHandler);
//	      lvt_6_1_ = RiverLayer.INSTANCE.apply(seedHandler.apply(1L), lvt_6_1_);
//	      lvt_6_1_ = SmoothLayer.INSTANCE.apply(seedHandler.apply(1000L), lvt_6_1_);
//	      lvt_7_1_ = RareBiomeLayer.INSTANCE.apply(seedHandler.apply(1001L), lvt_7_1_);
//
//	      for(int i = 0; i < biomeSize; ++i) {
//	         lvt_7_1_ = ZoomLayer.NORMAL.apply(seedHandler.apply((long)(1000 + i)), lvt_7_1_);
//	         if (i == 0) {
//	            lvt_7_1_ = AddIslandLayer.INSTANCE.apply(seedHandler.apply(3L), lvt_7_1_);
//	         }
//
//	         if (i == 1 || biomeSize == 1) {
//	            lvt_7_1_ = ShoreLayer.INSTANCE.apply(seedHandler.apply(1000L), lvt_7_1_);
//	         }
//	      }
//
//	      lvt_7_1_ = SmoothLayer.INSTANCE.apply(seedHandler.apply(1000L), lvt_7_1_);
//	      lvt_7_1_ = MixRiverLayer.INSTANCE.apply(seedHandler.apply(100L), lvt_7_1_, lvt_6_1_);
		//Combine main layer with ocean types. Replaces only ocean biomes from main layer with ocean biome variants
//	      return MixOceansLayer.INSTANCE.apply(seedHandler.apply(100L), lvt_7_1_, iareafactory1);
//	   }
}
