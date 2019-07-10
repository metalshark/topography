package com.bloodnbonesgaming.topography.world.biome.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerBaseSingle;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerBiomeDC;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerBiomeEdgeDC;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerCombine;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerCombineWhitelist;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerDeepOceanDC;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerHeatToBiomes;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerHeatX;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerHeatZ;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerHillsDC;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerRemoveTooMuchOceanDC;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerReplaceAll;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerRiverMixDC;
import com.bloodnbonesgaming.topography.world.biome.provider.layers.GenLayerTouching;

import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

@ScriptClassDocumentation(documentationFile = ModInfo.BIOME_PROVIDER_DOCUMENTATION_FOLDER + "ScriptBiomeProvider", classExplaination = 
"The role of a BiomeProvider is to handle the generation of the dimensions biome map."
	+ " This particular type allows for the creation of a layer based biome mapping system that allows for extreme configuration."
	+ " The most important concept to keep in mind when building a BiomeProvider, is scale. Every zoom/magify increases the relative size of all GenLayers before it in the chain."
	+ " This allows for things to be done on an extremely large scale at the top of the chain, and a very small scale at the bottom of the chain."
	+ " This is extremely complicated, please see default configs for some examples.")
public class BiomeProviderScripted extends BiomeProvider {
	
	private Map<String, Class> classKeywords = new HashMap<String, Class>();
	private Map<String, Object> localVariables = new HashMap<String, Object>();
	private long seed = 0;
	private final List<Biome> spawnBiomes = new ArrayList<Biome>();
	
	{
		this.classKeywords.put("GenLayerIsland", GenLayerIsland.class);
		this.classKeywords.put("GenLayerFuzzyZoom", GenLayerFuzzyZoom.class);
		this.classKeywords.put("GenLayerAddIsland", GenLayerAddIsland.class);
		this.classKeywords.put("GenLayerZoom", GenLayerZoom.class);
		this.classKeywords.put("GenLayerRemoveTooMuchOcean", GenLayerRemoveTooMuchOcean.class);
		this.classKeywords.put("GenLayerAddSnow", GenLayerAddSnow.class);
		this.classKeywords.put("GenLayerEdge", GenLayerEdge.class);
		this.classKeywords.put("GenLayerAddMushroomIsland", GenLayerAddMushroomIsland.class);
		this.classKeywords.put("GenLayerDeepOcean", GenLayerDeepOcean.class);
		this.classKeywords.put("GenLayerRiverInit", GenLayerRiverInit.class);
		this.classKeywords.put("GenLayerBiome", GenLayerBiome.class);
		this.classKeywords.put("GenLayerBiomeEdge", GenLayerBiomeEdge.class);
		this.classKeywords.put("GenLayerHills", GenLayerHills.class);
		this.classKeywords.put("GenLayerRiver", GenLayerRiver.class);
		this.classKeywords.put("GenLayerSmooth", GenLayerSmooth.class);
		this.classKeywords.put("GenLayerRareBiome", GenLayerRareBiome.class);
		this.classKeywords.put("GenLayerShore", GenLayerShore.class);
		this.classKeywords.put("GenLayerRiverMix", GenLayerRiverMix.class);
		this.classKeywords.put("GenLayerVoronoiZoom", GenLayerVoronoiZoom.class);
		
		this.classKeywords.put("GenLayerBiomeDC", GenLayerBiomeDC.class);
		this.classKeywords.put("GenLayerBiomeEdgeDC", GenLayerBiomeEdgeDC.class);
		this.classKeywords.put("GenLayerDeepOceanDC", GenLayerDeepOceanDC.class);
		this.classKeywords.put("GenLayerHillsDC", GenLayerHillsDC.class);
		this.classKeywords.put("GenLayerRemoveTooMuchOceanDC", GenLayerRemoveTooMuchOceanDC.class);
		this.classKeywords.put("GenLayerRiverMixDC", GenLayerRiverMixDC.class);
		
		this.classKeywords.put("GenLayerTouching", GenLayerTouching.class);
		this.classKeywords.put("GenLayerReplaceAll", GenLayerReplaceAll.class);
		this.classKeywords.put("GenLayerBaseSingle", GenLayerBaseSingle.class);
		this.classKeywords.put("GenLayerHeatZ", GenLayerHeatZ.class);
        this.classKeywords.put("GenLayerHeatX", GenLayerHeatX.class);
		this.classKeywords.put("GenLayerHeatToBiomes", GenLayerHeatToBiomes.class);
		this.classKeywords.put("GenLayerCombineWhitelist", GenLayerCombineWhitelist.class);
        this.classKeywords.put("GenLayerCombine", GenLayerCombine.class);
		
		this.localVariables.put("Edge_Mode_Cool_Warm", GenLayerEdge.Mode.COOL_WARM);
		this.localVariables.put("Edge_Mode_Heat_Ice", GenLayerEdge.Mode.HEAT_ICE);
		this.localVariables.put("Edge_Mode_Special", GenLayerEdge.Mode.SPECIAL);
	}
	
	public void init(final World world, final String script)
	{
		this.localVariables.put("worldType", world.getWorldType());
		this.localVariables.put("generatorString", world.getWorldInfo().getGeneratorOptions());
		this.seed = world.getSeed();
		IOHelper.loadBiomeProvider(script, this, this.classKeywords, this.localVariables);
	}
	
	@ScriptMethodDocumentation(args = "Genlayer, Genlayer", usage = "genlayer, zoomed genlayer", notes = 
			"Sets the Genlayer chain for this BiomeProvider."
			+ " The second layer should be the same as the first layer, but with an added GenLayerVoronoiZoom on the end."
			+ " The first one is used for the lerped terrain generation, the second is used for the detailed biome map.")
	public void setGenLayers(final GenLayer layer1, final GenLayer layer2)
	{
		Topography.instance.getLog().debug("Setting GenLayers");
		layer1.initWorldGenSeed(seed);
		layer2.initWorldGenSeed(seed);
		this.genBiomes = layer1;
        this.biomeIndexLayer = layer2;
	}
	
	@ScriptMethodDocumentation(args = "long, Genlayer, int", usage = "seed offset, genlayer to zoom, zoom count", notes = "Adds zoom count GenLayerZoom layers to the provided GenLayer. This is just a helper method to quickly add multiple GenLayerZoom layers at once.")
	public GenLayer magnify(final long seedOffset, final GenLayer parent, final int count)
	{
		return GenLayerZoom.magnify(seedOffset, parent, count);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
    @ScriptMethodDocumentation(usage = "biome ID", notes = "Adds a biome to the list for players to spawn in.")
    public void addSpawnBiome(final int biomeID)
	{
	    final Biome biome = Biome.getBiomeForId(biomeID);
	    
	    if (biome == null)
	    {
	        Topography.instance.getLog().info(biomeID + " is not a biome!");
	    }
	    else
	    {
	        this.spawnBiomes.add(biome);
	    }
	}
	
	@Override
	public List<Biome> getBiomesToSpawnIn()
    {
        return this.spawnBiomes.isEmpty() ? super.getBiomesToSpawnIn() : this.spawnBiomes;
    }
}