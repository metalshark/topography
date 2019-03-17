package com.bloodnbonesgaming.topography.world.generator;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "IceAndSnowGenerator", classExplaination = 
"This file is for the IceAndSnowGenerator. This generator generates snow and freezes ice in cold areas.")
public class IceAndSnowGenerator implements IGenerator {
	
	
	@ScriptMethodDocumentation(usage = "", notes = "This constructs a IceAndSnowGenerator.")
	public IceAndSnowGenerator()
	{
		
	}
	
	@Override
	public void populate(World world, int chunkX, int chunkZ, Random random) {

		BlockPos blockpos = new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8);
        
		for (int x = 0; x < 16; ++x)
		{
            for (int z = 0; z < 16; ++z)
            {
                BlockPos blockpos1 = world.getPrecipitationHeight(blockpos.add(x, 0, z));
                BlockPos blockpos2 = blockpos1.down();

                if (world.canBlockFreezeWater(blockpos2))
                {
                    world.setBlockState(blockpos2, Blocks.ICE.getDefaultState(), 2);
                }

                if (world.canSnowAt(blockpos1, true))
                {
                    world.setBlockState(blockpos1, Blocks.SNOW_LAYER.getDefaultState(), 2);
                }
            }
        }
	}
}
