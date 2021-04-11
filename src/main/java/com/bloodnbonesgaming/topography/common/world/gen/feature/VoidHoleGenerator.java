package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureConfig;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

public class VoidHoleGenerator extends RegionFeature<RegionFeatureConfig> {
	
	public static final VoidHoleGenerator INSTANCE = new VoidHoleGenerator(RegionFeatureConfig.CODEC);

	public VoidHoleGenerator(Codec<RegionFeatureConfig> codec) {
		super(codec);
		this.setRegistryName(ModInfo.MODID, "void_hole");
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, RegionFeatureConfig config) {
		int chunkX = pos.getX() / 16 * 16;
		int chunkZ = pos.getZ() / 16 * 16;
		int regionX = ((int) Math.floor(Math.floor(chunkX / 16.0D) * 16D / config.regionSize));
		int regionZ = ((int) Math.floor(Math.floor(chunkZ / 16.0D) * 16D / config.regionSize));
		config.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()));
		List<BlockPos> positions = generatePositions(config, regionX, regionZ, config.radius);
		return generate(reader, rand, pos, config, positions);
	}

	@Override
	public boolean generate(ISeedReader reader, Random rand, BlockPos pos, RegionFeatureConfig config, List<BlockPos> positions) {
		int chunkX = pos.getX() / 16 * 16;
		int chunkZ = pos.getZ() / 16 * 16;
		int regionX = ((int) Math.floor(Math.floor(chunkX / 16.0D) * 16D / config.regionSize));
		int regionZ = ((int) Math.floor(Math.floor(chunkZ / 16.0D) * 16D / config.regionSize));
		config.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()));
		int maxHeight = 80;
		boolean changes = false;
		Mutable mutable = new BlockPos.Mutable();
		int posIndex = -1;
		
		for (BlockPos position : positions) {
			posIndex++;
			
			if (canBeInChunk(position, config.radius, chunkX, chunkZ)) {
				//Increment seed for unique rand per feature
				config.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()) + posIndex);
				
				for (int x = 0; x < 16; x++) {
                    final double realX = x + chunkX;
                    final double xDistance = Math.pow(Math.abs(position.getX() - realX), 2);
                    
					for (int z = 0; z < 16; z++) {
	                    final double realZ = z + chunkZ;
	                    final double zDistance = Math.pow(Math.abs(position.getZ() - realZ), 2);
	                    double distanceFromCenter = Math.sqrt(xDistance + zDistance);
	                    //Randomly reduce size per level
//						int sizeReduction = 0;
	                    
	                    if (distanceFromCenter <= config.radius) {
							for (int y = maxHeight; y >= 0; y--) {
								int sizeReduction = config.regionPositionRand.nextInt(10);
//								for (int i = 0; i < config.sizeReductionAttemptCount; i++) {
//									//Reduce by 0-1 per layer
//									if (config.sizeReductionChance > 0 && this.regionPositionRand.nextInt(config.sizeReductionChance) == 0) {
//										sizeReduction ++;
//									}
//								}
								
//								//Squarify the column
//								int xZDiff = config.radius - (config.radius - (int) Math.abs(Math.abs(position.getX() - realX) - Math.abs(position.getZ() - realZ)));
//								sizeReduction += xZDiff / 4;
								
								if (distanceFromCenter <= config.radius) {
									if (config.radius - distanceFromCenter > Math.min(5, config.radius / 2) || config.regionPositionRand.nextInt(4) > 0) {
										mutable.setPos(x + pos.getX(), y, z + pos.getZ());
										reader.setBlockState(mutable, Blocks.AIR.getDefaultState(), 0);
										changes = true;
									}
								} else {
									break;
								}
							}
                        }
					}
				}
			}
		}
		return changes;
	}
}
