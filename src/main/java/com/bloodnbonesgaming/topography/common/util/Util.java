package com.bloodnbonesgaming.topography.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.Functions.TriFunction;
import com.bloodnbonesgaming.topography.common.world.gen.feature.RegionFeatureRedirector;
import com.bloodnbonesgaming.topography.common.world.gen.feature.VerticalOre;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameType;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.AlwaysTrueRuleTest;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.gen.feature.template.Template.EntityInfo;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class Util {
	public static ClientUtil Client = Topography.proxy.makeClientUtil();
	
	public static class Features {

		public static void addFeature(BiomeLoadingEvent event, GenerationStage.Decoration stage, Supplier<ConfiguredFeature<?, ?>> feature) {
			event.getGeneration().getFeatures(stage).add(feature);
		}
		
		public static void addOre(BiomeLoadingEvent event, Supplier<ConfiguredFeature<?, ?>> ore) {
			addFeature(event, Decoration.UNDERGROUND_ORES, ore);
		}
		
		public static void clearFeatures(BiomeLoadingEvent event, GenerationStage.Decoration stage) {
			event.getGeneration().getFeatures(stage).clear();
		}
		
		public static void clearFeatures(BiomeLoadingEvent event) {
			for(GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
				Iterator<Supplier<ConfiguredFeature<?, ?>>> iterator = event.getGeneration().getFeatures(stage).iterator();
				
				while (iterator.hasNext()) {
					if (!(iterator.next().get().feature instanceof RegionFeatureRedirector)) {//Do not remove
						iterator.remove();
					}
				}
			}
		}
		
		public static void clearStructures(BiomeLoadingEvent event) {
			event.getGeneration().getStructures().clear();
		}
		
		public static ConfiguredFeature buildConfiguredFeature(String location, IFeatureConfig config) {
			return new ConfiguredFeature(ForgeRegistries.FEATURES.getValue(new ResourceLocation(location)), config);
		}
		
		public static ConfiguredFeature heightRange(ConfiguredFeature feature, int min, int max) {
			return feature.withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(min, 0, max - min)));
		}
		
		public static ConfiguredFeature square(ConfiguredFeature feature) {
			return feature.withPlacement(Placement.SQUARE.configure(NoPlacementConfig.INSTANCE));
		}
		
		public static ConfiguredFeature chance(ConfiguredFeature feature, int chance) {
			return feature.withPlacement(Placement.CHANCE.configure(new ChanceConfig(chance)));
		}
		
		public static ConfiguredFeature count(ConfiguredFeature feature, int min, int max) {		
			return feature.withPlacement(Placement.COUNT.configure(new FeatureSpreadConfig(FeatureSpread.func_242253_a(min, max - min))));
		}
		
		public static ConfiguredFeature placement(ConfiguredFeature feature, String placement, IPlacementConfig config) {
			return feature.withPlacement(new ConfiguredPlacement(ForgeRegistries.DECORATORS.getValue(new ResourceLocation(placement)), config));
		}
		
		public static void removeStructure(BiomeLoadingEvent event, String id) {
			ResourceLocation toRemove = new ResourceLocation(id);
			Iterator<Supplier<StructureFeature<?, ?>>> iterator = event.getGeneration().getStructures().iterator();
			
			while (iterator.hasNext()) {
				Supplier<StructureFeature<?, ?>> supplier = iterator.next();
				ResourceLocation location = ForgeRegistries.STRUCTURE_FEATURES.getKey(supplier.get().field_236268_b_);
				
				if (toRemove.equals(location)) {
					iterator.remove();
				}
			}
		}
		
		public static void removeFeature(BiomeLoadingEvent event, String id) {
			ResourceLocation toRemove = new ResourceLocation(id);
			Iterator<Supplier<ConfiguredFeature<?, ?>>> iterator = event.getGeneration().getFeatures(Decoration.TOP_LAYER_MODIFICATION).iterator();
			
			while (iterator.hasNext()) {
				Supplier<ConfiguredFeature<?, ?>> supplier = iterator.next();
				ResourceLocation location = ForgeRegistries.FEATURES.getKey(supplier.get().feature);
				
				if (toRemove.equals(location)) {
					iterator.remove();
					Topography.getLog().info("Removed " + id + " from " + event.getName());
				}
			}
		}
		
		public static class Ores {
			
			public static RuleTest BASE_STONE_OVERWORLD = OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD;
			public static RuleTest NETHERRACK = OreFeatureConfig.FillerBlockType.NETHERRACK;
			public static RuleTest BASE_STONE_NETHER = OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER;
			public static RuleTest ALWAYS_TRUE = AlwaysTrueRuleTest.INSTANCE;
			
			public static void addOre(BiomeLoadingEvent event, Supplier<ConfiguredFeature<?, ?>> ore) {
				addFeature(event, Decoration.UNDERGROUND_ORES, ore);
			}
			
			public static void clearOre(BiomeLoadingEvent event) {
				clearFeatures(event, Decoration.UNDERGROUND_ORES);
			}
			
			public static ConfiguredFeature<?, ?> buildOreForOverworldStone(BlockState blockState, int clusterSize, int minHeight, int maxHeight, boolean square) {
				ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, blockState, clusterSize));
				feature = heightRange(feature, minHeight, maxHeight);
				if (square) {
					feature = square(feature);
				}
				return feature;
			}
			
			public static ConfiguredFeature<?, ?> buildOreForNetherrack(BlockState blockState, int clusterSize, int minHeight, int maxHeight, boolean square) {
				ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, blockState, clusterSize));
				feature = heightRange(feature, minHeight, maxHeight);
				if (square) {
					feature =square(feature);
				}
				return feature;
			}
			
			public static ConfiguredFeature<?, ?> buildOreForNetherStone(BlockState blockState, int clusterSize, int minHeight, int maxHeight, boolean square) {
				ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_NETHER, blockState, clusterSize));
				feature = heightRange(feature, minHeight, maxHeight);
				if (square) {
					feature = square(feature);
				}
				return feature;
			}
			
			public static ConfiguredFeature<?, ?> buildOre(BlockState blockState, RuleTest test, int clusterSize, int clusterCountMin, int clusterCountMax, int minHeight, int maxHeight, boolean square, int chance) {
				ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(test, blockState, clusterSize));
				feature = heightRange(feature, minHeight, maxHeight);
				if (chance > 0) {
					feature = chance(feature, chance);
				}
				if (square) {
					feature = square(feature);
				}
				if (clusterCountMax > 0) {
					feature = count(feature, clusterCountMin, clusterCountMax >= clusterCountMin ? clusterCountMax : clusterCountMin);
				}
				return feature;
			}
			
			public static ConfiguredFeature<?, ?> buildOre(BlockState blockState, RuleTest test, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square, int chance) {
				ConfiguredFeature<?, ?> feature = Feature.ORE.withConfiguration(new OreFeatureConfig(test, blockState, clusterSize));
				feature = heightRange(feature, minHeight, maxHeight);
				if (chance > 0) {
					feature = chance(feature, chance);
				}
				if (square) {
					feature = square(feature);
				}
				if (clusterCount > 1) {
					feature = count(feature, clusterCount, clusterCount);
				}
				return feature;
			}
			
			public static ConfiguredFeature<?, ?> buildVerticalOre(BlockState blockState, RuleTest test, int clusterSize, int clusterCountMin, int clusterCountMax, int minHeight, int maxHeight, boolean square, int chance) {
				ConfiguredFeature<?, ?> feature = VerticalOre.INSTANCE.withConfiguration(new OreFeatureConfig(test, blockState, clusterSize));
				feature = heightRange(feature, minHeight, maxHeight);
				if (chance > 0) {
					feature = chance(feature, chance);
				}
				if (square) {
					feature = square(feature);
				}
				if (clusterCountMax > 0) {
					feature = count(feature, clusterCountMin, clusterCountMax >= clusterCountMin ? clusterCountMax : clusterCountMin);
				}
				return feature;
			}
			
			public static ConfiguredFeature<?, ?> buildVerticalOre(BlockState blockState, RuleTest test, int clusterSize, int clusterCount, int minHeight, int maxHeight, boolean square, int chance) {
				ConfiguredFeature<?, ?> feature = VerticalOre.INSTANCE.withConfiguration(new OreFeatureConfig(test, blockState, clusterSize));
				feature = heightRange(feature, minHeight, maxHeight);
				if (chance > 0) {
					feature = chance(feature, chance);
				}
				if (square) {
					feature = square(feature);
				}
				if (clusterCount > 1) {
					feature = count(feature, clusterCount, clusterCount);
				}
				return feature;
			}
		}
	}
	
	public static class Registries {
				
		private static DynamicRegistries.Impl implRegistries;
		
		public static void UpdateRegistries(DynamicRegistries.Impl impl) {
			implRegistries = impl;
		}
		
		public static DynamicRegistries.Impl getRegistry() {
			return implRegistries;
		}
		
		public static Registry<Biome> getBiomeRegistry() {
			return implRegistries.getRegistry(net.minecraft.util.registry.Registry.BIOME_KEY);
		}
		
		public static Registry<Structure<?>> getStructureRegistry() {
			return implRegistries.getRegistry(net.minecraft.util.registry.Registry.STRUCTURE_FEATURE_KEY);
		}
		
		public static void registerRecipe() {
			
		}
		
		public static Object get(MutableRegistry registry, String id) {
			RegistryKey key = RegistryKey.getOrCreateKey(registry.getRegistryKey(), new ResourceLocation(id));
			return registry.getOrThrow(key);
		}
	}
	
	public static class Biomes {
		
		public static Biome getBiome(String location) throws Exception {
			try {
				return Topography.proxy.getRegistries().getRegistry(Registry.BIOME_KEY).getOrThrow(RegistryKey.getOrCreateKey(Registry.BIOME_KEY, new ResourceLocation(location)));
			} catch (Exception e) {
				throw new Exception("Could not get biome: " + location + " " + e);
			}
		}
		
		public static Biome getBiome(RegistryKey<Biome> key) throws Exception {
			return Topography.proxy.getRegistries().getRegistry(Registry.BIOME_KEY).getOrThrow(key);
		}
		
		public static List<Biome> allBiomes() {
			List<Biome> biomes = new ArrayList<Biome>();
			for (Entry<RegistryKey<Biome>, Biome> entry : Topography.proxy.getRegistries().getRegistry(Registry.BIOME_KEY).getEntries()) {
				biomes.add(entry.getValue());
			}
			return biomes;
		}
		
		public static List<Biome> forBiomes(String... biomes) throws Exception {
			List<Biome> biomeList = new ArrayList<Biome>();
			
			for (String string : biomes) {
				Biome biome = getBiome(string);
				
				if (biome != null) {
					biomeList.add(biome);
				}
			}
			return biomeList;
		}
		
		public static List<Biome> forBiomes(Biome... biomes) {
			List<Biome> biomeList = new ArrayList<Biome>();
			
			for (Biome biome : biomes) {
				if (biome != null) {
					biomeList.add(biome);
				}
			}
			return biomeList;
		}
		
		public static List<Biome> forOverworld() throws Exception {
			List<Biome> biomeList = new ArrayList<Biome>();
			Set<RegistryKey<Biome>> biomeSet = BiomeDictionary.getBiomes(Type.OVERWORLD);
			
			for (RegistryKey<Biome> key : biomeSet) {
				biomeList.add(getBiome(key));
			}
			return biomeList;
		}
		
		public static List<Biome> forNether() throws Exception {
			List<Biome> biomeList = new ArrayList<Biome>();
			Set<RegistryKey<Biome>> biomeSet = BiomeDictionary.getBiomes(Type.NETHER);
			
			for (RegistryKey<Biome> key : biomeSet) {
				biomeList.add(getBiome(key));
			}
			return biomeList;
		}
		
		public static List<Biome> forEnd() throws Exception {
			List<Biome> biomeList = new ArrayList<Biome>();
			Set<RegistryKey<Biome>> biomeSet = BiomeDictionary.getBiomes(Type.END);
			
			for (RegistryKey<Biome> key : biomeSet) {
				biomeList.add(getBiome(key));
			}
			return biomeList;
		}
		
		public static List<Biome> withoutRivers(List<Biome> biomes) {
			List<Biome> biomeList = new ArrayList<Biome>();
			
			for (Biome biome : biomes) {
				RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
				
				if (!BiomeDictionary.hasType(key, BiomeDictionary.Type.RIVER)) {
					biomeList.add(biome);
				}
			}
			return biomeList;
		}
		
		public static List<Biome> withoutOceans(List<Biome> biomes) {
			List<Biome> biomeList = new ArrayList<Biome>();
			
			for (Biome biome : biomes) {
				RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
				
				if (!BiomeDictionary.hasType(key, BiomeDictionary.Type.OCEAN)) {
					biomeList.add(biome);
				}
			}
			return biomeList;
		}
		
		public static List<Biome> withoutTypes(List<Biome> biomes, BiomeDictionary.Type... types) {
			List<Biome> biomeList = new ArrayList<Biome>();
			
			biomeLoop:
			for (Biome biome : biomes) {
				RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
				
				for (BiomeDictionary.Type type : types) {
					if (BiomeDictionary.hasType(key, type)) {
						continue biomeLoop;
					}
				}
				biomeList.add(biome);
			}
			return biomeList;
		}
		
		public static List<Biome> withoutBiomes(List<Biome> biomes, String... toRemove) throws Exception {
			List<Biome> biomeList = new ArrayList<Biome>();
			//Add all the original biomes
			biomeList.addAll(biomes);
			//Get biomes to remove
			List<Biome> biomesToRemove = forBiomes(toRemove);
			//Remove biomes from list
			biomeList.removeAll(biomesToRemove);
			return biomeList;
		}
		
		public static void addOre(BiomeLoadingEvent event, Supplier<ConfiguredFeature<?, ?>> ore) {
			event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES).add(ore);
		}
		
		public static ConfiguredFeature<?, ?> buildOreGen(BlockState blockState, int clusterSize, int minHeight, int maxHeight) {
			return Feature.ORE.withConfiguration(new OreFeatureConfig(AlwaysTrueRuleTest.INSTANCE, blockState, clusterSize)).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, 0, maxHeight))).square();
		}
		
		public static boolean test(ResourceLocation location, BiomeDictionary.Type... types) {
			if (location != null) {
				RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, location);
				
				for (BiomeDictionary.Type type : types) {
					if (BiomeDictionary.hasType(key, type)) {
						return true;
					}
				}
			}
			return false;
		}
		
		public static Biome makeBiome(String name) {
			return new Biome.Builder().precipitation(Biome.RainType.NONE).category(Biome.Category.NONE).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).setEffects((new BiomeAmbience.Builder()).setWaterColor(4159204).setWaterFogColor(329011).setFogColor(12638463).withSkyColor(0).setMoodSound(MoodSoundAmbience.DEFAULT_CAVE).build()).withMobSpawnSettings(MobSpawnInfo.EMPTY).withGenerationSettings(new BiomeGenerationSettings.Builder().withSurfaceBuilder(ConfiguredSurfaceBuilders.field_244184_p).build()).build().setRegistryName(name);
		}
	}
	
	public static class Blocks {

		public static Block getBlock(String location) {
			return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location));
		}
		
		public static Block getBlock(BlockState state) {
			return state.getBlock();
		}
		
		public static BlockState getState(String location) {
			return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(location)).getDefaultState();
		}
		
		public static BlockInfo buildRandomSpawnerBlockInfo(BlockInfo original, PlacementSettings settings) {
			MobSpawnerTileEntity tile = buildRandomSpawner(settings.getRandom(null));
			return new BlockInfo(original.pos, net.minecraft.block.Blocks.SPAWNER.getDefaultState(), getNBT(tile));
		}
		
		public static CompoundNBT getNBT(TileEntity tile) {
			return tile.serializeNBT();
		}
		
		public static BlockInfo buildBlockInfo(BlockPos pos, BlockState state, CompoundNBT nbt) {
			return new BlockInfo(pos, state, nbt);
		}
		
		public static MobSpawnerTileEntity buildRandomSpawner(Random rand) {
			MobSpawnerTileEntity tile = new MobSpawnerTileEntity();
			tile.getSpawnerBaseLogic().setEntityType(DungeonHooks.getRandomDungeonMob(rand));
			return tile;
		}
		
		public static MobSpawnerTileEntity buildSpawner(String entity) {
			MobSpawnerTileEntity tile = new MobSpawnerTileEntity();
			tile.getSpawnerBaseLogic().setEntityType(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entity)));
			return tile;
		}
		
		public static CompoundNBT setChestLoot(BlockState state, CompoundNBT nbt, Random rand, String table) {
			LockableLootTileEntity tile = (LockableLootTileEntity) LockableLootTileEntity.readTileEntity(state, nbt);
			tile.setLootTable(new ResourceLocation(table), rand.nextLong());
			return tile.serializeNBT();
		}
		
		public static CompoundNBT buildChestLoot(Random rand, String table) {
			ChestTileEntity tile = new ChestTileEntity();
			tile.setLootTable(new ResourceLocation(table), rand.nextLong());
			return tile.serializeNBT();
		}
	}

	public static class Dimensions {
		public static DimensionTypeBuilder typeBuilder() {
			return new DimensionTypeBuilder();
		}
	}
	
	public static class Worlds {
		
		public static BlockState getState(IWorld world, double x, double y, double z) {
			return getState(world, new BlockPos(x, y, z));
		}
		
		public static BlockState getState(IWorld world, BlockPos pos) {
			return world.getBlockState(pos);
		}
		
		public static String getStringID(World world) {
			return world.getDimensionKey().getLocation().toString();
		}
		
		public static ResourceLocation getID(World world) {
			return world.getDimensionKey().getLocation();
		}
		
		public static boolean test(IWorld world, String id) {
			if (world instanceof World) {
				return ((World)world).getDimensionKey().getLocation().toString().equals(id);
			}
			return false;
		}
		
		public static boolean test(IWorld world, ResourceLocation id) {
			if (world instanceof World) {
				return ((World)world).getDimensionKey().getLocation().equals(id);
			}
			return false;
		}
		
		public static boolean loopChunkX(ISeedReader reader, BlockPos pos, TriFunction<ISeedReader, BlockPos, BlockPos, Boolean> func) {
			boolean ret = false;
			Mutable localPos = new Mutable();
			Mutable worldPos = new Mutable();
			
			for (int x = 0; x < 16; x++) {
				if (func.apply(reader, localPos.setPos(x, 0, 0), worldPos.setPos(pos.getX() + x, pos.getY(), pos.getZ()))) {
					ret = true;
				}
			}
			return ret;
		}
		
		public static int getLight(World world, BlockPos pos) {
			return world.getLight(pos);
		}
		
		public static long getGameTime(World world) {
			return world.getGameTime();
		}
	}
	
	public static class Entities {
		
		public static String getStringID(Entity entity) {
			return ForgeRegistries.ENTITIES.getKey(entity.getType()).toString();
		}
		
		public static ResourceLocation getID(Entity entity) {
			return ForgeRegistries.ENTITIES.getKey(entity.getType());
		}
		
		public static boolean test(Entity entity, String id) {
			return ForgeRegistries.ENTITIES.getKey(entity.getType()).toString().equals(id);
		}
		
		public static boolean test(Entity entity, ResourceLocation id) {
			return ForgeRegistries.ENTITIES.getKey(entity.getType()).equals(id);
		}
		
		public static void addDrop(LivingDropsEvent event, String entityID, String itemID, int count) {
			EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
			
			if (event.getEntity().getType() == type) {
				event.getDrops().add(Items.buildEntity(event.getEntity().world, event.getEntity().getPosition(), Items.buildStack(itemID, count)));
			}
		}
		
		public static void addDrop(LivingDropsEvent event, String entityID, String itemID, int count, String nbt) throws CommandSyntaxException {
			EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
			
			if (event.getEntity().getType() == type) {
				event.getDrops().add(Items.buildEntity(event.getEntity().world, event.getEntity().getPosition(), Items.buildStack(itemID, count, nbt)));
			}
		}
		
		public static void addDrop(LivingDropsEvent event, String entityID, ItemEntity itemEntity) {
			EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
			
			if (event.getEntity().getType() == type) {
				event.getDrops().add(itemEntity);
			}
		}
		
		public static void addDrop(LivingDropsEvent event, String entityID, ItemStack stack) {
			EntityType<?> type = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityID));
			
			if (event.getEntity().getType() == type) {
				event.getDrops().add(Items.buildEntity(event.getEntity().world, event.getEntity().getPosition(), stack));
			}
		}
		
		public static void modifyAttribute(Entity entity, String attribute, String name, double amount, AttributeModifier.Operation operation) throws Exception {
			if (!(entity instanceof LivingEntity)) {
				throw new Exception("Can only modify attribute of living entities");
			}
			LivingEntity living = (LivingEntity)entity;
			Attribute att = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
			if (!(entity instanceof LivingEntity)) {
				throw new Exception("Attribute " + attribute + " is not registered");
			}
			ModifiableAttributeInstance ins = living.getAttribute(att);
			
			if (ins != null) {
				ins.applyPersistentModifier(new AttributeModifier(name, amount, operation));
			}
		}
		
		public static void healToMax(LivingEntity entity) {
			entity.setHealth(entity.getMaxHealth());
		}
		
		public static BlockPos getPos(Entity entity) {
			return entity.getPosition();
		}
		
		public static int getLight(Entity entity) {
			return Util.Worlds.getLight(getWorld(entity), getPos(entity));
		}
		
		public static World getWorld(Entity entity) {
			return entity.world;
		}
		
		public static EntityInfo buildEntityInfo(Vector3d pos, BlockPos blockPos, CompoundNBT nbt) {
			return new EntityInfo(pos, blockPos, nbt);
		}
		
		public static EntityInfo buildEntityInfo(BlockPos pos, BlockPos blockPos, CompoundNBT nbt) {
			return new EntityInfo(Vector3d.copy(pos), blockPos, nbt);
		}
		
		public static CompoundNBT buildNBT(String str) throws CommandSyntaxException {
			return JsonToNBT.getTagFromJson(str);
		}
	}
	
	public static class Items {
		
		public static ItemStack buildStack(String itemID) {
			return buildStack(itemID, 1);
		}
		
		public static ItemStack buildStack(String itemID, int count) {
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemID));
			return new ItemStack(item, count);
		}
		
		public static ItemStack buildStack(String itemID, String nbt) throws CommandSyntaxException {
			return buildStack(itemID, 1, nbt);
		}
		
		public static ItemStack buildStack(String itemID, int count, String nbt) throws CommandSyntaxException {
			ItemStack stack = buildStack(itemID, count);
			stack.setTag(buildNBT(nbt));
			return stack;
		}
		
		public static ItemStack buildStack(Item item) {
			return buildStack(item, 1);
		}
		
		public static ItemStack buildStack(Item item, int count) {
			return new ItemStack(item, count);
		}
		
		public static ItemStack buildStack(Item item, String nbt) throws CommandSyntaxException {
			return buildStack(item, 1, nbt);
		}
		
		public static ItemStack buildStack(Item item, int count, String nbt) throws CommandSyntaxException {
			ItemStack stack = buildStack(item, count);
			stack.setTag(buildNBT(nbt));
			return stack;
		}
		
		public static ItemEntity buildEntity(World world, double x, double y, double z, String itemID, int count) {
			ItemStack stack = buildStack(itemID, count);
			return buildEntity(world, x, y, z, stack);
		}
		
		public static ItemEntity buildEntity(World world, double x, double y, double z, ItemStack stack) {
			return new ItemEntity(world, x, y, z, stack);
		}
		
		public static ItemEntity buildEntity(World world, BlockPos pos, String itemID, int count) {
			ItemStack stack = buildStack(itemID, count);
			return buildEntity(world, pos, stack);
		}
		
		public static ItemEntity buildEntity(World world, BlockPos pos, ItemStack stack) {
			return new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
		}
		
		public static CompoundNBT buildNBT(String json) throws CommandSyntaxException {
			return JsonToNBT.getTagFromJson(json);
		}
	}
	
	public static class Players {
		public static GameType getGamemode(PlayerEntity player) {
			if (!player.world.isRemote) {
				return ((ServerPlayerEntity)player).interactionManager.getGameType();
			} else {
				return Util.Client.getGamemode();
			}
		}
		
		public static String getGamemodeName(PlayerEntity player) {
			return getGamemode(player).name();
		}
	}
	
	public static class Effects {
		public static void addPotion(LivingEntity entity, String id, int duration, int amplification, boolean ambient, boolean showParticles) {
			Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(id));
			entity.addPotionEffect(new EffectInstance(effect, duration, amplification, ambient, showParticles));
		}
	}

	public static class Structures {
		
		public static void AddStructure(BiomeLoadingEvent event, Supplier<StructureFeature<?, ?>> structure) {
			event.getGeneration().getStructures().add(structure);
		}
		
		public static void AddStructure(BiomeLoadingEvent event, String location, IFeatureConfig config) {
			event.getGeneration().getStructures().add(() -> new StructureFeature(ForgeRegistries.STRUCTURE_FEATURES.getValue(new ResourceLocation(location)), config));
		}
	}
}
