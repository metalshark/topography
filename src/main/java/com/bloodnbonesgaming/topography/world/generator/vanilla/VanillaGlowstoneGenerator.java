package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.layer.GenLayer;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VanillaGlowstoneGenerator", classExplaination = 
"This file is for the VanillaGlowstoneGenerator. This is the vanilla glowstone generator. Generates glowstone clusters under netherrack.")
public class VanillaGlowstoneGenerator implements IGenerator
{
    private final WorldGenGlowStone1 lightGemGen = new WorldGenGlowStone1();

    @ScriptMethodDocumentation(usage = "", notes = "This constructs a VanillaGlowstoneGenerator.")
	public VanillaGlowstoneGenerator() {}

    @Override
    public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random)
    {

    }

    @Override
    public void populate(World world, int chunkX, int chunkZ, Random rand)
    {
        BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        
        for (int j1 = 0; j1 < 20; ++j1)
        {
            this.lightGemGen.generate(world, rand, blockpos.add(rand.nextInt(16) + 8, rand.nextInt(245) + 4, rand.nextInt(16) + 8));
        }
    }

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }

}
