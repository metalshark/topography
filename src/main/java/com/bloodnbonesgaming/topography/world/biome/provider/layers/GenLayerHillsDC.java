package com.bloodnbonesgaming.topography.world.biome.provider.layers;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

@ScriptClassDocumentation(documentationFile = ModInfo.GENLAYER_DOCUMENTATION_FOLDER + "GenLayerHillsDC", classExplaination = 
"A configurable version of the vanilla GenLayerHills. Pseudo-randomly adds hill and mutated biomes to existing biomes.")
public class GenLayerHillsDC extends GenLayer
{
	private final GenLayer secondParent;
	//private final Map<Integer, HillBiomesData> hillMap;
	private HillData hillData;
	
	@ScriptMethodDocumentation(args = "long, GenLayer, GenLayer", usage = "layer base seed, first parent, second parent", notes = "Constructs the layer with its seed and parent layers."
			+ " The first layer is the normal biome map, and the second is the hill/river initialization layer.")
	public GenLayerHillsDC(long seed, final GenLayer parent, final GenLayer secondParent)
	{
		super(seed);
		this.parent = parent;
		this.secondParent = secondParent;
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID, ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome ID, mutation biome ID", notes = "Sets the mutation biome for the provided biomeID.")
	public void setMutation(final int biome, final int mutation)
	{
		if (this.hillData == null)
		{
			this.hillData = new HillData();
		}
		this.hillData.setMutation(biome, mutation);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID, ArgType.NON_NULL_BIOME_ID_ARRAY})
	@ScriptMethodDocumentation(usage = "biome ID, hill biome ID array", notes = "Sets the hill biomes for the provided biomeID.")
	public void setHill(final int biome, final int[] hills)
	{
		if (this.hillData == null)
		{
			this.hillData = new HillData();
		}
		this.hillData.setHill(biome, hills);
	}
	
	@ScriptMethodDocumentation(usage = "", notes = "Adds all of the hill biomes that would be added by the default GenLayerHills layer.")
	public void addDefaultHills()
	{
		if (this.hillData == null)
		{
			this.hillData = new HillData();
		}
		this.hillData.addDefaultHills();
	}
	
	@ScriptMethodDocumentation(usage = "", notes = "Adds all of the mutation biomes that would be added by the default GenLayerHills layer.")
	public void addDefaultMutations()
	{
		if (this.hillData == null)
		{
			this.hillData = new HillData();
		}
		this.hillData.addDefaultMutations();
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "biome ID", notes = "Removes the hill biomes for the provided biome.")
	public void removeHill(final int biome)
	{
		if (this.hillData == null)
		{
			this.hillData = new HillData();
		}
		this.hillData.removeHill(biome);
	}
	
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
	@ScriptMethodDocumentation(usage = "", notes = "Removes the mutation biome for the provided biome.")
	public void removeMutation(final int biome)
	{
		if (this.hillData == null)
		{
			this.hillData = new HillData();
		}
		this.hillData.removeMutation(biome);
	}

