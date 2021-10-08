package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureConfig;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.StructureFeatureConfig;
import com.mojang.serialization.Codec;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

public class StructureFeature extends RegionFeature<StructureFeatureConfig> {
	
	public static final StructureFeature INSTANCE = new StructureFeature(StructureFeatureConfig.CODEC);

	public StructureFeature(Codec<StructureFeatureConfig> codec) {
		super(codec);
		this.setRegistryName(ModInfo.MODID, "structure");
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, StructureFeatureConfig config) {
		int chunkX = pos.getX() / 16 * 16;
		int chunkZ = pos.getZ() / 16 * 16;
		int regionX = ((int) Math.floor(Math.floor(chunkX / 16.0D) * 16D / config.regionSize));
		int regionZ = ((int) Math.floor(Math.floor(chunkZ / 16.0D) * 16D / config.regionSize));
		config.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()));
		Map<BlockPos, RegionFeatureConfig> allPositions = new LinkedHashMap<BlockPos, RegionFeatureConfig>();
		List<BlockPos> positions = config.generatePositions(allPositions, regionX, regionZ);
		return generate(reader, rand, pos, config, positions);
	}
	
//	protected List<BlockPos> generatePositions(StructureFeatureConfig config, int regionX, int regionZ) {
//		List<BlockPos> positions = new ArrayList<BlockPos>();
//		
//        for (int i = 0; i < config.positionAttemptCount; i++) {
////            final int regionCenterX = (int) ((regionX) * config.regionSize + config.regionSize / 2);
////            final int regionCenterZ = (int) ((regionZ) * config.regionSize + config.regionSize / 2);
//            final int regionMinX = regionX * config.regionSize;
//            final int regionMinZ = regionZ * config.regionSize;
//
//            BlockPos templateSize = config.getTemplate().getSize();
//            int xSize = templateSize.getX();
//            int zSize = templateSize.getZ();
//            
//            final int randomXSpace = config.regionSize - xSize;
//            final int randomZSpace = config.regionSize - zSize;
//            
//            final int featureX = regionMinX + config.regionPositionRand.nextInt(randomXSpace);
//            final int featureZ = regionMinZ + config.regionPositionRand.nextInt(randomZSpace);
//            
//            
//            
////            final int randomSpace = (int) (config.regionSize - templateSize.getX() > templateSize.getZ() ? templateSize.getX() : templateSize.getZ());
////
////            final int featureCenterX = config.regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
////            final int featureCenterZ = config.regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;
//
//            final BlockPos pos = new BlockPos(featureX, 0, featureZ);
//            
//            if (this.isPostionAcceptable(positions, pos, config.minSpacing)) {
//            	positions.add(pos);
//            }
//        }
//		return positions;
//	}
    
//	protected boolean isPostionAcceptable(List<BlockPos> positions, BlockPos pos, int minSpacing) {		
//    	for (BlockPos position : positions) {
//    		if (getHorizontalDistance(pos, position) < minSpacing) {
//    			return false;
//    		}
//    	}
//    	return true;
//    }
	
    protected boolean canBeInChunk(BlockPos pos, int sizeX, int sizeZ, final int chunkMinX, final int chunkMinZ) {
    	if (pos.getX() + sizeX < chunkMinX) {//square max X less than chunk min X
    		return false;
    	}
    	if (pos.getX() > chunkMinX + 16) {//square min X greater than chunk max X
    		return false;
    	}
    	if (pos.getZ() + sizeZ < chunkMinZ) {//square max Z less than chunk min Z
    		return false;
    	}
    	if (pos.getZ() > chunkMinZ + 16) {//square min Z greater than chunk max Z
    		return false;
    	}
    	return true;
    }

	@Override
	public boolean generate(ISeedReader reader, Random rand, BlockPos pos, StructureFeatureConfig config, List<BlockPos> positions) {
		int chunkX = pos.getX() / 16 * 16;
		int chunkZ = pos.getZ() / 16 * 16;
		int regionX = ((int) Math.floor(Math.floor(chunkX / 16.0D) * 16D / config.regionSize));
		int regionZ = ((int) Math.floor(Math.floor(chunkZ / 16.0D) * 16D / config.regionSize));
		config.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()));
		boolean changes = false;
		Mutable mutable = new BlockPos.Mutable();
		int posIndex = -1;
		
		for (BlockPos position : positions) {
			posIndex++;
            BlockPos templateSize = config.getTemplate().getSize();
			int size = templateSize.getX() > templateSize.getZ() ? templateSize.getX() : templateSize.getZ();
			
			if (canBeInChunk(position, config.sizeX, config.sizeZ, chunkX, chunkZ)) {
				//Increment seed for unique rand per feature
				config.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()) + posIndex);
				
				//Spawn structure
				Rotation rotation = Rotation.randomRotation(config.regionPositionRand);
			    Template template = config.getTemplate();
			    ChunkPos chunkpos = new ChunkPos(pos);
			    MutableBoundingBox mutableboundingbox = new MutableBoundingBox(chunkpos.getXStart(), 0, chunkpos.getZStart(), chunkpos.getXEnd(), 256, chunkpos.getZEnd());
			    PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(mutableboundingbox).setRandom(rand);
			    for (StructureProcessor processor : config.processors) {
			    	placementsettings = placementsettings.addProcessor(processor);
			    }
			    BlockPos blockpos = template.transformedSize(rotation);
//			    int j = rand.nextInt(16 - blockpos.getX());
//			    int k = rand.nextInt(16 - blockpos.getZ());
//			    int l = 256;

//			    for(int i1 = 0; i1 < blockpos.getX(); ++i1) {
//			       for(int j1 = 0; j1 < blockpos.getZ(); ++j1) {
//			          l = Math.min(l, reader.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + i1 + j, pos.getZ() + j1 + k));
//			       }
//			    }
//
//			    int k1 = Math.max(l - 15 - rand.nextInt(10), 10);
			    BlockPos blockpos1 = template.getZeroPositionWithTransform(position.add(0, config.height, 0), Mirror.NONE, rotation);
//			    IntegrityProcessor integrityprocessor = new IntegrityProcessor(0.9F);
//			    placementsettings.clearProcessors().addProcessor(integrityprocessor);
			    template.func_237146_a_(reader, blockpos1, blockpos1, placementsettings, rand, 0);
			}
		}
		return changes;
	}
}
