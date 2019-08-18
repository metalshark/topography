package com.bloodnbonesgaming.topography.world.generator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.data.BlockPredicate;
import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.layer.GenLayer;

@ScriptClassDocumentation(documentationFile = ModInfo.GENERATOR_DOCUMENTATION_FOLDER + "LayerGenerator", classExplaination = 
"This file is for the LayerGenerator. This generator can be created in a dimension file with 'new LayerGenerator()'.")
public class LayerGenerator implements IGenerator
{

    @Override
    public void generate(final World world, ChunkPrimer primer, int chunkX, int chunkZ, Random random)
    {
        for (int y = 0; y < 256; y++)
        {
            for (final Entry<MinMaxBounds, Map<IBlockState, BlockPredicate>> entry : layers.entrySet())
            {
            	for (final Entry<IBlockState, BlockPredicate> inner : entry.getValue().entrySet())
            	{
                    if (entry.getKey().test(y))
                    {
                        for (int x = 0; x < 16; x++)
                        {
                            for (int z = 0; z < 16; z++)
                            {
                            	BlockPredicate predicate = inner.getValue();
                            	
                            	if (predicate == null || predicate == BlockPredicate.ANY || predicate.test(primer.getBlockState(x, y, z)))
                            	{
                                    primer.setBlockState(x, y, z, inner.getKey());
                            	}
                            }
                        }
                    }
            	}
            }
        }
    }

    
    private final Map<MinMaxBounds, Map<IBlockState, BlockPredicate>> layers = new LinkedHashMap<MinMaxBounds, Map<IBlockState, BlockPredicate>>();
    
    @ScriptMethodDocumentation(args = "MinMaxBounds, ItemBlockData", usage = "y axis bounds, block to place", notes = "Adds a layer of blocks to be generated within the provided bounds, made of the provided block.")
	public void addLayer(final MinMaxBounds bounds, final ItemBlockData block) throws Exception
    {
    	final Map<IBlockState, BlockPredicate> whitelist = new LinkedHashMap<IBlockState, BlockPredicate>();
    	whitelist.put(block.buildBlockState(), null);
        this.layers.put(bounds, whitelist);
    }
    
    @ScriptMethodDocumentation(args = "MinMaxBounds, ItemBlockData, ItemBlockData", usage = "y axis bounds, block to place, block to replace", notes = "Adds a layer of blocks to be generated within the provided bounds, made of the provided block, replacing only the provided block.")
	public void addLayer(final MinMaxBounds bounds, final ItemBlockData block, final ItemBlockData toReplace) throws Exception
    {
    	final Map<IBlockState, BlockPredicate> whitelist = new LinkedHashMap<IBlockState, BlockPredicate>();
    	whitelist.put(block.buildBlockState(), toReplace.buildBlockPredicate());
        this.layers.put(bounds, whitelist);
    }

    @Override
    public void populate(World world, int chunkX, int chunkZ, Random rand)
    {        
    }

    @Override
    public GenLayer getLayer(World world, GenLayer parent)
    {
        return null;
    }
}
