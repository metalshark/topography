
function buildChunkGenerator(seed, biomeRegistry, dimensionSettingsRegistry) {
	var biomesToRemove = ["minecraft:eroded_badlands"];
	var biomeTypesToRemove = [BiomeDictionary.Type.WATER, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.VOID, BiomeDictionary.Type.OCEAN];
	var biomes = BiomeHelper.withoutBiomes(BiomeHelper.withoutTypes(BiomeHelper.forOverworld(), biomeTypesToRemove), biomesToRemove);
		
	return new ChunkGeneratorSimplexSkylands(biomes, biomeRegistry, function() {
		return RegistryHelper.get(dimensionSettingsRegistry, "minecraft:overworld");
	}, seed, 128, 32);
}
