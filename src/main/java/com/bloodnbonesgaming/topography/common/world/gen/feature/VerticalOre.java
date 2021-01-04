package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class VerticalOre extends Feature<OreFeatureConfig> {
	
	public static final VerticalOre INSTANCE = new VerticalOre(OreFeatureConfig.CODEC);

	public VerticalOre(Codec<OreFeatureConfig> codec) {
		super(codec);
		this.setRegistryName("vertical_ore_vein");
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, OreFeatureConfig config) {
		BlockPos.Mutable mutable = pos.toMutable();
		int placed = 0;
		int x = mutable.getX();
		int y = mutable.getY();
		int z = mutable.getZ();
		
		for (int i = 0; i < config.size; i++) {
			mutable.setPos(x + rand.nextInt(2), y + i, z + rand.nextInt(2));
			
			if (mutable.getY() >= 256 || mutable.getY() <= 0) {
				break;
			}
			
			if (config.target.test(reader.getBlockState(mutable), rand)) {
				reader.setBlockState(mutable, config.state, 2);
				placed++;
			}
		}
		return placed > 0;
	}
}
