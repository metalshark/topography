package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.block.BlockFalling;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VanillaDecorationGenerator", classExplaination = 
"This file is for the VanillaDecorationGenerator. This generates vanilla decorations. This includes things like trees and ores.")
public class VanillaDecorationGenerator implements IGenerator {
	
	@ScriptMethodDocumentation(usage = "", notes = "This constructs a VanillaDecorationGenerator.")
	public VanillaDecorationGenerator() {}
	
	@Override
	public void populate(World world, int chunkX, int chunkZ, Random random) {

		BlockFalling.fallInstantly = true;
		
        BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        
        world.getBiome(blockpos.add(16, 0, 16)).decorate(world, random, blockpos);

        BlockFalling.fallInstantly = false;
	}
}
