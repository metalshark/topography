package com.bloodnbonesgaming.topography.common.world.gen;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.carver.ICarverConfig;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

public class GenerationHandler {
	
	private NoisePhase noisePhase = new NoisePhase();
	private CarverPhase carverPhase = new CarverPhase();
	private LiquidCarverPhase liquidCarverPhase = new LiquidCarverPhase();
	
	
	
	private class NoisePhase extends GenerationPhase {

		protected Map<Biome, DataObj> dataMap = new HashMap<Biome, DataObj>();
		
		@Override
		public void addGenerator(Collection<Biome> biomes, Object... args) {
			for (Biome biome : biomes) {
				DataObj data = this.getOrAdd(biome, this.dataMap);
				
				data.addGenerator(args);
			}
		}

		@Override
		public boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk) {
			ChunkPos chunkpos = chunk.getPos();
		    Biome biome = chunkGenerator.getBiomeProvider().getNoiseBiome(chunkpos.x << 2, 0, chunkpos.z << 2);
		    
		    if (this.dataMap.containsKey(biome)) {
		    	return this.dataMap.get(biome).generate(world, chunkGenerator, chunks, chunk);
		    }
			return false;
		}

		@Override
		public void disableDefault(Collection<Biome> biomes) {
			for (Biome biome : biomes) {
				DataObj data = this.getOrAdd(biome, this.dataMap);
				
				data.disableDefault();
			}
		}

		@Override
		public void addDefault(Collection<Biome> biomes) {
			for (Biome biome : biomes) {
				DataObj data = this.getOrAdd(biome, this.dataMap);
				
				data.addDefault();
			}
		}

		@Override
		protected DataObj getOrAdd(Biome biome, Map<Biome, DataObj> map) {
			if (!map.containsKey(biome)) {
				map.put(biome, new NoisePhase.Data());
			}
			return map.get(biome);
		}
		
		private class Data extends DataObj {

			private List<Object> generatorsPre = new ArrayList<Object>();
			private List<Object> generatorsPost = new ArrayList<Object>();
			private boolean disableDefault = false;
			private boolean addDefault = false;
			
			@Override
			public void addGenerator(Object... args) {
				// TODO Auto-generated method stub
				if (args[0] instanceof IGenerator) {
					if (!addDefault) {
						generatorsPre.add(args[0]);
					} else {
						generatorsPost.add(args[0]);
					}
				} else {
					//TODO Handle other types
				}
			}

			@Override
			public boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk) {
				// TODO Auto-generated method stub
				if (!this.disableDefault) {
					WorldGenRegion worldgenregion = new WorldGenRegion(world, chunks);
					chunkGenerator.func_230352_b_(worldgenregion, world.func_241112_a_().getStructureManager(worldgenregion), chunk);
				}
				
				long seed = world.getSeed();
				SharedSeedRandom rand = new SharedSeedRandom();
				ChunkPos chunkpos = chunk.getPos();
				int count = 0;
				
				for (Object obj : this.generatorsPre) {
					count++;
					rand.setLargeFeatureSeed(1024 + seed + count, chunkpos.x, chunkpos.z);
					
					if (obj instanceof IGenerator) {
						IGenerator generator = (IGenerator)obj;
						generator.generate(world, chunk, rand, seed + count);
					} else {
						//TODO handle other types
					}
				}
				if (this.addDefault) {
					WorldGenRegion worldgenregion = new WorldGenRegion(world, chunks);
					chunkGenerator.func_230352_b_(worldgenregion, world.func_241112_a_().getStructureManager(worldgenregion), chunk);
					
					for (Object obj : this.generatorsPost) {
						count++;
						rand.setLargeFeatureSeed(1024 + seed + count, chunkpos.x, chunkpos.z);
						
						if (obj instanceof IGenerator) {
							IGenerator generator = (IGenerator)obj;
							generator.generate(world, chunk, rand, seed + count);
						} else {
							//TODO handle other types
						}
					}
				}
				return true;
			}

			@Override
			public void disableDefault() {
				// TODO Auto-generated method stub
				this.disableDefault = true;
			}

