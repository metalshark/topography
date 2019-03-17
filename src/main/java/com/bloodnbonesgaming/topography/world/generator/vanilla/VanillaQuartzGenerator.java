package com.bloodnbonesgaming.topography.world.generator.vanilla;

import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;

import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.layer.GenLayer;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "VanillaQuartzGenerator", classExplaination = 
"This file is for the VanillaQuartzGenerator. This is the vanilla nether quartz generator. Generates nether quartz clusters in netherrack.")
public class VanillaQuartzGenerator implements IGenerator
{
    private final WorldGenerator quartzGen = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 14, BlockMatcher.forBlock(Blocks.NETHERRACK));

    @ScriptMethodDocumentation(usage = "", notes = "This constructs a VanillaQuartzGenerator.")
	public VanillaQuartzGenerator() {}

    @Override
    public void generate(World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random)
    {

    }

    @Override
    public void populate(World world, int chunkX, int chunkZ, Random rand)
    {
        BlockPos blockpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        
        for (int l1 = 0; l1 < 32; ++l1)
        {
            this.quartzGen.generate(world, rand, blockpos.add(rand.nextInt(16), rand.nextInt(240) + 10, rand.nextInt(16)));
        }
    }

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }

}
