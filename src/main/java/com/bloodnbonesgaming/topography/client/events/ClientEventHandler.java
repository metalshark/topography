package com.bloodnbonesgaming.topography.client.events;

import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalLong;
import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.client.ChatListenerCopy;
import com.bloodnbonesgaming.topography.client.gui.GuiCreateWorld;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;
import com.bloodnbonesgaming.topography.common.config.GlobalConfig;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.util.EventSide;
import com.bloodnbonesgaming.topography.common.util.FileHelper;
import com.bloodnbonesgaming.topography.common.util.RegistryHelper;
import com.bloodnbonesgaming.topography.common.util.StructureHelper;
import com.bloodnbonesgaming.topography.common.util.TopographyWorldData;
import com.bloodnbonesgaming.topography.common.util.Util;
import com.bloodnbonesgaming.topography.common.world.DimensionTypeTopography;
import com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorVoid;
import com.mojang.serialization.Lifecycle;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.CreateWorldScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries.Impl;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.util.text.ChatType;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.ColumnFuzzedBiomeMagnifier;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ISpawnWorldInfo;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.thread.SidedThreadGroups;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientEventHandler {

	@SubscribeEvent
	public void OnOpenGui(final GuiOpenEvent event) {
		if (event.getGui() instanceof CreateWorldScreen && !(event.getGui() instanceof GuiCreateWorld)) {
			//Initialize the config manager so presets exist for the gui
			ConfigurationManager.init();
			event.setGui(GuiCreateWorld.func_243425_a(((CreateWorldScreen)event.getGui()).parentScreen));
		}
	}
	
	@SubscribeEvent
	public void OnWorldTick(WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			if (!event.world.isRemote) {
				if (event.world.getDimensionKey().getLocation().equals(new ResourceLocation("overworld"))) {//Make sure it's the overworld
					if (!TopographyWorldData.exists((ServerWorld) event.world)) {
						Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
						
						if (preset != null) {
							for (Entry<ResourceLocation, DimensionDef> entry : preset.defs.entrySet()) {
								DimensionDef def = entry.getValue();
								
								if (def.spawnStructure != null) {
									RegistryKey<World> worldKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, entry.getKey());
									final ServerWorld dimWorld = Minecraft.getInstance().getIntegratedServer().getWorld(worldKey);
                                    
									if (dimWorld != null) {
										int preloadArea = def.spawnStructure.getSize().getX();
                                        preloadArea = def.spawnStructure.getSize().getZ() > preloadArea ? def.spawnStructure.getSize().getZ() : preloadArea;
                                        preloadArea = preloadArea / 16;
                                        preloadArea += 4;
                                        Topography.getLog().info("Preloading " + ((preloadArea * 2 + 1) * (preloadArea * 2 + 1)) + " chunks for spawn structure in dimension " + entry.getKey());
                    	            	
                    	            	for (int x = -preloadArea; x < preloadArea; x++)
                                        {
                                            for (int z = -preloadArea; z < preloadArea; z++)
                                            {
                                               dimWorld.getChunkProvider().getChunk(x, z, true);
                                            }
                                        }
                    	            	Topography.getLog().info("Spawning structure for dimension " + entry.getKey());
                    	            	BlockPos pos = new BlockPos(0, def.spawnStructureHeight, 0);
                    	            	def.spawnStructure.func_237146_a_(dimWorld, pos, pos, new PlacementSettings(), dimWorld.rand, 2);
                                        final BlockPos spawn = StructureHelper.getSpawn(def.spawnStructure);
                                        
                                        if (spawn != null) {
                        	            	dimWorld.setBlockState(spawn.add(0, def.spawnStructureHeight, 0), Blocks.AIR.getDefaultState(), 2);
                                        }
                                        
                                        if (entry.getKey().equals(new ResourceLocation("overworld")))
                                        {
                                            
                                            if (spawn != null)
                                            {
                                                dimWorld.getGameRules().get(GameRules.SPAWN_RADIUS).changeValue(new GameRules.IntegerValue(null, 0), null);
                                                //They added a spawn angle arg for some reason?
                                                ((ISpawnWorldInfo)dimWorld.getWorldInfo()).setSpawn(spawn.add(0, def.spawnStructureHeight, 0), 0);
                                            }
                                        }
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void OnServerAboutToStart(FMLServerAboutToStartEvent event) {
		
		String line = FileHelper.readLineFromFile(event.getServer().anvilConverterForAnvilFile.getWorldDir().toString() + "/topography.txt");//TODO Check file existence first
		
		if (line != null) {
			Topography.getLog().info("Read preset: " + line);
			long seed = ((ServerWorldInfo)event.getServer().serverConfig).generatorSettings.getSeed();
			Topography.getLog().info("CE " + seed);
			boolean generateStructures = true;
			
			//Reload everything for the world starting
//			ConfigurationManager.init();
//			ConfigurationManager.getGlobalConfig().setPreset(line);
			Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
			
			if (preset != null) {
				Topography.getLog().info("Using preset: " + preset.internalID);
				Impl impl = event.getServer().field_240767_f_;
				
				RegistryHelper.UpdateRegistries(impl);
//				preset.readDimensionDefs();
				//Make new registry instead of reusing the current one^
				//DynamicRegistries.Impl impl = DynamicRegistries.func_239770_b_();
				
				//SimpleRegistry<Dimension> registry = DimensionType.getDefaultSimpleRegistry(impl.getRegistry(Registry.DIMENSION_TYPE_KEY), impl.getRegistry(Registry.BIOME_KEY), impl.getRegistry(Registry.NOISE_SETTINGS_KEY), seed);
				SimpleRegistry<Dimension> registry = ((ServerWorldInfo)event.getServer().serverConfig).generatorSettings.func_236224_e_();
				for (ResourceLocation location : registry.keySet()) {
					Topography.getLog().info("key: " + location);//No overworld? When is it added?
				}
				
				for (Entry<ResourceLocation, DimensionDef> entry : preset.defs.entrySet()) {
					ResourceLocation location = entry.getKey();
					RegistryKey<Dimension> key = RegistryKey.getOrCreateKey(Registry.DIMENSION_KEY, location);
					Dimension oldDim = registry.getValueForKey(key);
					ChunkGenerator chunkGen = entry.getValue().getChunkGenerator(seed, impl.getRegistry(Registry.BIOME_KEY), impl.getRegistry(Registry.NOISE_SETTINGS_KEY));
					
					if (chunkGen == null) {
//						continue;
						if (oldDim != null) {
							chunkGen = oldDim.getChunkGenerator();//Does this need a new copy with the new seed? Shouldn't the seed be the same?
						} else {
							//TODO Add default chunk generator
							chunkGen = new ChunkGeneratorVoid(new SingleBiomeProvider(impl.getRegistry(Registry.BIOME_KEY).getOrThrow(Biomes.PLAINS)), () -> { return new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745, 0.9999999814507745, 80.0, 160.0), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false);}, seed);
							//chunkGen = new ChunkGeneratorVoid(new SingleBiomeProvider(impl.getRegistry(Registry.BIOME_KEY).getOrThrow(Biomes.PLAINS)), () -> { return new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false);}, seed) ;
						}
					}
					//Add modifications to the structure separation settings in the chunk generator
					Map<Structure<?>, StructureSeparationSettings> structureSpacingMap = chunkGen.func_235957_b_().func_236195_a_();
					
					for (Entry<String, StructureSeparationSettings> settings : entry.getValue().structureSpacingMap.entrySet()) {
						try {
							structureSpacingMap.put(ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation(settings.getKey())), settings.getValue());
						} catch(Exception e) {
							Topography.getLog().error(e);
						}
					}

					Supplier<DimensionType> typeSupplier;
					
					if (oldDim != null) {
						typeSupplier = oldDim.getDimensionTypeSupplier();
					} else {
						DimensionType dimType = new DimensionTypeTopography(preset, OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, 256, ColumnFuzzedBiomeMagnifier.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), new ResourceLocation("overworld"), 0.0F);
						event.getServer().field_240767_f_.getRegistry(Registry.DIMENSION_TYPE_KEY).register(RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, entry.getKey()), dimType, Lifecycle.stable());
						typeSupplier = () -> {
							//return DimensionType.func_236019_a_();
							return dimType;
						};
					}
					if (entry.getValue().getDimensionType() != null) {
						typeSupplier = entry.getValue().getDimensionType();
					}
					
					Topography.getLog().info("Registering dimension: " + key.getLocation());
					registry.register(key, new Dimension(typeSupplier, chunkGen), Lifecycle.stable());
				}
				((ServerWorldInfo)event.getServer().serverConfig).generatorSettings = new DimensionGeneratorSettings(seed,
						generateStructures, false, registry);
			}
		}
	}
	
	private boolean registeredChatListener = false;
	
	@SubscribeEvent
	public void onGuiOpen(final GuiOpenEvent event)
	{
		if (event.getGui() instanceof MainMenuScreen)
		{
			if (!registeredChatListener)
			{
				Topography.getLog().info("Registering Clipboard chat listener");
//				(Minecraft.getMinecraft().ingameGUI.chatListeners.get(ChatType.CHAT)).add(new ChatListenerCopy());
				(Minecraft.getInstance().ingameGUI.chatListeners.get(ChatType.SYSTEM)).add(new ChatListenerCopy());
//				(Minecraft.getMinecraft().ingameGUI.chatListeners.get(ChatType.GAME_INFO)).add(new ChatListenerCopy());
				this.registeredChatListener = true;
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
		ConfigurationManager.getGlobalConfig().clean();
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onAllEvents(Event event) {
		try {
			GlobalConfig global = ConfigurationManager.getGlobalConfig();
			
			if (global != null) {
				Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
				
				if (preset != null) {
					if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
						preset.fireEventSubscribers(event.getClass().getSimpleName(), event, EventSide.SERVER, EventSide.ANY);
					} else {
						preset.fireEventSubscribers(event.getClass().getSimpleName(), event, EventSide.CLIENT, EventSide.ANY);
					}
				}
			}
		}
		catch(Exception e) {
			Topography.getLog().error("Script error: ", e);
		}
	}
}
