package com.bloodnbonesgaming.topography.world.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.world.SkyIslandDataHandler;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;

public class DecoratorScattered
{
    private final IBlockState decoration;
    private final Random rand = new Random();
    
    public DecoratorScattered(final ItemBlockData data) throws Exception
    {
        this.decoration = data.buildBlockState();
    }
    
    public void generateForSkyIsland(final int count, final long seed, final int chunkX, final int chunkZ, final ChunkPrimer primer, final BlockPos center, final SkyIslandData data, final SkyIslandType type, final SkyIslandGenerator generator, final int regionSize)
    {
        final double radius = data.getHorizontalRadius();
        final int regionStartX = (int) (Math.floor(chunkX * 16D / regionSize) * regionSize);
        final int regionStartZ = (int) (Math.floor(chunkZ * 16D / regionSize) * regionSize);
        
        this.rand.setSeed((long) Math.floor(chunkX * 16D / regionSize) * 341873128712L
                + (long) Math.floor(chunkZ * 16D / regionSize) * 132897987541L + seed
                + center.getX() * 25565L + center.getZ() * 35754L);
        
        final List<BlockPos> posList = new ArrayList<BlockPos>();
        
        for (int i = 0; i < count; i++)
        {
            final int xPos = this.rand.nextInt(regionSize);
            final int zPos = this.rand.nextInt(regionSize);
            
            final BlockPos pos = new BlockPos(xPos + regionStartX, 0, zPos + regionStartZ);
            
            final int chunkMinX = chunkX * 16;
            final int chunkMinZ = chunkZ * 16;
            final int chunkMaxX = chunkX * 16 + 16;
            final int chunkMaxZ = chunkZ * 16 + 16;
            
//            Topography.instance.getLog().info(pos + " " + center + " " + new BlockPos(chunkMinX, 0, chunkMinZ) + " " + new BlockPos(chunkMaxX, 0, chunkMaxZ));
            if (pos.getX() >= chunkMinX && pos.getX() < chunkMaxX)
            {
                if (pos.getZ() >= chunkMinZ && pos.getZ() < chunkMaxZ)
                {
                    if (SkyIslandDataHandler.getDistance(pos, center) <= radius && !posList.contains(pos))
                    {
                        final int inChunkX = pos.getX() - chunkMinX;
                        final int inChunkZ = pos.getZ() - chunkMinZ;
                        posList.add(pos);
                        int y = this.getTopSolidBlock(primer, inChunkX, inChunkZ);
                        
                        if (y >= center.getY())
//                        if (y > 0)
                        {
                            primer.setBlockState(inChunkX, y + 1, inChunkZ, decoration);
                        }
                    }
                }
            }
        }
    }
    
    private final int getTopSolidBlock(final ChunkPrimer primer, final int x, final int z)
    {
        for (int y = 255; y > 0; y--)
        {
            final IBlockState state = primer.getBlockState(x, y, z);
            
            if (state.isTopSolid())
            {
                return y;
            }
        }
        
        return 0;
    }
}
