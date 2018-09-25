package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.topography.world.StructureHandler;
import com.bloodnbonesgaming.topography.world.biome.provider.BiomeProviderConfigurable;
import com.bloodnbonesgaming.topography.world.chunkgenerator.ChunkGeneratorVoid;
import com.bloodnbonesgaming.topography.world.decorator.DecoratorScattered;
import com.bloodnbonesgaming.topography.world.generator.CellInterpolationTestGenerator;
import com.bloodnbonesgaming.topography.world.generator.CellNoiseGenerator;
import com.bloodnbonesgaming.topography.world.generator.DeformedSphereGenerator;
import com.bloodnbonesgaming.topography.world.generator.FluidPocketGenerator;
import com.bloodnbonesgaming.topography.world.generator.HangingCrystalGenerator;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.generator.LayerGenerator;
import com.bloodnbonesgaming.topography.world.generator.ScatteredBlockGenerator;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaFireGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaGlowstoneGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaLavaPocketGenerator;
import com.bloodnbonesgaming.topography.world.generator.vanilla.VanillaQuartzGenerator;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class DimensionDefinition
{
    public final Map<String, Class> classKeywords = new HashMap<String, Class>();
    private String spawnStructure;
    private Integer fogColor;
    private boolean enviromentalFog = false;
    private Float celestialAngle;
    private boolean renderSky = true;
    private boolean renderClouds = true;
    private final List<IGenerator> generators = new ArrayList<IGenerator>();
    private boolean skylight = true;
    private float[] lightBrightnessTable = null;
    private boolean resetRelightChecks = false;
    private final List<EntityEffect> entityEffects = new ArrayList<EntityEffect>();
    
    
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
    
    public void setSpawnStructure(final String structure)
    {
        this.spawnStructure = structure;
    }
    
    public String getSpawnStructure()
    {
        return this.spawnStructure;
    }
    
    public void setFogColor(final int color)
    {
        this.fogColor = color;
    }
    
    public Integer getFogColor()
    {
        return this.fogColor;
    }
    
    public void enableEnviromentalFog()
    {
        this.enviromentalFog = true;
    }
    
    public boolean renderEnviromentalFog()
    {
        return this.enviromentalFog;
    }
    
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
    
    public void disableSky()
    {
        this.renderSky = false;
    }
    
    public boolean renderClouds()
    {
        return this.renderClouds;
    }
    
    public void disableClouds()
    {
        this.renderClouds = false;
    }
    
    public StructureHandler getStructureHandler()
    {
        return this.structureHandler;
    }
    
    public void generateNetherFortress()
    {
        this.structureHandler.generateNetherFortress();
    }
    
    public void addGenerator(final IGenerator generator)
    {
        this.generators.add(generator);
    }
    
    public List<IGenerator> getGenerators()
    {
        return this.generators;
    }
    
    public void disableSkylight()
    {
        this.skylight = false;
    }
    
    public boolean skylight()
    {
        return this.skylight;
    }
    
    @ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
    public void setSingleBiome(final int biome)
    {
        this.singleBiome = biome;
    }
    
    public float[] getLightBrightnessTable()
    {
        return this.lightBrightnessTable;
    }
    
    public void setLightBrightnessTable(final float[] table)
    {
        this.lightBrightnessTable = table;
    }
    
    public boolean resetRelightChecks()
    {
        return this.resetRelightChecks;
    }
    
    public void enableRelightChecks()
    {
        this.resetRelightChecks = true;
    }
    
    public List<EntityEffect> getEntityEffects()
    {
        return this.entityEffects;
    }
    
    public EntityEffect addEntityEffect()
    {
        return this.addEntityEffect(0);
    }
    
    public EntityEffect addEntityEffect(final int frequency)
    {
        final EntityEffect effect = new EntityEffect(frequency);
        this.entityEffects.add(effect);
        return effect;
    }
}