	@Override
	public int[] getInts(int chunkX, int chunkZ, int width, int depth)
	{
		int[] parentInts = this.parent.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
        int[] secondParentInts = this.secondParent.getInts(chunkX - 1, chunkZ - 1, width + 2, depth + 2);
        int[] returnInts = IntCache.getIntCache(width * depth);
        final boolean nullData = this.hillData == null;
        
        for (int z = 0; z < depth; z++)
        {
            for (int x = 0; x < width; x++)
            {
            	this.initChunkSeed((long)(x + chunkX), (long)(z + chunkZ));
            	
            	final int parentIndex = x + 1 + (z + 1) * (width + 2);
            	final int currentBiome = parentInts[parentIndex];
            	final int riverInt = secondParentInts[parentIndex];
            	final boolean useMutatedHill = (riverInt - 2) % 29 == 0;
            	final boolean useMutation = currentBiome != 0 && riverInt >= 2 && (riverInt - 2) % 29 == 1;
            	
            	if (!nullData)
            	{
            		if (useMutation)
                	{
            			//If there is a mutation, use it, otherwise use currentBiome
                		final Integer mutation = this.hillData.getMutation(currentBiome);
                		returnInts[x + z * width] = mutation != null ? mutation : currentBiome;
                	}
            		else if (this.nextInt(3) != 0 && !useMutatedHill)
            		{
            			returnInts[x + z * width] = currentBiome;
            		}
            		else
            		{
            			int hill = -1;
            			final int[] hills = this.hillData.getHills(currentBiome);
            			
            			if (hills != null)
            			{
            				if (hills.length > 2)
                			{
                				if (nextInt(3) == 0)
                				{
                					if (nextInt(2) == 0)
                    				{
                    					hill = hills[1];
                    				}
                					else
                					{
                						hill = hills[0];
                					}
                				}
                				else
                				{
                					hill = hills[2];
                				}
                			}
                			else if (hills.length == 2)
                			{
                				if (nextInt(3) == 0)
                				{
                					hill = hills[0];
                				}
                				else
                				{
                					hill = hills[1];
                				}
                			}
                			else
                			{
                				hill = hills[0];
                			}
            			}
            			
            			if (hill != -1)
            			{
            				if (useMutatedHill)
            				{
            					final Integer mutation = this.hillData.getMutation(hill);
            					hill = mutation != null ? mutation : -1;
            				}

            				if (hill != -1)
            				{
            					final int biome1 = parentInts[x + 1 + (z) * (width + 2)];
            					final int biome2 = parentInts[x + 2 + (z + 1) * (width + 2)];
                                final int biome3 = parentInts[x + (z + 1) * (width + 2)];
                                final int biome4 = parentInts[x + 1 + (z + 2) * (width + 2)];
            					int count = 0;
            					
            					if (biomesEqualOrMesaPlateau(biome1, currentBiome))
                                {
                                    ++count;
                                }

                                if (biomesEqualOrMesaPlateau(biome2, currentBiome))
                                {
                                    ++count;
                                }

                                if (biomesEqualOrMesaPlateau(biome3, currentBiome))
                                {
                                    ++count;
                                }

                                if (biomesEqualOrMesaPlateau(biome4, currentBiome))
                                {
                                    ++count;
                                }
            					
            					if (count >= 3)
            					{
            						returnInts[x + z * width] = hill;
            					}
            					else
            					{
            						returnInts[x + z * width] = currentBiome;
            					}
            				}
            				else
            				{
            					returnInts[x + z * width] = currentBiome;
            				}
            			}
            			else
            			{
            				returnInts[x + z * width] = currentBiome;
            			}
            		}
            	}
            	else
            	{
            		returnInts[x + z * width] = currentBiome;
            	}
            	
            	
            	
            	
            	
            	
            	
            	
            	/*final HillBiomesData data = this.hillMap.get(currentBiome);

				if (data != null)
				{
					final int randValue = secondParentInts[parentIndex] - 2;

					if (this.useVariantBiome(randValue, data))
					{
						returnInts[x + z * width] = this.getVariantBiome(currentBiome, data);
					}
					else if (this.useVariantHillBiome(randValue, data))
					{
						int biome1 = parentInts[x + 1 + (z) * (width + 2)];
                        int biome2 = parentInts[x + 2 + (z + 1) * (width + 2)];
                        int biome3 = parentInts[x + (z + 1) * (width + 2)];
                        int biome4 = parentInts[x + 1 + (z + 2) * (width + 2)];
						returnInts[x + z * width] = this.getVariantHillBiome(currentBiome, biome1, biome2, biome3, biome4, data);
					}
					else if (this.useHillBiome(randValue, data))
					{
						int biome1 = parentInts[x + 1 + (z) * (width + 2)];
                        int biome2 = parentInts[x + 2 + (z + 1) * (width + 2)];
                        int biome3 = parentInts[x + (z + 1) * (width + 2)];
                        int biome4 = parentInts[x + 1 + (z + 2) * (width + 2)];
						returnInts[x + z * width] = this.getHillBiome(currentBiome, biome1, biome2, biome3, biome4, data);
					}
					else
					{
						returnInts[x + z * width] = currentBiome;
					}
				}
				else
				{
					returnInts[x + z * width] = currentBiome;
				}*/
			}
        }
		return returnInts;
	}
	
//	public boolean useHillBiome(final int value, final HillBiomesData data)
//	{
//		final Double chance = data.getHillBiomeChance();
//		
//		if (chance != null)
//		{
//			if (chance <= 0.5)
//			{
//				final int modulo = (int) (1 / chance);
//
//				return value % modulo == 0;
//			}
//			else
//			{
//				final int modulo = (int) (1 / (1 - chance));
//
//				return !(value % modulo == 0);
//			}
//		}
//		else
//		{
//			final Double globalChance = this.definition.getGlobalHillBiomeChance();
//			
//			if (globalChance != null)
//			{
//				if (globalChance <= 0.5)
//				{
//					final int modulo = (int) (1 / globalChance);
//
//					return value % modulo == 0;
//				}
//				else
//				{
//					final int modulo = (int) (1 / (1 - globalChance));
//
//					return !(value % modulo == 0);
//				}
//			}
//		}
//		return false;
//	}
//	
//	public int getHillBiome(final int currentBiome, final int biome1, final int biome2, final int biome3, final int biome4, final HillBiomesData data)
//	{
//		final Integer hillBiome = data.getHillBiome();
//		
//		if (hillBiome != null)
//		{
//			//Check if at least 3 surrounding biomes are the same as the current
//			int count = 0;
//			
//			if (currentBiome == biome1)
//			{
//				count++;
//			}
//			if (currentBiome == biome2)
//			{
//				count++;
//			}
//			if (currentBiome == biome3)
//			{
//				count++;
//			}
//			if (currentBiome == biome4)
//			{
//				count++;
//			}
//			if (count > 2)
//			{
//				return hillBiome;
//			}
//			
//			//Get comparable biomes array list
//			final List<int[]> comparableBiomes = this.definition.getComparableBiomes();
//			
//			if (comparableBiomes != null)
//			{
//				//loop through list
//				for (final int[] biomes : comparableBiomes)
//				{
//					if (biomes != null && biomes.length > 0)
//					{
//						boolean current = false;
//						count = 0;
//
//						//loop through array, looking for it to contain both the current biome and 3+ of the surrounding biomes
//						for (int i = 0; i < biomes.length; i++)
//						{
//							if (biomes[i] == currentBiome)
//							{
//								current = true;
//							}
//							if (biomes[i] == biome1)
//							{
//								count++;
//							}
//							if (biomes[i] == biome1)
//							{
//								count++;
//							}
//							if (biomes[i] == biome1)
//							{
//								count++;
//							}
//							if (biomes[i] == biome1)
//							{
//								count++;
//							}
//						}
//						//Check if both current biome and 3+ of the surrounding biomes were found in array
//						if (current && count > 2)
//						{
//							return hillBiome;
//						}
//					}
//				}
//			}
//		}
//		return currentBiome;
//	}
//	
//	public boolean useVariantBiome(final int value, final HillBiomesData data)
//	{
//		final Double chance = data.getVariantBiomeChance();
//		
//		if (chance != null)
//		{
//			if (chance <= 0.5)
//			{
//				final int modulo = (int) (1 / chance);
//
//				return value % modulo == 0;
//			}
//			else
//			{
//				final int modulo = (int) (1 / (1 - chance));
//
//				return !(value % modulo == 0);
//			}
//		}
//		else
//		{
//			final Double globalChance = this.definition.getGlobalVariantBiomeChance();
//			
//			if (globalChance != null)
//			{
//				if (globalChance <= 0.5)
//				{
//					final int modulo = (int) (1 / globalChance);
//
//					return value % modulo == 0;
//				}
//				else
//				{
//					final int modulo = (int) (1 / (1 - globalChance));
//
//					return !(value % modulo == 0);
//				}
//			}
//		}
//		return false;
//	}
//	
//	public int getVariantBiome(final int currentBiome, final HillBiomesData data)
//	{
//		final Integer biome = data.getVariantBiome();
//		
//		return biome != null ? biome : currentBiome;
//	}
//	
//	public boolean useVariantHillBiome(final int value, final HillBiomesData data)
//	{
//		final Double chance = data.getVariantHillBiomeChance();
//		
//		if (chance != null)
//		{
//			if (chance <= 0.5)
//			{
//				final int modulo = (int) (1 / chance);
//
//				return value % modulo == 1;
//			}
//			else
//			{
//				final int modulo = (int) (1 / (1 - chance));
//
//				return !(value % modulo == 1);
//			}
//		}
//		else
//		{
//			final Double globalChance = this.definition.getGlobalVariantHillBiomeChance();
//			
//			if (globalChance != null)
//			{
//				if (globalChance <= 0.5)
//				{
//					final int modulo = (int) (1 / globalChance);
//
//					return value % modulo == 1;
//				}
//				else
//				{
//					final int modulo = (int) (1 / (1 - globalChance));
//
//					return !(value % modulo == 1);
//				}
//			}
//		}
//		return false;
//	}
//	
//	public int getVariantHillBiome(final int currentBiome, final int biome1, final int biome2, final int biome3, final int biome4, final HillBiomesData data)
//	{
//		final Integer hillBiome = data.getVariantHillBiome();
//		
//		if (hillBiome != null)
//		{
//			//Check if at least 3 surrounding biomes are the same as the current
//			int count = 0;
//			
//			if (currentBiome == biome1)
//			{
//				count++;
//			}
//			if (currentBiome == biome2)
//			{
//				count++;
//			}
//			if (currentBiome == biome3)
//			{
//				count++;
//			}
//			if (currentBiome == biome4)
//			{
//				count++;
//			}
//			if (count > 2)
//			{
//				return hillBiome;
//			}
//			
//			//Get comparable biomes array list
//			final List<int[]> comparableBiomes = this.definition.getComparableBiomes();
//			
//			if (comparableBiomes != null)
//			{
//				//loop through list
//				for (final int[] biomes : comparableBiomes)
//				{
//					if (biomes != null && biomes.length > 0)
//					{
//						boolean current = false;
//						count = 0;
//
//						//loop through array, looking for it to contain both the current biome and 3+ of the surrounding biomes
//						for (int i = 0; i < biomes.length; i++)
//						{
//							if (biomes[i] == currentBiome)
//							{
//								current = true;
//							}
//							if (biomes[i] == biome1)
//							{
//								count++;
//							}
//							if (biomes[i] == biome1)
//							{
//								count++;
//							}
//							if (biomes[i] == biome1)
//							{
//								count++;
//							}
//							if (biomes[i] == biome1)
//							{
//								count++;
//							}
//						}
//						//Check if both current biome and 3+ of the surrounding biomes were found in array
//						if (current && count > 2)
//						{
//							return hillBiome;
//						}
//					}
//				}
//			}
//		}
//		return currentBiome;
//	}
}