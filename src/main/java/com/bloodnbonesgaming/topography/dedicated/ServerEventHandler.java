package com.bloodnbonesgaming.topography.dedicated;

import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalLong;
import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.network.SyncPacket;
import com.bloodnbonesgaming.topography.common.network.TopoPacketHandler;
import com.bloodnbonesgaming.topography.common.util.StructureHelper;
import com.bloodnbonesgaming.topography.common.util.Util;
import com.bloodnbonesgaming.topography.common.util.storage.TopographyWorldData;
import com.bloodnbonesgaming.topography.common.world.DimensionTypeTopography;
import com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorVoid;
import com.mojang.serialization.Lifecycle;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries.Impl;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
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
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class ServerEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void OnServerAboutToStart(final FMLServerAboutToStartEvent event) {
		final long seed = ((ServerWorldInfo)event.getServer().serverConfig).generatorSettings.getSeed();
		Topography.getLog().info("CE " + seed);

		final boolean generateStructures = true;
		final Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
		if (preset == null)
			return;

		Topography.getLog().info("Using preset: " + preset.internalID);
		final Impl impl = event.getServer().field_240767_f_;

		Util.Registries.UpdateRegistries(impl);
		preset.readDimensionDefs();
		//Make new registry instead of reusing the current one^
		//DynamicRegistries.Impl impl = DynamicRegistries.func_239770_b_();

		//SimpleRegistry<Dimension> registry = DimensionType.getDefaultSimpleRegistry(impl.getRegistry(Registry.DIMENSION_TYPE_KEY), impl.getRegistry(Registry.BIOME_KEY), impl.getRegistry(Registry.NOISE_SETTINGS_KEY), seed);
		final SimpleRegistry<Dimension> registry = ((ServerWorldInfo)event.getServer().serverConfig).generatorSettings.func_236224_e_();
		for (final ResourceLocation location : registry.keySet()) {
			Topography.getLog().info("key: " + location);//No overworld? When is it added?
		}

		for (final Entry<ResourceLocation, DimensionDef> entry : preset.defs.entrySet()) {
			final ResourceLocation worldType = entry.getKey();
			final RegistryKey<Dimension> worldDimensionKey = RegistryKey.getOrCreateKey(Registry.DIMENSION_KEY, worldType);
			final Dimension oldDim = registry.getValueForKey(worldDimensionKey);
			ChunkGenerator chunkGen = entry.getValue().getChunkGenerator(seed, impl.getRegistry(Registry.BIOME_KEY), impl.getRegistry(Registry.NOISE_SETTINGS_KEY));

			if (chunkGen == null) {
				if (oldDim != null) {
					chunkGen = oldDim.getChunkGenerator();//Does this need a new copy with the new seed? Shouldn't the seed be the same?
				} else {
					//TODO Add default chunk generator
					chunkGen = new ChunkGeneratorVoid(new SingleBiomeProvider(impl.getRegistry(Registry.BIOME_KEY).getOrThrow(Biomes.PLAINS)), () -> { return new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745, 0.9999999814507745, 80.0, 160.0), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false);}, seed);
					//chunkGen = new ChunkGeneratorVoid(new SingleBiomeProvider(impl.getRegistry(Registry.BIOME_KEY).getOrThrow(Biomes.PLAINS)), () -> { return new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false);}, seed) ;
				}
			}
			//Add modifications to the structure separation settings in the chunk generator
			final Map<Structure<?>, StructureSeparationSettings> structureSpacingMap = chunkGen.func_235957_b_().func_236195_a_();
			for (final Entry<String, StructureSeparationSettings> settings : entry.getValue().structureSpacingMap.entrySet()) {
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
				final DimensionType dimType = new DimensionTypeTopography(preset, OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, 256, ColumnFuzzedBiomeMagnifier.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getName(), new ResourceLocation("overworld"), 0.0F);
				event.getServer().field_240767_f_.getRegistry(Registry.DIMENSION_TYPE_KEY).register(RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, entry.getKey()), dimType, Lifecycle.stable());
				typeSupplier = () -> {
					//return DimensionType.func_236019_a_();
					return dimType;
				};
			}
			if (entry.getValue().getDimensionType() != null) {
				typeSupplier = entry.getValue().getDimensionType();
			}
			Topography.getLog().info("Registering dimension: " + oldDim);
			registry.register(worldDimensionKey, new Dimension(typeSupplier, chunkGen), Lifecycle.stable());
		}
		((ServerWorldInfo)event.getServer().serverConfig).generatorSettings = new DimensionGeneratorSettings(seed,
				generateStructures, false, registry);
	}
	
	@SubscribeEvent
	public void OnWorldTick(final WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.START)
			return;

		final World world = event.world;
		if (world.isRemote)
			return;
		final ServerWorld serverWorld = (ServerWorld) world;

		if (!serverWorld.getDimensionKey().getLocation().equals(World.OVERWORLD.getLocation()))
			return;

		if (TopographyWorldData.exists(serverWorld))
			return;

		final Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
		if (preset == null)
			return;

		final MinecraftServer server = serverWorld.getServer();
		for (final Entry<ResourceLocation, DimensionDef> entry : preset.defs.entrySet()) {
			final ResourceLocation worldType = entry.getKey();
			final DimensionDef worldDimension = entry.getValue();

			if (worldDimension.spawnStructure == null)
				return;

			final RegistryKey<World> worldKey = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, entry.getKey());
			final ServerWorld dimWorld = server.getWorld(worldKey);

			if (dimWorld == null)
				return;

			int preloadArea = worldDimension.spawnStructure.getSize().getX();
			preloadArea = Math.max(worldDimension.spawnStructure.getSize().getZ(), preloadArea);
			preloadArea = preloadArea / 16;
			preloadArea += 4;

			Topography.getLog().info("Preloading " + ((preloadArea * 2 + 1) * (preloadArea * 2 + 1)) + " chunks for spawn structure in dimension " + entry.getKey());
			for (int x = -preloadArea; x < preloadArea; x++) {
				for (int z = -preloadArea; z < preloadArea; z++) {
				   dimWorld.getChunkProvider().getChunk(x, z, true);
				}
			}

			Topography.getLog().info("Spawning structure for dimension " + worldType);
			final BlockPos pos = new BlockPos(0, worldDimension.spawnStructureHeight, 0);
			worldDimension.spawnStructure.func_237146_a_(dimWorld, pos, pos, new PlacementSettings(), dimWorld.rand, 2);

			final BlockPos spawn = StructureHelper.getSpawn(worldDimension.spawnStructure);
			if (spawn != null)
				dimWorld.setBlockState(spawn.add(0, worldDimension.spawnStructureHeight, 0), Blocks.AIR.getDefaultState(), 2);

			if (!worldType.equals(World.OVERWORLD.getLocation()))
				continue;

			if (spawn == null)
				continue;

			server.getCommandManager().handleCommand(server.getCommandSource(), "gamerule spawnRadius 0");
			((ISpawnWorldInfo) dimWorld.getWorldInfo())
					.setSpawn(spawn.add(0, worldDimension.spawnStructureHeight, 0), 0);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(final PlayerLoggedInEvent event) {
		final Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
		if (preset == null)
			return;

		TopoPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()), new SyncPacket().setPreset(preset.internalID));
	}
}
