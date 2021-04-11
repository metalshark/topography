package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureConfig;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class RegionFeatureRedirector extends Feature<NoFeatureConfig> {
	
	public static final RegionFeatureRedirector INSTANCE = new RegionFeatureRedirector(NoFeatureConfig.field_236558_a_);
	
	private final Random regionPositionRand = new Random();

	public RegionFeatureRedirector(Codec<NoFeatureConfig> codec) {
		super(codec);
		this.setRegistryName(ModInfo.MODID, "region_feature_redirector");
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
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

				//Sort into lists based on region size
				for (ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>> feature : def.regionFeatures) {
					
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
					regionPositionRand.setSeed(this.getRegionSeed(regionX, regionZ, reader.getSeed(), regionSize));
					
					Map<BlockPos, RegionFeatureConfig> allPositions = new LinkedHashMap<BlockPos, RegionFeatureConfig>();
					
					for (ConfiguredFeature<RegionFeatureConfig, RegionFeature<RegionFeatureConfig>> feature : entry.getValue()) {

						List<BlockPos> positions = generatePositions(allPositions, regionSize, regionX, regionZ, feature.config);
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
		return changes;
	}
	
	public long getRegionSeed(int regionX, int regionZ, long worldSeed, int regionSize) {
		return (long) (regionX) * 341873128712L + (long) (regionZ) * 132897987541L + worldSeed + regionSize;
	}
	
	public List<BlockPos> generatePositions(Map<BlockPos, RegionFeatureConfig> allPositions, int regionSize, int regionX, int regionZ, RegionFeatureConfig config) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		
        for (int i = 0; i < config.positionAttemptCount; i++)
        {
            final double maxHorizontalFeatureRadius = config.radius;

            final int regionCenterX = (int) ((regionX) * regionSize + regionSize / 2);
            final int regionCenterZ = (int) ((regionZ) * regionSize + regionSize / 2);

            final int randomSpace = (int) (regionSize - maxHorizontalFeatureRadius * 2);

            final int featureCenterX = regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
            final int featureCenterZ = regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;

            final BlockPos pos = new BlockPos(featureCenterX, 0, featureCenterZ);
            
            if (this.isPostionAcceptable(allPositions, pos, maxHorizontalFeatureRadius, config.minSpacing)) {
            	positions.add(pos);
            	allPositions.put(pos, config);
            }
        }
		return positions;
	}
    
	protected boolean isPostionAcceptable(Map<BlockPos, RegionFeatureConfig> positions, BlockPos pos, double maxHorizontalFeatureRadius, int extraSpacing) {
		for (Entry<BlockPos, RegionFeatureConfig> entry : positions.entrySet()) {
			double minDistance = maxHorizontalFeatureRadius + entry.getValue().radius + entry.getValue().minSpacing + extraSpacing;
			
			if (getHorizontalDistance(pos, entry.getKey()) < minDistance) {
    			return false;
    		}
		}
    	return true;
    }

    protected static double getHorizontalDistance(final BlockPos pos, final BlockPos pos2)//Switch this to using multiplied values instead of sqrt. faster
    {
        double d0 = pos.getX() - pos2.getX();
        double d2 = pos.getZ() - pos2.getZ();
        return Math.sqrt(d0 * d0 + d2 * d2);
    }
}
