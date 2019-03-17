package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.block.BlockFalling;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VanillaAnimalGenerator", classExplaination = 
"This file is for the VanillaAnimalGenerator. This generates animals.")
public class VanillaAnimalGenerator implements IGenerator {
	
	@ScriptMethodDocumentation(usage = "", notes = "This constructs a VanillaAnimalGenerator.")
	public VanillaAnimalGenerator()
	{
		
	}
	
	@Override
	public void populate(World world, int chunkX, int chunkZ, Random random) {
		
		BlockFalling.fallInstantly = true;
		
        int i = chunkX * 16;
        int j = chunkZ * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        
        WorldEntitySpawner.performWorldGenSpawning(world, world.getBiome(blockpos.add(16, 0, 16)), i + 8, j + 8, 16, 16, random);

        BlockFalling.fallInstantly = false;
	}
}
