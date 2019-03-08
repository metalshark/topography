package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.block.BlockFalling;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VanillaDecorationGenerator implements IGenerator {
	
	@Override
	public void populate(World world, int chunkX, int chunkZ, Random random) {

		BlockFalling.fallInstantly = true;
		
        BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        
        world.getBiome(blockpos.add(16, 0, 16)).decorate(world, random, blockpos);

        BlockFalling.fallInstantly = false;
	}
}
