package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenRavine;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VanillaRavineGenerator", classExplaination = 
"This file is for the VanillaRavineGenerator. Generates vanilla ravines.")
public class VanillaRavineGenerator extends MapGenRavine implements IGenerator {

    @ScriptMethodDocumentation(usage = "", notes = "This constructs a VanillaRavineGenerator.")
	public VanillaRavineGenerator() {}
	
	@Override
	public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random) {

		this.generate(world, chunkX, chunkZ, primer);
	}
}
