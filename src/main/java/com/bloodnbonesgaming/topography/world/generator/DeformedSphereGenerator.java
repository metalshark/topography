package com.bloodnbonesgaming.topography.world.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.bloodnbonesgaming.lib.util.NumberHelper;
import com.bloodnbonesgaming.lib.util.data.BlockPredicate;
import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.noise.OpenSimplexNoiseGeneratorOctaves;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.util.MathUtil;
import com.bloodnbonesgaming.topography.util.noise.RunnableSimplexNoise;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "DeformedSphereGenerator", classExplaination = 
"This file is for the DeformedSphereGenerator. This generator generates spheres of blocks, which are then deformed using simplex noise. "
+ "These spheres generate within grid regions, and can therefore be any size. ")
public class DeformedSphereGenerator implements IGenerator
{
    final IBlockState state;
    final int regionSize;
    final int radius;
    final int count;
    final int minCount;
    double deformScale = 16.0;
    private int minHeight = 0;
    private int maxHeight = 256;
    boolean populate = false;
    private final List<BlockPredicate> requiredBlocks = new ArrayList<BlockPredicate>();
    protected OpenSimplexNoiseGeneratorOctaves terrainNoise = null;
    double[] smallNoiseArray = new double[825];
    double[] largeNoiseArray = new double[65536];
    
    double[] smallDeformNoiseArray = new double[825];
    double[] largeDeformNoiseArray = new double[65536];

    @ScriptMethodDocumentation(args = "ItemBlockData, int, int, int, int", usage = "block to generate, grid region size in chunks, radius, generation attempt count, minimum sphere count"
    		, notes = "This constructs a DeformedSphereGenerator.")
	public DeformedSphereGenerator(final ItemBlockData data, final int regionSize, final int radius, final int count, final int minCount) throws Exception
    {
        this.state = data.buildBlockState();
        this.regionSize = regionSize * 16;
        this.radius = radius;
        this.count = count;
        this.minCount = minCount;
    }
    
    @ScriptMethodDocumentation(args = "ItemBlockData", usage = "required block", notes = "Adds a block the generator is allowed to generate a sphere within. By default can generate within block.")
	public void addRequiredBlock(final ItemBlockData data) throws Exception
    {
        this.requiredBlocks.add(data.buildBlockPredicate());
    }
    
    @ScriptMethodDocumentation(args = "double", usage = "scale", notes = "Sets the noise scale used to deform the spheres. Default is 16.0. The higher the number, the more the sphere can be deformed.")
	public void setDeformScale(final double scale)
    {
        this.deformScale = scale;
    }
    
    @ScriptMethodDocumentation(args = "int, int", usage = "min, max", notes = "Sets the minimum and maximum heights spheres can generate at. Defaults 0-256.")
	public void setHeightRange(final int min, final int max)
	{
		this.minHeight = min;
		this.maxHeight = max;
	}
    
    public void populate()
    {
        this.populate = true;
    }
    
    private void generateNoise(final int arraySizeX, final int arraySizeY, final int arraySizeZ, final int x, final int y, final int z, final int xCoordinateScale, final int yCoordinateScale, final int zCoordinateScale)
    {
//        for (int xI = 0; xI < arraySizeX; xI++)
//        {
//            for (int zI = 0; zI < arraySizeZ; zI++)
//            {
//                for (int yI = 0; yI < arraySizeY; yI++)
//                {
//                    int index = (xI * arraySizeX + zI) * arraySizeY + yI;
//                    
//                    this.smallNoiseArray[index] = this.terrainNoise.eval((x + xI * xCoordinateScale) / 16.0, (y + yI * yCoordinateScale) / 16.0, (z + zI * zCoordinateScale) / 16.0, 3, 0.75);
//                    this.smallDeformNoiseArray[index] = this.terrainNoise.eval((x + xI * xCoordinateScale) / this.deformScale, (y + yI * yCoordinateScale) / this.deformScale, (z + zI * zCoordinateScale) / this.deformScale, 3, 0.75);
//                }
//            }
//        }
        RunnableSimplexNoise.getNoise(smallNoiseArray, terrainNoise, x, z, 16.0, 3, 0.75);
        RunnableSimplexNoise.getNoise(smallDeformNoiseArray, terrainNoise, x, z, this.deformScale, 3, 0.75);
    }
    
