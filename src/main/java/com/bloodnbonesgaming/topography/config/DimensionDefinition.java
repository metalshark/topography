package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ClientOnly;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.client.renderer.SkyRendererCustom;
import com.bloodnbonesgaming.topography.util.SpawnStructure;
import com.bloodnbonesgaming.topography.world.StructureHandler;
import com.bloodnbonesgaming.topography.world.biome.provider.BiomeProviderConfigurable;
import com.bloodnbonesgaming.topography.world.chunkgenerator.ChunkGeneratorVoid;
import com.bloodnbonesgaming.topography.world.decorator.DecoratorScattered;
import com.bloodnbonesgaming.topography.world.generator.BiomeBlockReplacementGenerator;
import com.bloodnbonesgaming.topography.world.generator.CellInterpolationTestGenerator;
import com.bloodnbonesgaming.topography.world.generator.CellNoiseGenerator;
import com.bloodnbonesgaming.topography.world.generator.DeformedSphereGenerator;
import com.bloodnbonesgaming.topography.world.generator.DuneTestGenerator;
import com.bloodnbonesgaming.topography.world.generator.FluidPocketGenerator;
import com.bloodnbonesgaming.topography.world.generator.HangingCrystalGenerator;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.generator.IceAndSnowGenerator;
import com.bloodnbonesgaming.topography.world.generator.LayerGenerator;
import com.bloodnbonesgaming.topography.world.generator.OverworldGenerator;
import com.bloodnbonesgaming.topography.world.generator.ScatteredBlockGenerator;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaAnimalGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaCaveGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaDecorationGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaDungeonGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaFireGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaGlowstoneGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaLakeGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaLavaPocketGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaQuartzGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaRavineGenerator;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

@ScriptClassDocumentation(documentationFile = ModInfo.DOCUMENTATION_FOLDER + "Dimensions", classExplaination = 
"This file is for the options in dimension script files. These are the files referenced when registering dimensions in the Topography.txt file. "
+ "These files can be placed anywhere in the config/topography folder, and must end in '.txt'.")
public class DimensionDefinition
{
    public final Map<String, Class> classKeywords = new HashMap<String, Class>();
    private SpawnStructure spawnStructure;
    private int spawnStructureSpacing = 65;
    private boolean enviromentalFog = false;
    private Float celestialAngle;
    private boolean renderSky = true;
    private boolean renderClouds = true;
    private final List<IGenerator> generators = new ArrayList<IGenerator>();
    private boolean skylight = true;
    private float[] lightBrightnessTable = null;
    private boolean resetRelightChecks = false;
    private final List<EntityEffect> entityEffects = new ArrayList<EntityEffect>();
    private boolean canRespawn = true;
    private boolean captureTeleports = false;
    private boolean vaporizeWater = false;
    private boolean inheriteSkyRenderer = false;
    
    private SkyRendererCustom skyRenderer = null;
    
    private final Map<Integer, Map<MinMaxBounds, MinMaxBounds>> fog = new LinkedHashMap<Integer, Map<MinMaxBounds, MinMaxBounds>>();
    
    private Integer singleBiome = null;
    
    private final StructureHandler structureHandler = new StructureHandler();
    
