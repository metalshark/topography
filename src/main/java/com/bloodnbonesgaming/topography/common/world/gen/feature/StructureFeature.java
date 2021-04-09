package com.bloodnbonesgaming.topography.common.world.gen.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.RegionFeatureConfig;
import com.bloodnbonesgaming.topography.common.world.gen.feature.config.StructureFeatureConfig;
import com.mojang.serialization.Codec;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.IntegrityProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class StructureFeature extends RegionFeature<StructureFeatureConfig> {
	
	public static final StructureFeature INSTANCE = new StructureFeature(StructureFeatureConfig.CODEC);

	public StructureFeature(Codec<StructureFeatureConfig> codec) {
		super(codec);
		this.setRegistryName(ModInfo.MODID, "structure");
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, StructureFeatureConfig config) {
		//this.currentRegionX = ((int) Math.floor(Math.floor(x / 16.0D) * 16D / this.regionSize));
        //this.currentRegionZ = ((int) Math.floor(Math.floor(z / 16.0D) * 16D / this.regionSize));
		//this.islandPositionRandom.setSeed((long) (this.currentRegionX) * 341873128712L + (long) (this.currentRegionZ) * 132897987541L + worldSeed);
		int chunkX = pos.getX() / 16 * 16;
		int chunkZ = pos.getZ() / 16 * 16;
		int regionX = ((int) Math.floor(Math.floor(chunkX / 16.0D) * 16D / config.regionSize));
		int regionZ = ((int) Math.floor(Math.floor(chunkZ / 16.0D) * 16D / config.regionSize));
		config.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()));
		int maxHeight = 80;
		List<BlockPos> positions = generatePositions(config, regionX, regionZ);
		boolean changes = false;
		Mutable mutable = new BlockPos.Mutable();
		int posIndex = -1;
		
		for (BlockPos position : positions) {
			posIndex++;
            BlockPos templateSize = config.getTemplate().getSize();
			int size = templateSize.getX() > templateSize.getZ() ? templateSize.getX() : templateSize.getZ();
			
			if (canBeInChunk(position, size, chunkX, chunkZ)) {
				//Increment seed for unique rand per feature
				config.regionPositionRand.setSeed(this.getRegionSeed(config, regionX, regionZ, reader.getSeed()) + posIndex);
				
				//Spawn structure
				Rotation rotation = Rotation.randomRotation(rand);
			    Template template = config.getTemplate();
			    ChunkPos chunkpos = new ChunkPos(pos);
			    MutableBoundingBox mutableboundingbox = new MutableBoundingBox(chunkpos.getXStart(), 0, chunkpos.getZStart(), chunkpos.getXEnd(), 256, chunkpos.getZEnd());
			    PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(mutableboundingbox).setRandom(rand).addProcessor(BlockIgnoreStructureProcessor.AIR_AND_STRUCTURE_BLOCK);
			    BlockPos blockpos = template.transformedSize(rotation);
			    int j = rand.nextInt(16 - blockpos.getX());
			    int k = rand.nextInt(16 - blockpos.getZ());
			    int l = 256;

//			    for(int i1 = 0; i1 < blockpos.getX(); ++i1) {
//			       for(int j1 = 0; j1 < blockpos.getZ(); ++j1) {
//			          l = Math.min(l, reader.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + i1 + j, pos.getZ() + j1 + k));
//			       }
//			    }
//
//			    int k1 = Math.max(l - 15 - rand.nextInt(10), 10);
			    BlockPos blockpos1 = template.getZeroPositionWithTransform(position.add(0, 81, 0), Mirror.NONE, rotation);
//			    IntegrityProcessor integrityprocessor = new IntegrityProcessor(0.9F);
//			    placementsettings.clearProcessors().addProcessor(integrityprocessor);
			    template.func_237146_a_(reader, blockpos1, blockpos1, placementsettings, rand, 4);
			}
		}
		return changes;
	}
	
	protected List<BlockPos> generatePositions(StructureFeatureConfig config, int regionX, int regionZ) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		
        for (int i = 0; i < config.positionAttemptCount; i++)
        {
            final int regionCenterX = (int) ((regionX) * config.regionSize + config.regionSize / 2);
            final int regionCenterZ = (int) ((regionZ) * config.regionSize + config.regionSize / 2);

            BlockPos templateSize = config.getTemplate().getSize();
            final int randomSpace = (int) (config.regionSize - templateSize.getX() > templateSize.getZ() ? templateSize.getX() : templateSize.getZ());

            final int featureCenterX = config.regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
            final int featureCenterZ = config.regionPositionRand.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;

            final BlockPos pos = new BlockPos(featureCenterX, 0, featureCenterZ);
            
            if (this.isPostionAcceptable(positions, pos, config.minSpacing)) {
            	positions.add(pos);
            }
        }
		return positions;
	}
    
	protected boolean isPostionAcceptable(List<BlockPos> positions, BlockPos pos, int minSpacing) {		
    	for (BlockPos position : positions) {
    		if (getHorizontalDistance(pos, position) < minSpacing) {
    			return false;
    		}
    	}
    	return true;
    }
	
    protected boolean canBeInChunk(BlockPos center, int size, final int chunkMinX, final int chunkMinZ) {
    	if (center.getX() - size > chunkMinX + 16) {//If outside chunk in +X direction, and size isn't large enough to get into chunk
    		return false;
    	}
    	if (center.getX() + size < chunkMinX) {//If outside chunk in -X direction, and size isn't large enough to get into chunk
    		return false;
    	}
    	if (center.getZ() - size > chunkMinZ + 16) {//If outside chunk in +Z direction, and size isn't large enough to get into chunk
    		return false;
    	}
    	if (center.getZ() + size < chunkMinZ) {//If outside chunk in -Z direction, and size isn't large enough to get into chunk
    		return false;
    	}
    	return true;
    }
}