    @Override
    public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, final Random random)
    {
        if (populate)
        {
            return;
        }
        final MutableBlockPos mutable = new MutableBlockPos();
        
        if (this.terrainNoise == null) {
            this.terrainNoise = new OpenSimplexNoiseGeneratorOctaves(world.getSeed());
        }
        this.generateNoise(5, 33, 5, chunkX * 16, 0, chunkZ * 16, 4, 8, 4);
        NumberHelper.interpolate(this.smallNoiseArray, this.largeNoiseArray, 5, 33, 5, 4, 8, 4);
        NumberHelper.interpolate(this.smallDeformNoiseArray, this.largeDeformNoiseArray, 5, 33, 5, 4, 8, 4);
        
        final int regionX = (int)Math.floor(chunkX * 16D / this.regionSize);
        final int regionZ = (int)Math.floor(chunkZ * 16D / this.regionSize);
        
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        
        this.generatePositions(regionX, regionZ, random, mutable, positions);
        
        this.generateSpheres(chunkX, chunkZ, mutable, positions, primer);
    }
    
    private void generatePositions(final int regionX, final int regionZ, final Random random, final MutableBlockPos mutable, final List<BlockPos> positions) {
    	int genCount = 0;
        countLoop: for (int i = 0; i < this.count || (genCount < this.minCount && i < this.count * 2); i++)
        {
            final double maxFeatureRadius = this.radius;
            final double midHeight = maxFeatureRadius + random.nextInt((int) (maxHeight - minHeight - (maxFeatureRadius * 2))) + minHeight;

            final int regionCenterX = (int) (regionX * regionSize + regionSize / 2);
            final int regionCenterZ = (int) (regionZ * regionSize + regionSize / 2);

            final int randomSpace = (int) (regionSize - maxFeatureRadius * 2);

            final int featureCenterX = random.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
            final int featureCenterZ = random.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;

//            final BlockPos pos = new BlockPos(featureCenterX, midHeight, featureCenterZ);
            mutable.setPos(featureCenterX, midHeight, featureCenterZ);

            for (final BlockPos position : positions)
            {
                final double minDistance = this.radius + maxFeatureRadius;

                if (this.Distance3D(mutable, position) < (minDistance * minDistance))
                {
                    continue countLoop;
                }
            }
            positions.add(mutable.toImmutable());
            genCount++;
        }
    }
    
    private void generateSpheres(final int chunkX, final int chunkZ, final MutableBlockPos mutable, final List<BlockPos> positions, final ChunkPrimer primer) {
		final int chunkMinX = chunkX * 16;
		final int chunkMinZ = chunkZ * 16;
//		
//		for (int x = 0; x < 16; x++) {
//			for (int z = 0; z < 16; z++) {
//				for (int y = this.minHeight; y < this.maxHeight; y++) {
//					final double noise = this.largeNoiseArray[(x * 16 + z) * 256 + y];
//
//					if (noise > 0.5) {
//						final double deformNoise = this.largeDeformNoiseArray[(x * 16 + z) * 256 + y];
//						mutable.setPos(chunkMinX + x, y, chunkMinZ + z);
//						boolean blockAcceptable = false;
//						
//						for (final BlockPos center : positions) {
//							if (this.canBeInChunk(center, chunkMinX, chunkMinZ)) {
//								if (this.checkDistance(mutable, center, deformNoise, chunkMinX, chunkMinZ)) {
//									if (blockAcceptable || this.isBlockAcceptable(primer, x, y, z)) {
//										blockAcceptable = true;
//										this.setBlockState(primer, x, y, z);
//										break;
//									} else {
//										break;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		
		SphereGenerationRunnable.GenerateSpheres(primer, state, requiredBlocks, largeNoiseArray, largeDeformNoiseArray, chunkMinX, chunkMinZ, positions, radius, 32);
	}
    
    private boolean canBeInChunk(BlockPos center, final int chunkMinX, final int chunkMinZ) {
    	if (center.getX() + this.radius > chunkMinX && center.getX() - this.radius < chunkMinZ + 16) {
    		if (center.getZ() + this.radius > chunkMinZ && center.getZ() - this.radius < chunkMinZ + 16) {
    	    	return true;
    		}
    	}
    	return false;
    }
    
    private boolean checkDistance(MutableBlockPos mutable, BlockPos center, final double deformNoise, final int chunkMinX, final int chunkMinZ) {
    	//if (center.getX() + this.radius > chunkMinX && center.getX() - this.radius < chunkMinZ + 16) {
    		//if (center.getZ() + this.radius > chunkMinZ && center.getZ() - this.radius < chunkMinZ + 16) {
    	    	return this.Distance3D(mutable, center) <= (this.radius * this.radius) * deformNoise;
    		//}
    	//}
    	//return false;
    }
    
    private void setBlockState(final ChunkPrimer primer, final int x, final int y, final int z) {
    	primer.setBlockState(x, y, z, this.state);
    }
    
    private boolean isBlockAcceptable(final ChunkPrimer primer, final int x, final int y, final int z)
    {
        if (this.requiredBlocks.isEmpty())
        {
            return true;
        }
		final IBlockState state = primer.getBlockState(x, y, z);
		
        for (final BlockPredicate predicate : this.requiredBlocks)
        {
            if (predicate.test(state))
            {
                return true;
            }
        }
        return false;
    }
    
    public double Distance3D(final BlockPos pos, final BlockPos pos2)
    {
        double d0 = pos.getX() - pos2.getX();
        double d1 = pos.getY() - pos2.getY();
        double d2 = pos.getZ() - pos2.getZ();
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    @Override
    public void populate(World world, int chunkX, int chunkZ, final Random random)
    {
        if (populate)
        {
        final long seed = world.getSeed();
        
        this.terrainNoise = new OpenSimplexNoiseGeneratorOctaves(seed);
        this.generateNoise(5, 33, 5, chunkX * 16, 0, chunkZ * 16, 4, 8, 4);
        NumberHelper.interpolate(this.smallNoiseArray, this.largeNoiseArray, 5, 33, 5, 4, 8, 4);
        NumberHelper.interpolate(this.smallDeformNoiseArray, this.largeDeformNoiseArray, 5, 33, 5, 4, 8, 4);
        
        final int regionX = (int)Math.floor(chunkX * 16D / this.regionSize);
        final int regionZ = (int)Math.floor(chunkZ * 16D / this.regionSize);
        
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        
        int genCount = 0;
        countLoop: for (int i = 0; i < this.count || genCount < this.minCount; i++)
        {
            final double maxFeatureRadius = this.radius;
            final double midHeight = maxFeatureRadius + random.nextInt((int) (256 - (maxFeatureRadius * 2)));

            final int regionCenterX = (int) (regionX * regionSize + regionSize / 2);
            final int regionCenterZ = (int) (regionZ * regionSize + regionSize / 2);

            final int randomSpace = (int) (regionSize - maxFeatureRadius * 2);

            final int featureCenterX = random.nextInt(randomSpace) - randomSpace / 2 + regionCenterX;
            final int featureCenterZ = random.nextInt(randomSpace) - randomSpace / 2 + regionCenterZ;

            final BlockPos pos = new BlockPos(featureCenterX, midHeight, featureCenterZ);

            for (final BlockPos position : positions)
            {
                final double minDistance = this.radius + maxFeatureRadius;

                if (this.Distance3D(pos, position) < minDistance)
                {
                    continue countLoop;
                }
            }
            positions.add(pos);
            genCount++;
        }
        
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = 0; y < 256; y++)
                {
                    for (final BlockPos center : positions)
                    {
                        final int chunkMinX = chunkX * 16;
                        final int chunkMinZ = chunkZ * 16;
                        final int chunkMaxX = chunkX * 16 + 16;
                        final int chunkMaxZ = chunkZ * 16 + 16;
                        
                        final BlockPos pos = new BlockPos(chunkMinX + x + 2, y, chunkMinZ + z + 2);
                        final double noise = this.largeNoiseArray[(int) ((x * 16 + z) * 256 + y)];
                        final double deformNoise = this.largeNoiseArray[(int) ((x * 16 + z) * 256 + y)] * 0.75 + 0.25;
                        
                                if (noise > 0.5 && this.Distance3D(pos, center) <= this.radius * deformNoise)
                                {
                                    
                                    
                                    if (world.getBlockState(pos) == Blocks.NETHERRACK.getDefaultState())
                                    {
                                        world.setBlockState(pos, this.state);
//                                        world.immediateBlockTick(pos, this.state, this.rand);
                                    }
                                }
                    }
                }
            }
        }
        }
    }

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }
    
    @Override
    public int getRegionSize() {
    	return this.regionSize;
    }
    
    public static class SphereGenerationRunnable implements Runnable {
    	
    	private final Collection<BlockPos> toPlace;
    	private final ChunkPrimer primer;
        private final double[] largeNoiseArray;
        private final double[] largeDeformNoiseArray;
        private final int chunkMinX;
        private final int chunkMinZ;
        private final List<BlockPos> positions;
        private final int radius;
        private final int yStart;
        private final int layerSize;
        private final List<BlockPredicate> requiredBlocks;
        
        public static void GenerateSpheres(final ChunkPrimer primer, final IBlockState state, final List<BlockPredicate> requiredBlocks, final double[] largeNoiseArray, final double[] largeDeformNoiseArray, final int chunkMinX, final int chunkMinZ, final List<BlockPos> positions, final int radius, final int layerSize) {
        	Collection<BlockPos> toPlace = Collections.synchronizedCollection(new ArrayList<BlockPos>());
    		final List<Callable<Object>> callables = new ArrayList<Callable<Object>>();
        	
        	for (int yStart = 0; yStart < 256; yStart += layerSize) {
        		callables.add(Executors.callable(new SphereGenerationRunnable(toPlace, primer, requiredBlocks, largeNoiseArray, largeDeformNoiseArray, yStart, layerSize, chunkMinX, chunkMinZ, positions, radius)));
        	}
        	
        	try {
    			ConfigurationManager.getInstance().getExecutor().invokeAll(callables);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
        	
        	//Must loop through in synchronized block
        	synchronized(toPlace) {
        		for (BlockPos pos : toPlace) {
        			primer.setBlockState(pos.getX(), pos.getY(), pos.getZ(), state);
        		}
        	}
        }

    	private SphereGenerationRunnable(final Collection<BlockPos> toPlace, final ChunkPrimer primer, final List<BlockPredicate> requiredBlocks, final double[] largeNoiseArray, final double[] largeDeformNoiseArray, final int yStart, final int layerSize, final int chunkMinX, final int chunkMinZ, final List<BlockPos> positions, final int radius) {
    		this.largeNoiseArray = largeNoiseArray;
    		this.largeDeformNoiseArray = largeDeformNoiseArray;
    		this.chunkMinX = chunkMinX;
    		this.chunkMinZ = chunkMinZ;
    		this.positions = positions;
    		this.radius = radius;
    		this.yStart = yStart;
    		this.layerSize = layerSize;
    		this.primer = primer;
    		this.requiredBlocks = requiredBlocks;
    		this.toPlace = toPlace;
    	}
    	
		@Override
		public void run() {
			MutableBlockPos mutable = new MutableBlockPos();
			
			for (int y = yStart; y < 256 && y < yStart + layerSize; y++) {
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						final double noise = this.largeNoiseArray[(x * 16 + z) * 256 + y];

						if (noise > 0.5) {
							final double deformNoise = this.largeDeformNoiseArray[(x * 16 + z) * 256 + y];
							mutable.setPos(chunkMinX + x, y, chunkMinZ + z);
							boolean blockAcceptable = false;
							
							for (final BlockPos center : positions) {
								if (this.canBeInChunk(center, chunkMinX, chunkMinZ)) {
									if (this.checkDistance(mutable, center, deformNoise, chunkMinX, chunkMinZ)) {
										if (blockAcceptable || this.isBlockAcceptable(primer, x, y, z)) {
											blockAcceptable = true;
											//this.setBlockState(primer, x, y, z);
											this.toPlace.add(new BlockPos(x, y, z));
											break;
										} else {
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	    
//	    private boolean canBeInChunk(BlockPos center, final int chunkMinX, final int chunkMinZ) {
//	    	if (center.getX() + this.radius > chunkMinX && center.getX() - this.radius < chunkMinZ + 16) {
//	    		if (center.getZ() + this.radius > chunkMinZ && center.getZ() - this.radius < chunkMinZ + 16) {
//	    	    	return true;
//	    		}
//	    	}
//	    	return false;
//	    }
		
		private boolean canBeInChunk(BlockPos center, final int chunkMinX, final int chunkMinZ) {
//			final MutableBlockPos pos = new MutableBlockPos();
//			double radiusSq = radius * radius;
			
//	    	if (MathUtil.getDistanceSq(pos.setPos(chunkMinX, 0, chunkMinZ), center) < radiusSq || MathUtil.getDistanceSq(pos.setPos(chunkMinX + 16, 0, chunkMinZ), center) < radiusSq || MathUtil.getDistanceSq(pos.setPos(chunkMinX, 0, chunkMinZ + 16), center) < radiusSq || MathUtil.getDistanceSq(pos.setPos(chunkMinX + 16, 0, chunkMinZ + 16), center) < radiusSq) {
//	        	return true;
//	    	}
	    	if (center.getX() - radius > chunkMinX + 16) {//If outside chunk in +X direction, and radius isn't large enough to get into chunk
	    		return false;
	    	}
	    	if (center.getX() + radius < chunkMinX) {//If outside chunk in -X direction, and radius isn't large enough to get into chunk
	    		return false;
	    	}
	    	if (center.getZ() - radius > chunkMinZ + 16) {//If outside chunk in +Z direction, and radius isn't large enough to get into chunk
	    		return false;
	    	}
	    	if (center.getZ() + radius < chunkMinZ) {//If outside chunk in -Z direction, and radius isn't large enough to get into chunk
	    		return false;
	    	}
	    	return true;
	    }
	    
	    private boolean checkDistance(MutableBlockPos mutable, BlockPos center, final double deformNoise, final int chunkMinX, final int chunkMinZ) {
	    	//if (center.getX() + this.radius > chunkMinX && center.getX() - this.radius < chunkMinZ + 16) {
	    		//if (center.getZ() + this.radius > chunkMinZ && center.getZ() - this.radius < chunkMinZ + 16) {
	    	    	return this.Distance3D(mutable, center) <= (this.radius * this.radius) * deformNoise;
	    		//}
	    	//}
	    	//return false;
	    }
	    
	    public double Distance3D(final BlockPos pos, final BlockPos pos2)
	    {
	        double d0 = pos.getX() - pos2.getX();
	        double d1 = pos.getY() - pos2.getY();
	        double d2 = pos.getZ() - pos2.getZ();
	        return d0 * d0 + d1 * d1 + d2 * d2;
	    }
	    
	    private boolean isBlockAcceptable(final ChunkPrimer primer, final int x, final int y, final int z)
	    {
	        if (this.requiredBlocks.isEmpty())
	        {
	            return true;
	        }
			final IBlockState state = primer.getBlockState(x, y, z);
			
	        for (final BlockPredicate predicate : this.requiredBlocks)
	        {
	            if (predicate.test(state))
	            {
	                return true;
	            }
	        }
	        return false;
	    }
    }
}
