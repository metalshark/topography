package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerBiomeEdgeDC", classExplaination = 
"A configurable version of the vanilla GenLayerBiomeEdge. Replaces biomes with objectionable neighbors with biomes that make more sense.")
public class GenLayerBiomeEdgeDC extends GenLayer
{
	private List<BiomeEdgeData> edgeDataList = new ArrayList<BiomeEdgeData>();
	
	@ScriptMethodDocumentation(args = "long, GenLayer", usage = "base layer seed, parent layer", notes = "Constructs the layer with its seed and parent layer.")
	public GenLayerBiomeEdgeDC(long seed, final GenLayer parent)
	{
		super(seed);
		
		this.parent = parent;
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome IDs, edge biome ID", notes = "Adds a biome edge for the provided biomes with no whitelist or blacklist.")
	public void addBiomeEdge(final int[] biomes, final int edge)
	{
		this.addBiomeEdge(biomes, (int[])null, (int[])null, edge);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID_ARRAY, ArgType.BIOME_ID_ARRAY, ArgType.BIOME_ID_ARRAY, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome IDs, whitelist, blacklist, edge biome ID", notes = "Adds a biome edge for the provided biomes with a whitelist/blacklist.")
	public void addBiomeEdge(final int[] biomes, final int[] whitelist, final int[] blacklist, final int edge)
	{
		final BiomeEdgeData edgeData = new BiomeEdgeData();
		edgeData.setBiomes(biomes);
		edgeData.setWhitelist(whitelist);
		edgeData.setBlacklist(blacklist);
		edgeData.setEdge(edge);
		this.addBiomeEdge(edgeData);
	}
	
	public void addBiomeEdge(final BiomeEdgeData data)
	{
		this.edgeDataList.add(data);
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
		int[] parentInts = this.parent.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
        int[] returnInts = IntCache.getIntCache(width * depth);

        for (int z = 0; z < depth; ++z)
        {
            for (int x = 0; x < width; ++x)
            {
                this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
                final int primary = parentInts[x + 1 + (z + 1) * (width + 2)];
                boolean foundEdge = false;
                
                /*
                 * If blacklisted, assume whitelist is for all others
                 * If whitelisted, assume blacklist is for all others
                 * 
                 * 
                 * 
                 * If not whitelisted, blacklist
                 * If not blacklisted, whitelist
                 * 
                 * 
                 * If nothing, whitelist
                */
                

                //Check if any edges were set
				if (this.edgeDataList != null)
				{
					//Loop through possible edges
					for (final BiomeEdgeData data : this.edgeDataList)
					{
						//Check if biome has possibility for edging
						if (data.usedForBiome(primary))
						{
							//Check biome in each direction for if there should be an edge
							final int biome1 = parentInts[x + 1 + (z + 1 - 1) * (width + 2)];
							final int biome2 = parentInts[x + 1 + 1 + (z + 1) * (width + 2)];
							final int biome3 = parentInts[x + 1 - 1 + (z + 1) * (width + 2)];
							final int biome4 = parentInts[x + 1 + (z + 1 + 1) * (width + 2)];
							//Check if any surrounding biome is not in the whitelist, and in the blacklist
							if (data.useEdge(biome1))
							{
								//Set edge biome, set foundEdge so this isn't replaced and break from loop
								returnInts[x + z * width] = data.getEdge();
								foundEdge = true;
								break;
							}
							else if (data.useEdge(biome2))
							{
								//Set edge biome, set foundEdge so this isn't replaced and break from loop
								returnInts[x + z * width] = data.getEdge();
								foundEdge = true;
								break;
							}
							else if (data.useEdge(biome3))
							{
								//Set edge biome, set foundEdge so this isn't replaced and break from loop
								returnInts[x + z * width] = data.getEdge();
								foundEdge = true;
								break;
							}
							else if (data.useEdge(biome4))
							{
								//Set edge biome, set foundEdge so this isn't replaced and break from loop
								returnInts[x + z * width] = data.getEdge();
								foundEdge = true;
								break;
							}
						}
					}
				}
				//Set biome as primary if edge wasnt found
				if (!foundEdge)
				{
					returnInts[x + z * width] = primary;
				}
            }
        }
		return returnInts;
	}
}