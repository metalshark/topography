package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.StalactiteFormationConfig;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

public class ColumnFormation extends RegionFeature<StalactiteFormationConfig> {
	
	public static final ColumnFormation INSTANCE = new ColumnFormation(StalactiteFormationConfig.CODEC);

	public ColumnFormation(Codec<StalactiteFormationConfig> codec) {
		super(codec);
		this.setRegistryName(ModInfo.MODID, "column_formation");
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, StalactiteFormationConfig config) {
		int chunkX = pos.getX() / 16 * 16;
		int chunkZ = pos.getZ() / 16 * 16;
		int regionX = ((int) Math.floor(Math.floor(chunkX / 16.0D) * 16D / config.regionSize));
		int regionZ = ((int) Math.floor(Math.floor(chunkZ / 16.0D) * 16D / config.regionSize));
		this.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()));
		int minHeight = 81;
		int maxHeight = 174;
		List<BlockPos> positions = generatePositions(config, regionX, regionZ, config.radius);
		boolean changes = false;
		Mutable mutable = new BlockPos.Mutable();
		int posIndex = -1;
		
		for (BlockPos position : positions) {
			posIndex++;
			
			if (canBeInChunk(position, config.radius, chunkX, chunkZ)) {
				//Increment seed for unique rand per feature
				this.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()) + posIndex);
				
				for (int x = 0; x < 16; x++) {
                    final double realX = x + chunkX;
                    final double xDistance = Math.pow(Math.abs(position.getX() - realX), 2);
                    
					for (int z = 0; z < 16; z++) {
	                    final double realZ = z + chunkZ;
	                    final double zDistance = Math.pow(Math.abs(position.getZ() - realZ), 2);
	                    double distanceFromCenter = Math.sqrt(xDistance + zDistance);
	                    //Randomly reduce size per level
						int sizeReduction = 0;
	                    
	                    if (distanceFromCenter <= config.radius) {
							for (int y = maxHeight; y >= minHeight; y--) {
								//Reduce by 0-1 per layer
								if (this.regionPositionRand.nextInt(config.sizeReductionChance) == 0) {
									sizeReduction++;
								}
								
								//Squarify the column
								int xZDiff = config.radius - (config.radius - (int) Math.abs(Math.abs(position.getX() - realX) - Math.abs(position.getZ() - realZ)));
//								sizeReduction += xZDiff / 4;
								
								if (distanceFromCenter <= config.radius - (sizeReduction + xZDiff / 4)) {
									mutable.setPos(x + pos.getX(), y, z + pos.getZ());
									reader.setBlockState(mutable, Blocks.COBBLESTONE.getDefaultState(), 0);
									changes = true;
								} else {
									break;
								}
							}
							sizeReduction = 0;
							
							for (int y = minHeight; y <= maxHeight; y++) {
								//Reduce by 0-1 per layer
								if (this.regionPositionRand.nextInt(config.sizeReductionChance) == 0) {
									sizeReduction++;
								}
								
								//Squarify the column
								int xZDiff = config.radius - (config.radius - (int) Math.abs(Math.abs(position.getX() - realX) - Math.abs(position.getZ() - realZ)));
//								sizeReduction += xZDiff / 4;
								
								if (distanceFromCenter <= config.radius - (sizeReduction + xZDiff / 4)) {
									mutable.setPos(x + pos.getX(), y, z + pos.getZ());
									reader.setBlockState(mutable, Blocks.COBBLESTONE.getDefaultState(), 0);
									changes = true;
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