    public DimensionDefinition()
    {
        this.classKeywords.put("ScatteredBlockGenerator", ScatteredBlockGenerator.class);
        this.classKeywords.put("FluidPocketGenerator", FluidPocketGenerator.class);
        this.classKeywords.put("HangingCrystalGenerator", HangingCrystalGenerator.class);
        this.classKeywords.put("VanillaFireGenerator", VanillaFireGenerator.class);
        this.classKeywords.put("VanillaLavaPocketGenerator", VanillaLavaPocketGenerator.class);
        this.classKeywords.put("VanillaGlowstoneGenerator", VanillaGlowstoneGenerator.class);
        this.classKeywords.put("VanillaQuartzGenerator", VanillaQuartzGenerator.class);
        this.classKeywords.put("DecoratorScattered", DecoratorScattered.class);
        this.classKeywords.put("DeformedSphereGenerator", DeformedSphereGenerator.class);
        this.classKeywords.put("CellNoiseGenerator", CellNoiseGenerator.class);
        this.classKeywords.put("LayerGenerator", LayerGenerator.class);
        this.classKeywords.put("SkyIslandGenerator", SkyIslandGenerator.class);
        this.classKeywords.put("SkyIslandType", SkyIslandType.class);
        

        this.classKeywords.put("CellInterpolationTestGenerator", CellInterpolationTestGenerator.class);
        this.classKeywords.put("DuneTestGenerator", DuneTestGenerator.class);
        this.classKeywords.put("OverworldGenerator", OverworldGenerator.class);
        this.classKeywords.put("IceAndSnowGenerator", IceAndSnowGenerator.class);
        this.classKeywords.put("BiomeBlockReplacementGenerator", BiomeBlockReplacementGenerator.class);
        this.classKeywords.put("VanillaDecorationGenerator", VanillaDecorationGenerator.class);
        this.classKeywords.put("VanillaAnimalGenerator", VanillaAnimalGenerator.class);
        this.classKeywords.put("VanillaCaveGenerator", VanillaCaveGenerator.class);
        this.classKeywords.put("VanillaRavineGenerator", VanillaRavineGenerator.class);
        this.classKeywords.put("VanillaLakeGenerator", VanillaLakeGenerator.class);
        this.classKeywords.put("VanillaDungeonGenerator", VanillaDungeonGenerator.class);
    }
    
    public BiomeProvider getBiomeProvider(final World world)
    {
        if (this.singleBiome != null)
        {
            return new BiomeProviderConfigurable(world, this.singleBiome, this);
        }
        return new BiomeProviderConfigurable(world, this);
    }
    
    public IChunkGenerator getChunkGenerator(final World world)
    {
        return new ChunkGeneratorVoid(world, world.getSeed(), this);
    }
    
    @ScriptMethodDocumentation(args = "String", usage = "structure file path", notes = "Sets the spawn structure for the dimension, at height 64. The file path is relative to the config/topography/structures folder and does not use the file extension.")
	public void setSpawnStructure(final String structure)
    {
        this.setSpawnStructure(structure, 64);
    }
    
    @ScriptMethodDocumentation(args = "String, int", usage = "structure file path, height", notes = "Sets the spawn structure for the dimension, at the provided height. The file path is relative to the config/topography/structures folder and does not use the file extension.")
	public void setSpawnStructure(final String structure, final int height)
    {
        this.spawnStructure = new SpawnStructure(structure, height);
    }
    
    public SpawnStructure getSpawnStructure()
    {
        return this.spawnStructure;
    }
    
    public void setFogColor(final int color)
    {
    	this.addFogColor(color);
    }
    
    @ScriptMethodDocumentation(args = "int", usage = "color", notes = "Adds a fog color to the dimension with an alpha of 1 and full celestial angle bounds.")
	public void addFogColor(final int color)
    {
        this.addFogColor(color, new MinMaxBounds(0.0F, 1.0F), new MinMaxBounds(1.0F, 1.0F));
    }
    
    @ScriptMethodDocumentation(args = "int, MinMaxBounds, MinMaxBounds", usage = "color, angle bounds, alpha for transition", notes = "Adds a fog color to the dimension with the provided angle and alpha bounds.")
	public void addFogColor(final int color, final MinMaxBounds angle, final MinMaxBounds alpha)
    {
    	if (!this.fog.containsKey(color))
    	{
        	this.fog.put(color, new LinkedHashMap<MinMaxBounds, MinMaxBounds>());
    	}
    	final Map<MinMaxBounds, MinMaxBounds> map = this.fog.get(color);
    	
    	map.put(angle, alpha);
    }
    
