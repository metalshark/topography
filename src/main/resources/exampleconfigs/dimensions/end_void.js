
setSpawnStructure("examples/structures/oaktree", 64);

function buildChunkGenerator(seed, biomeRegistry, dimensionSettingsRegistry) {
	var biomesToRemove = [];
	var biomeTypesToRemove = [BiomeDictionary.Type.WATER, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.VOID, BiomeDictionary.Type.OCEAN];
	var biomes = BiomeHelper.withoutBiomes(BiomeHelper.withoutTypes(BiomeHelper.forEnd(), biomeTypesToRemove), biomesToRemove);
	
	var biomeProvider = new MultiBiomeProvider(biomes, seed, 6, biomeRegistry);
	
	return new ChunkGeneratorVoid(biomeProvider, function() {
		return RegistryHelper.get(dimensionSettingsRegistry, "minecraft:end");
	}, seed);
}
