package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

//@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerCombineWhitelist", classExplaination = 
//"A layer used to combine two parent layers using a whitelist. If the biome from the first parent is in the whitelist, it's used, otherwise the biome from the second parent is used."
//+ " This is useful for doing things like combining a heat map and a terrain map.")
public class GenLayerCombine extends GenLayer{
    
    private final GenLayer parent2;
    private List<int[]> combinations = new ArrayList<int[]>();
    
    @ScriptArgs(args = {ArgType.LONG, ArgType.GENLAYER, ArgType.GENLAYER, ArgType.NON_NULL_BIOME_ID_ARRAY})
    @ScriptMethodDocumentation(usage = "base layer seed, first parent layer, second parent layer, whitelist", notes = "Constructs the layer with its seed, parent layers and whitelist.")
    public GenLayerCombine(long seed, final GenLayer parent, final GenLayer parent2) {
        super(seed);
        this.parent = parent;
        this.parent2 = parent2;
    }
    
    @ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY})
    public void addCombination(final int[] combination)
    {
        this.combinations.add(combination);
    }

    @Override
    public int[] getInts(int chunkX, int chunkZ, int width, int depth)
    {
        int[] parentInts = this.parent.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
        int[] parentInts2 = this.parent2.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
        int[] returnInts = IntCache.getIntCache(width * depth);

        for (int z = 0; z < depth; ++z)
        {
            for (int x = 0; x < width; ++x)
            {
                this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
                
                final int parentIndex = x + 1 + (z + 1) * (width + 2);
                final int currentBiome = parentInts[parentIndex];
                final int currentBiome2 = parentInts2[parentIndex];
                boolean combined = false;
                
                for (final int[] combination : this.combinations)
                {
                    if (combination[0] == currentBiome && combination[1] == currentBiome2)
                    {
                        returnInts[x + z * width] = combination[2];
                        combined = true;
                        break;
                    }
                }
                if (!combined)
                {
                    returnInts[x + z * width] = currentBiome;
                }
            }
        }
        return returnInts;
    }
}