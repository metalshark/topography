package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDungeons;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VanillaDungeonGenerator", classExplaination = 
"This file is for the VanillaDungeonGenerator. This generates vanilla dungeons.")
public class VanillaDungeonGenerator extends WorldGenDungeons  implements IGenerator{
	
	private int attempts = 8;
	
	@ScriptMethodDocumentation(usage = "", notes = "This constructs a VanillaDungeonGenerator.")
	public VanillaDungeonGenerator() {}
	
	@ScriptMethodDocumentation(args = "int", usage = "attempts", notes = "Sets the number of attempts the generator should make each chunk to generate a dungeon. Default/vanilla is 8.")
	public void setSpawnAttempts(final int attempts)
	{
		this.attempts = attempts;
	}

	@Override
	public void populate(World world, int chunkX, int chunkZ, Random random) {

		for (int i = 0; i < this.attempts; ++i)
        {
            int x = random.nextInt(16) + 8;
            int y = random.nextInt(256);
            int z = random.nextInt(16) + 8;
            this.generate(world, random, new BlockPos(chunkX * 16 + x, y, chunkZ * 16 + z));
        }
	}
}
