package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureConfig;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureRedirectorConfig;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;

public class RegionFeatureRedirector extends Feature<RegionFeatureRedirectorConfig> {
	
	public static final RegionFeatureRedirector INSTANCE = new RegionFeatureRedirector(RegionFeatureRedirectorConfig.CODEC);
	
	private final Random regionPositionRand = new Random();

	public RegionFeatureRedirector(Codec<RegionFeatureRedirectorConfig> codec) {
		super(codec);
		this.setRegistryName(ModInfo.MODID, "region_feature_redirector");
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, RegionFeatureRedirectorConfig config) {
		Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
		boolean changes = false;
		
		if (preset != null) {
			DimensionDef def = preset.defs.get(reader.getWorld().getDimensionKey().getLocation());
			
			if (def != null) {
				int chunkX = pos.getX() / 16 * 16;
				int chunkZ = pos.getZ() / 16 * 16;
				Map<Integer, List<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>> regionSizeMap = new LinkedHashMap<Integer, List<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>>();
				List<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>> regionFeatures = new ArrayList<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>();
				Map<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>, List<BlockPos>> featurePositions = new HashMap<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>, List<BlockPos>>();

				if (def.regionFeatures.containsKey(config.stage)) {
					//Sort into lists based on region size
					for (ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>> feature : def.regionFeatures.get(config.stage)) {
						
						if (!regionSizeMap.containsKey(feature.config.regionSize)) {
							regionSizeMap.put(feature.config.regionSize, new ArrayList<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>());
						}
						regionSizeMap.get(feature.config.regionSize).add(feature);
						regionFeatures.add(feature);
					}
					//Generate positions
					for (Entry<Integer, List<ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>>>> entry : regionSizeMap.entrySet()) {
						int regionSize = entry.getKey();
						int regionX = ((int) Math.floor(Math.floor(chunkX / 16.0D) * 16D / regionSize));
						int regionZ = ((int) Math.floor(Math.floor(chunkZ / 16.0D) * 16D / regionSize));
						long seed = this.getRegionSeed(regionX, regionZ, reader.getSeed(), regionSize);
						regionPositionRand.setSeed(seed);
						
						Map<BlockPos, RegionFeatureConfig> allPositions = new LinkedHashMap<BlockPos, RegionFeatureConfig>();
						
						for (ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>> feature : entry.getValue()) {

							feature.config.regionPositionRand.setSeed(seed);
							List<BlockPos> positions = feature.config.generatePositions(allPositions, regionX, regionZ);
							featurePositions.put(feature, positions);
						}
					}
					//Generate features
					for (ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>> feature : regionFeatures) {
						if (feature.feature.generate(reader, rand, pos, feature.config, featurePositions.get(feature))) {
							changes = true;
						}
					}
				}
			}
		}
		return changes;
	}
	
	public long getRegionSeed(int regionX, int regionZ, long worldSeed, int regionSize) {
		return (long) (regionX) * 341873128712L + (long) (regionZ) * 132897987541L + worldSeed + regionSize;
	}
}
