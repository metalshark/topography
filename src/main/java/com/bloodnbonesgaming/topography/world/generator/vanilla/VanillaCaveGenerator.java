package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VanillaCaveGenerator", classExplaination = 
"This file is for the VanillaCaveGenerator. This generates vanilla caves.")
public class VanillaCaveGenerator extends MapGenCaves implements IGenerator {
	
	@ScriptMethodDocumentation(usage = "", notes = "This constructs a VanillaCaveGenerator.")
	public VanillaCaveGenerator() {}
	
	@Override
	public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random) {
		
		this.generate(world, chunkX, chunkZ, primer);
	}
}
