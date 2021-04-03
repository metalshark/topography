package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureConfig;
import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;

public class ColumnFormation extends RegionFeature<RegionFeatureConfig> {
	
	public static final ColumnFormation INSTANCE = new ColumnFormation(RegionFeatureConfig.codec);

	public ColumnFormation(Codec<RegionFeatureConfig> codec) {
		super(codec);
		this.setRegistryName(ModInfo.MODID, "column_formation");
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, RegionFeatureConfig config) {
		//this.currentRegionX = ((int) Math.floor(Math.floor(x / 16.0D) * 16D / this.regionSize));
        //this.currentRegionZ = ((int) Math.floor(Math.floor(z / 16.0D) * 16D / this.regionSize));
		//this.islandPositionRandom.setSeed((long) (this.currentRegionX) * 341873128712L + (long) (this.currentRegionZ) * 132897987541L + worldSeed);
		int chunkX = pos.getX() / 16 * 16;
		int chunkZ = pos.getZ() / 16 * 16;
		int regionX = ((int) Math.floor(Math.floor(chunkX / 16.0D) * 16D / config.regionSize));
		int regionZ = ((int) Math.floor(Math.floor(chunkZ / 16.0D) * 16D / config.regionSize));
		this.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()));
		int minRadius = (int)Math.max(config.radius * 0.75, 1);
		int minHeight = 81;
		int maxHeight = 174;
		List<BlockPos> positions = generatePositions(config, regionX, regionZ, config.radius);
		boolean changes = false;
		Mutable mutable = new BlockPos.Mutable();
		
		for (BlockPos position : positions) {
			if (canBeInChunk(position, config.radius, chunkX, chunkZ)) {
				for (int x = 0; x < 16; x++) {
                    final double realX = x + chunkX;
                    final double xDistance = Math.pow(Math.abs(position.getX() - realX), 2);
                    
					for (int z = 0; z < 16; z++) {
	                    final double realZ = z + chunkZ;
	                    final double zDistance = Math.pow(Math.abs(position.getZ() - realZ), 2);
	                    double distanceFromCenter = Math.sqrt(xDistance + zDistance);
	                    
	                    if (distanceFromCenter <= config.radius) {
							for (int y = minHeight; y <= maxHeight; y++) {
								int distanceFromTop = maxHeight - y;
								int distanceFromBottom = y - minHeight;
								int distanceFromEnd = distanceFromTop < distanceFromBottom ? distanceFromTop : distanceFromBottom;
								double halfHeight = (maxHeight - minHeight) / 2 + minHeight;
								int distanceFromMid = (maxHeight - minHeight) / 2 - distanceFromEnd;
								double distancePercentage = distanceFromEnd / halfHeight;
								distancePercentage *= 10;//Increase reduction faster as it gets closer to the center
								
								
								
								int maxRadiusReduction = config.radius - minRadius;
								int sizeReduction = (int) Math.min(maxRadiusReduction, distancePercentage * maxRadiusReduction);
								
								try {
									//Randomly remove outside blocks. Increase the closer to the center
									if (this.regionPositionRand.nextInt(distanceFromMid / 5 + 2) == 0) {
										sizeReduction++;
									}
								} catch (Exception e) {
									Topography.getLog().info(distanceFromMid / 5 + 2 + " " + distanceFromMid + " " + y + " " + halfHeight + " " + distanceFromEnd + " " + minHeight);
									throw e;
								}
								
								//Squarify the column
								int xZDiff = config.radius - (config.radius - (int) Math.abs(Math.abs(position.getX() - realX) - Math.abs(position.getZ() - realZ)));
								sizeReduction += xZDiff / 4;
								
								if (distanceFromCenter <= config.radius - sizeReduction) {
									mutable.setPos(x + pos.getX(), y, z + pos.getZ());
									reader.setBlockState(mutable, Blocks.COBBLESTONE.getDefaultState(), 0);
									changes = true;
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