    public Map<Integer, Map<MinMaxBounds, MinMaxBounds>> getFog()
    {
    	return this.fog;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Enables enviromental fog. This is the short distance spherical fog used in the nether in vanilla.")
	public void enableEnviromentalFog()
    {
        this.enviromentalFog = true;
    }
    
    public boolean renderEnviromentalFog()
    {
        return this.enviromentalFog;
    }
    
    @ScriptMethodDocumentation(args = "float", usage = "celestial angle", notes = "Sets a static celestial angle for the dimension. Useful for forcing it to always be day or not.")
	public void setCelestialAngle(final float angle)
    {
        this.celestialAngle = angle;
    }
    
    public Float getCelestialAngle()
    {
        return this.celestialAngle;
    }
    
    public boolean renderSky()
    {
        return this.renderSky;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Disables all sky rendering in the dimension. No stars, no sun, no moon, just black.")
	public void disableSky()
    {
        this.renderSky = false;
    }
    
    public boolean renderClouds()
    {
        return this.renderClouds;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Disables cloud rendering.")
	public void disableClouds()
    {
        this.renderClouds = false;
    }
    
    public StructureHandler getStructureHandler()
    {
        return this.structureHandler;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Adds nether fortresses to the dimension with rarity settings approximately the same as vanilla.")
	public void generateNetherFortress()
    {
        this.generateNetherFortress(3);//3 is vanilla rarity
    }
    
    public void generateNetherFortress(final int frequency)
    {
        this.structureHandler.generateNetherFortress(frequency, 16, 4);
    }
    
    @ScriptMethodDocumentation(args = "int, int, int", usage = "spawn chance, area, random area", notes = "Adds nether fortresses to the dimension. Once in every area*area chunk area there is a 1/spawn chance for a nether fortress to spawn,"
    		+ " with a random*random area being used for randomly placing the fortress in the total area. Approximate vanilla values are 3, 16, 4.")
	public void generateNetherFortress(final int frequency, final int totalArea, final int randomArea)
    {
        this.structureHandler.generateNetherFortress(frequency, totalArea, randomArea);
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Adds end cities to the dimension with 3, 16, 4 rarity settings. It's nearly impossible to compare these settings to vanilla, so these are approximately the same as vanilla nether fortress.")
	public void generateEndCity()
    {
        this.generateEndCity(3, 16, 4);
    }
    
    @ScriptMethodDocumentation(args = "int, int, int", usage = "spawn chance, area, random area", notes = "Adds end cities to the dimension. Once in every area*area chunk area there is a 1/spawn chance for an end city to spawn,"
    		+ " with a random*random area being used for randomly placing the city in the total area.")
	public void generateEndCity(final int frequency, final int totalArea, final int randomArea)
    {
        this.structureHandler.generateEndCity(frequency, totalArea, randomArea);
    }
    
    @ScriptMethodDocumentation(args = "Generator", usage = "", notes = "Adds mansions to the dimension, using the supplied Generator to find acceptable spawn locations. It's suggested to use an OverworldGenerator.")
	public void generateMansion(final IGenerator generator)
    {
        this.structureHandler.generateMansion(generator);
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Adds mineshafts to the dimension.")
	public void generateMineshaft()
    {
        this.structureHandler.generateMineshaft();
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Adds ocean monuments to the dimension.")
	public void generateMonument()
    {
        this.structureHandler.generateMonument();
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Adds strongholds to the dimension.")
	public void generateStronghold()
    {
        this.structureHandler.generateStronghold();
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Adds scattered features to the dimension. This includes swamp huts, igloos, desert pyramids and jungle pyramids.")
	public void generateTemple()
    {
        this.structureHandler.generateTemple();
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Adds villages to the dimension.")
	public void generateVillage()
    {
        this.structureHandler.generateVillage();
    }
    
    @ScriptMethodDocumentation(args = "IGenerator", usage = "generator", notes = "Adds the generator to the dimension.")
	public void addGenerator(final IGenerator generator)
    {
        this.generators.add(generator);
    }
    
    public List<IGenerator> getGenerators()
    {
        return this.generators;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Disables the skylight in the dimension. Doesn't effect sky rendering, just makes it not emit light.")
	public void disableSkylight()
    {
        this.skylight = false;
    }
    
    public boolean skylight()
    {
        return this.skylight;
    }
    
    @ScriptMethodDocumentation(usage = "biome id", notes = "Sets a single biome for the dimension to be. Supports all types of biome id format.")
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
    public void setSingleBiome(final int biome)
    {
        this.singleBiome = biome;
    }
    
    public float[] getLightBrightnessTable()
    {
        return this.lightBrightnessTable;
    }
    
    @ScriptMethodDocumentation(args = "float array", usage = "brightness table", notes = "Sets an array used for how bright light appears in the dimension. "
    		+ "Does not effect the actual light level, just how bright that light appers in the rendering. This array must have 16 values between 0 and 1. "
    		+ "In vanilla this is used in the nether to increase the brightness of low light values, making the dimension not as dark while also hiding the large number of lighting errors. "
    		+ "The array used for the nether is [0.1, 0.11578947, 0.13333333, 0.15294117, 0.175, 0.20000002, 0.22857141, 0.26153848, 0.3, 0.34545457, 0.4, 0.46666667, 0.5500001, 0.6571429, 0.79999995, 1.0]")
	public void setLightBrightnessTable(final float[] table)
    {
        this.lightBrightnessTable = table;
    }
    
    public boolean resetRelightChecks()
    {
        return this.resetRelightChecks;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Enables a very large number of relight checks to be done on each chunk after generation is complete, spread out over a small number of ticks. "
    		+ "This causes a very large amount of lag, but is required in dimensions like a lava based nether in order to get the lava to actually emit light.")
	public void enableRelightChecks()
    {
        this.resetRelightChecks = true;
    }
    
    public List<EntityEffect> getEntityEffects()
    {
        return this.entityEffects;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Adds an EntityEffect with a frequency of 0 to the dimension, and returns it.")
	public EntityEffect addEntityEffect()
    {
        return this.addEntityEffect(0);
    }
    
    @ScriptMethodDocumentation(args = "int", usage = "frequency", notes = "Adds an EntityEffect to the dimension which runs every frequency ticks, and returns it.")
	public EntityEffect addEntityEffect(final int frequency)
    {
        final EntityEffect effect = new EntityEffect(frequency);
        this.entityEffects.add(effect);
        return effect;
    }

	public boolean canRespawn() {
		return this.canRespawn;
	}
	
	@ScriptMethodDocumentation(usage = "", notes = "Disables respawning in the dimension.")
	public void disableRespawning() {
		this.canRespawn = false;
	}
	
	@ClientOnly
	public SkyRendererCustom getSkyRenderer()
	{
		return this.skyRenderer;
	}
	
	@ClientOnly
	@ScriptMethodDocumentation(usage = "", notes = "Sets a SkyRendererCustom for the dimension, and returns it.")
	public SkyRendererCustom setSkyRenderer()
	{
		this.skyRenderer = new SkyRendererCustom();
		return this.skyRenderer;
	}
	
	@ScriptMethodDocumentation(usage = "", notes = "If this dimension has a spawn structure set, then when teleporting to the dimension, "
			+ "this option cancels the dimension change and has Topography transfer the player itself, placing them in the spawn position on the spawn structure.")
	public void captureTeleports()
	{
		this.captureTeleports = true;
	}
	
	public boolean shouldCaptureTeleports()
	{
		return this.captureTeleports;
	}
	
	@ScriptMethodDocumentation(usage = "", notes = "This enables the vanilla nether option to have water/ice evaporate when placed in the dimension.")
	public void vaporizeWater()
	{
		this.vaporizeWater = true;
	}
	
	public boolean shouldVaporieWater()
	{
		return this.vaporizeWater;
	}
	
	@ScriptMethodDocumentation(args = "int", usage = "spacing", notes = "Sets the space between spawn structures, in chunks. Default is 65.")
	public void setSpawnStructureSpacing(final int spacing)
	{
		this.spawnStructureSpacing = spacing;
	}
	
	public int getSpawnStructureSpacing()
	{
		return this.spawnStructureSpacing;
	}
	
	public void inheritSkyRenderer()
	{
		this.inheriteSkyRenderer = true;
	}
	
	public boolean shouldInheritSkyRenderer()
	{
		return this.inheriteSkyRenderer;
	}
}
