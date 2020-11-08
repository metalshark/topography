package com.bloodnbonesgaming.topography.common.world.gen;

import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;

public class ScriptFeature extends Feature<NoFeatureConfig> {
	
	private final GenerationStage.Decoration stage;

	public ScriptFeature(Codec<NoFeatureConfig> codec, GenerationStage.Decoration stage) {
		super(codec);
		this.stage = stage;
	}

	//Generate method. Return if you did anything.
	@Override
	public boolean generate(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random rand, BlockPos pos, NoFeatureConfig config) {
		boolean modified = false;
		try {
			Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
			
			if (preset != null) {
				DimensionDef def;
				
				if (seedReader instanceof ServerWorld) {
					def = preset.defs.get(((ServerWorld)seedReader).getDimensionKey().getLocation());
				} else {
					def = preset.defs.get(((WorldGenRegion)seedReader).getWorld().getDimensionKey().getLocation());
				}
				
				if (def != null) {
					List<ConfiguredFeature<?, ?>> features = def.features.get(stage);
					
					if (features != null) {
						for (ConfiguredFeature<?, ?> feature : features) {
							boolean ret = feature.generate(seedReader, chunkGenerator, rand, pos);
							
							if (ret) {
								modified = true;
							}
						}
					}
				}
			}
		}
		catch(Exception e) {
			Topography.getLog().error("Error running features from script: ", e);
		}
		return modified;
	}

}