			@Override
			public void addDefault() {
				// TODO Auto-generated method stub
				this.addDefault = true;
			}
		}
	}
	
	private class CarverPhase extends GenerationPhase {
		
		protected Map<Biome, DataObj> dataMap = new HashMap<Biome, DataObj>();

		@Override
		public void addGenerator(Collection<Biome> biomes, Object... args) {
			for (Biome biome : biomes) {
				DataObj data = this.getOrAdd(biome, this.dataMap);
				
				data.addGenerator(args);
			}
		}

		@Override
		public boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk) {
			return generate(world, chunkGenerator, chunks, chunk, GenerationStage.Carving.AIR);
		}
		
		protected boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk, GenerationStage.Carving stage) {
			ChunkPos chunkpos = chunk.getPos();
		    Biome biome = chunkGenerator.getBiomeProvider().getNoiseBiome(chunkpos.x << 2, 0, chunkpos.z << 2);
		    
		    if (this.dataMap.containsKey(biome)) {
		    	return this.dataMap.get(biome).generate(world, chunkGenerator, chunks, chunk);
		    }
			return false;
		}

		@Override
		public void disableDefault(Collection<Biome> biomes) {
			for (Biome biome : biomes) {
				DataObj data = this.getOrAdd(biome, this.dataMap);
				
				data.disableDefault();
			}
		}

		@Override
		public void addDefault(Collection<Biome> biomes) {
			for (Biome biome : biomes) {
				DataObj data = this.getOrAdd(biome, this.dataMap);
				
				data.addDefault();
			}
		}

		@Override
		protected DataObj getOrAdd(Biome biome, Map<Biome, DataObj> map) {
			if (!map.containsKey(biome)) {
				map.put(biome, new CarverPhase.Data(GenerationStage.Carving.AIR));
			}
			return map.get(biome);
		}
		
		private class Data extends DataObj {
			private List<Object> generatorsPre = new ArrayList<Object>();
			private List<Object> generatorsPost = new ArrayList<Object>();
			private boolean disableDefault = false;
			private boolean addDefault = false;
			private GenerationStage.Carving stage;
			
			private Data(GenerationStage.Carving stage) {
				this.stage = stage;
			}
			
			@Override
			public void addGenerator(Object... args) {
				// TODO Auto-generated method stub
				if (args[0] instanceof IGenerator) {
					if (!this.addDefault) {
						generatorsPre.add(args[0]);
					} else {
						generatorsPost.add(args[0]);
					}
				} else if (args[0] instanceof String && args[1] instanceof ICarverConfig) {

					WorldCarver<?> carver = ForgeRegistries.WORLD_CARVERS.getValue(new ResourceLocation((String)args[0]));

					if (carver != null) {
						ConfiguredCarver<?> configuredCarver = new ConfiguredCarver(carver, (ICarverConfig) args[1]);
						
						if (!this.addDefault) {
							generatorsPre.add(configuredCarver);
						} else {
							generatorsPost.add(configuredCarver);
						}
					}
				}
			}
			
			@Override
			protected boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk) {
				if (!disableDefault) {
					chunkGenerator.func_230350_a_(world.getSeed(), world.getBiomeManager(), chunk, stage);// Generate defaults
				}
				
				long seed = world.getSeed();
				BiomeManager biomemanager = world.getBiomeManager().copyWithProvider(chunkGenerator.getBiomeProvider());
				SharedSeedRandom rand = new SharedSeedRandom();
				ChunkPos chunkpos = chunk.getPos();
				int j = chunkpos.x;
				int k = chunkpos.z;
				BitSet bitset = ((ChunkPrimer) chunk).getOrAddCarvingMask(stage);
			    Biome biome = chunkGenerator.getBiomeProvider().getNoiseBiome(chunkpos.x << 2, 0, chunkpos.z << 2);

				// Generate pre defaults
				for (int l = j - 8; l <= j + 8; ++l) {
					for (int i1 = k - 8; i1 <= k + 8; ++i1) {
						
						ListIterator<Object> listiterator = this.generatorsPre.listIterator();

						while (listiterator.hasNext()) {
							int index = listiterator.nextIndex();
							Object obj = listiterator.next();
							rand.setLargeFeatureSeed(1024 + seed + index, l, i1);
							
							if (obj instanceof IGenerator) {
								if (l == j && i1 == k) {//Only generate an IGenerator when the loop is on the same chunk as the current one, so it doesn't generate 500 times per chunk
									IGenerator generator = (IGenerator)obj;
									generator.generate(world, chunk, rand, seed + index);
								}
							} else if (obj instanceof ConfiguredCarver) {
								ConfiguredCarver<?> configuredcarver = (ConfiguredCarver<?>) obj;
								
								if (configuredcarver.shouldCarve(rand, l, i1)) {
									configuredcarver.carveRegion(chunk, biomemanager::getBiome, rand, chunkGenerator.getSeaLevel(), l, i1, j, k, bitset);
								}
							}
						}
					}
				}

				if (addDefault) {
					chunkGenerator.func_230350_a_(world.getSeed(), world.getBiomeManager(), chunk, stage);// Generate defaults

					// Generate post defaults
					for (int l = j - 8; l <= j + 8; ++l) {
						for (int i1 = k - 8; i1 <= k + 8; ++i1) {
							ListIterator<Object> listiterator = this.generatorsPost.listIterator();

							while (listiterator.hasNext()) {
								int index = listiterator.nextIndex();
								Object obj = listiterator.next();
								rand.setLargeFeatureSeed(2048 + seed + index, l, i1);
								
								if (obj instanceof IGenerator) {
									if (l == j && i1 == k) {//Only generate an IGenerator when the loop is on the same chunk as the current one, so it doesn't generate 500 times per chunk
										IGenerator generator = (IGenerator)obj;
										generator.generate(world, chunk, rand, seed + index);
									}
								} else if (obj instanceof ConfiguredCarver) {
									ConfiguredCarver<?> configuredcarver = (ConfiguredCarver<?>) obj;
									
									if (configuredcarver.shouldCarve(rand, l, i1)) {
										configuredcarver.carveRegion(chunk, biomemanager::getBiome, rand, chunkGenerator.getSeaLevel(), l, i1, j, k, bitset);
									}
								}
							}
						}
					}
				}
				return true;
			}

			@Override
			public void disableDefault() {
				this.disableDefault = true;
			}

			@Override
			public void addDefault() {
				this.addDefault = true;
			}
		}
	}
	
	private class LiquidCarverPhase extends CarverPhase {
		@Override
		public boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk) {
			return generate(world, chunkGenerator, chunks, chunk, GenerationStage.Carving.LIQUID);
		}

		@Override
		protected DataObj getOrAdd(Biome biome, Map<Biome, DataObj> map) {
			if (!map.containsKey(biome)) {
				map.put(biome, new CarverPhase.Data(GenerationStage.Carving.LIQUID));
			}
			return map.get(biome);
		}
	}
	
	private abstract class GenerationPhase {
		public abstract void addGenerator(Collection<Biome> biomes, Object... args);
		public abstract boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk);
		public abstract void disableDefault(Collection<Biome> biomes);
		public abstract void addDefault(Collection<Biome> biomes);
		protected abstract DataObj getOrAdd(Biome biome, Map<Biome, DataObj> map);
	}
	
	private abstract class DataObj {
		public abstract void addGenerator(Object... args);
		protected abstract boolean generate(ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk);
		public abstract void disableDefault();
		public abstract void addDefault();
	}
	
	public enum EnumGenerationPhase {
		NOISE {
			@Override
			public void addGenerator(GenerationHandler handler, Collection<Biome> biomes, Object... args) {
				// TODO Auto-generated method stub
				handler.noisePhase.addGenerator(biomes, args);
			}

			@Override
			public boolean generate(GenerationHandler handler, ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk) {
				// TODO Auto-generated method stub
				return handler.noisePhase.generate(world, generator, chunks, chunk);
			}

			@Override
			public void disableDefault(GenerationHandler handler, Collection<Biome> biomes) {
				// TODO Auto-generated method stub
				handler.noisePhase.disableDefault(biomes);
			}

			@Override
			public void addDefault(GenerationHandler handler, Collection<Biome> biomes) {
				// TODO Auto-generated method stub
				handler.noisePhase.addDefault(biomes);
			}
		},
		CARVERS {
			@Override
			public void addGenerator(GenerationHandler handler, Collection<Biome> biomes, Object... args) {
				// TODO Auto-generated method stub
				handler.carverPhase.addGenerator(biomes, args);
			}

			@Override
			public boolean generate(GenerationHandler handler, ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk) {
				// TODO Auto-generated method stub
				return handler.carverPhase.generate(world, generator, chunks, chunk);
			}

			@Override
			public void disableDefault(GenerationHandler handler, Collection<Biome> biomes) {
				// TODO Auto-generated method stub
				handler.carverPhase.disableDefault(biomes);
			}

			@Override
			public void addDefault(GenerationHandler handler, Collection<Biome> biomes) {
				// TODO Auto-generated method stub
				handler.carverPhase.addDefault(biomes);
			}
		},
		LIQUID_CARVERS {
			@Override
			public void addGenerator(GenerationHandler handler, Collection<Biome> biomes, Object... args) {
				// TODO Auto-generated method stub
				handler.liquidCarverPhase.addGenerator(biomes, args);
			}

			@Override
			public boolean generate(GenerationHandler handler, ServerWorld world, ChunkGenerator generator, List<IChunk> chunks, IChunk chunk) {
				// TODO Auto-generated method stub
				return handler.liquidCarverPhase.generate(world, generator, chunks, chunk);
			}

			@Override
			public void disableDefault(GenerationHandler handler, Collection<Biome> biomes) {
				// TODO Auto-generated method stub
				handler.liquidCarverPhase.disableDefault(biomes);
			}

			@Override
			public void addDefault(GenerationHandler handler, Collection<Biome> biomes) {
				// TODO Auto-generated method stub
				handler.liquidCarverPhase.addDefault(biomes);
			}
		};
		
		public abstract void addGenerator(GenerationHandler handler, Collection<Biome> biomes, Object... args);
		public abstract boolean generate(GenerationHandler handler, ServerWorld world, ChunkGenerator chunkGenerator, List<IChunk> chunks, IChunk chunk);
		public abstract void disableDefault(GenerationHandler handler, Collection<Biome> biomes);
		public abstract void addDefault(GenerationHandler handler, Collection<Biome> biomes);
	}
}
