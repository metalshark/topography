package com.bloodnbonesgaming.topography.world.biome.provider;

import java.util.List;
import java.util.Map;

import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerBiomeSimple;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerHillsSimple;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerRiverMixSimple;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class SimpleBiomeProvider extends BiomeProvider {
	
	private SimpleBiomeProvider(final List<BiomeEntry> biomes)
    {
		super();
        this.biomesToSpawnIn.clear();
        
        for (final BiomeEntry entry : biomes)
        {
        	this.biomesToSpawnIn.add(entry.biome);
        }
    }

    public SimpleBiomeProvider(long seed, WorldType worldTypeIn, String options, final List<BiomeEntry> biomes, final List<BiomeEntry> oceanBiomes, final Map<Integer, List<Integer>> hills, final Map<Integer, Integer> rivers)
    {
        this(biomes);

        if (worldTypeIn == WorldType.CUSTOMIZED && !options.isEmpty())
        {
            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(options).build();
        }
        
        GenLayer genlayer = new GenLayerIsland(1L);
        genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
        GenLayer genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
        GenLayer genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        GenLayer genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
        genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
        GenLayer genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
        GenLayer genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
        GenLayer genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayer genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayer genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
        genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
        GenLayer genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
//        GenLayer genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
//        GenLayer genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddisland3);
        GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayeraddisland3, 0);
        int i = 4;
        int j = i;

        if (this.settings != null)
        {
            i = this.settings.biomeSize;
            j = this.settings.riverSize;
        }

        if (worldTypeIn == WorldType.LARGE_BIOMES)
        {
            i = 6;
        }

        GenLayer lvt_7_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
        GenLayer genlayerriverinit = new GenLayerRiverInit(100L, lvt_7_1_);
//        GenLayer genlayerbiomeedge = worldTypeIn.getBiomeLayer(seed, genlayer4, this.settings); //Replace with simple biome layer
        GenLayer genlayerbiomeedge = new GenLayerBiomeSimple(seed, genlayer4, biomes, oceanBiomes);
        genlayerbiomeedge = GenLayerZoom.magnify(1000L, genlayerbiomeedge, 2);
//        GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_9_1_); //Replace with simple hill layer
        GenLayer genlayerhills = new GenLayerHillsSimple(1000L, genlayerbiomeedge, hills);
        GenLayer genlayer5 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        genlayer5 = GenLayerZoom.magnify(1000L, genlayer5, j);
        GenLayer genlayerriver = new GenLayerRiver(1L, genlayer5);
        GenLayer genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
//        genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);

        for (int k = 0; k < i; ++k)
        {
            genlayerhills = new GenLayerZoom((long)(1000 + k), genlayerhills);

//            if (k == 0)
//            {
//                genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
//            }

//            if (k == 1 || i == 1)
//            {
//                genlayerhills = new GenLayerShore(1000L, genlayerhills);
//            }
        }

        GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, genlayerhills);
//        GenLayer genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth); //Replace with simple river layer
        GenLayer genlayerrivermix = new GenLayerRiverMixSimple(100L, genlayersmooth1, genlayersmooth, rivers);
        GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(seed);
        genlayer3.initWorldGenSeed(seed);        
        
        
        this.genBiomes = genlayerrivermix;
        this.biomeIndexLayer = genlayer3;
    }
}
