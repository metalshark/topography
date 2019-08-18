package com.bloodnbonesgaming.topography.world;

import javax.annotation.Nullable;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.client.renderer.CloudRendererDisabled;
import com.bloodnbonesgaming.topography.client.renderer.SkyRendererCustom;
import com.bloodnbonesgaming.topography.client.renderer.SkyRendererDisabled;
import com.bloodnbonesgaming.topography.client.renderer.TopographyWeatherRenderer;
import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderConfigurableSurface extends WorldProviderSurface
{
    private DimensionType type;
    private DimensionDefinition definition;

    @Override
    public DimensionType getDimensionType()
    {
        return this.type;
    }
    
    @Override
    protected void init()
    {
        this.type = DimensionManager.getProviderType(this.getDimension());
        this.hasSkyLight = true;

        Topography.instance.getLog().info("GenSettings: " +  this.generatorSettings);
        final ConfigPreset preset = ConfigurationManager.getInstance().getPreset();
        this.generatorSettings = preset.getName();
        
        Topography.instance.getLog().info("Preset: " + preset.getName());

        if (preset != null)
        {
        	this.definition = preset.getDefinition(this.getDimension());
            this.biomeProvider = this.definition.getBiomeProvider(this.world);
            this.hasSkyLight = this.definition.skylight();
            this.doesWaterVaporize = this.definition.shouldVaporieWater();
            
            if (this.world.isRemote)
            {
            	if (!this.definition.renderSky())
                    this.setSkyRenderer(SkyRendererDisabled.instance);
                else
                {
                	final SkyRendererCustom renderer = this.definition.getSkyRenderer();
                	
                	if (renderer != null)
                	{
                		this.setSkyRenderer(renderer);
                	}
                	
                	final TopographyWeatherRenderer weather = this.definition.getWeatherRenderer();
                	
                	if (weather != null)
                	{
                		this.setWeatherRenderer(weather);
                	}
                }
            }
        }
    }
    
    public DimensionDefinition getDefinition()
    {
        return this.definition;
    }
    
    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return this.definition.getChunkGenerator(this.world);
    }
    
    
    
    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float celestialAngle, float p_76562_2_)
    {
        float f = MathHelper.cos(celestialAngle * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d((double)f1, (double)f2, (double)f3);
    }
    
    @Override
    public boolean doesXZShowFog(int x, int z)
    {
        return this.definition.renderEnviromentalFog();
    }
    
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        final Float angle = this.definition.getCelestialAngle();
        
        if (angle != null)
        {
            return angle;
        }
        return super.calculateCelestialAngle(worldTime, partialTicks);
    }
    
    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public IRenderHandler getCloudRenderer()
    {
        if (!this.definition.renderClouds())
            return CloudRendererDisabled.instance;
        return super.getCloudRenderer();
    }

    @Override
    protected void generateLightBrightnessTable()
    {
        final float[] table = this.definition.getLightBrightnessTable();
        
        if (table != null)
        {
            for (int i = 0; i <= 15; ++i)
            {
                this.lightBrightnessTable[i] = table[i];
            }
        }
        else
        {
            super.generateLightBrightnessTable();
        }
    }
    
    @Override
    public boolean shouldClientCheckLighting()
    {
        return this.definition.resetRelightChecks();
    }
    
    @Override
    public boolean canRespawnHere() {
    	return this.definition.canRespawn();
    }
    
    @Override
    public BlockPos getSpawnCoordinate() {
    	if (this.getDimension() == 1)
    	{
    		return new BlockPos(100, 50, 0);
    	}
    	return null;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public float getCloudHeight() {
    	float height = this.definition.getCloudHeight();
    	
    	if (height != -999)
    	{
    		return height;
    	}
    	return super.getCloudHeight();
    }
    
    @Override
    public boolean canDropChunk(int x, int z)
    {
        return !this.world.provider.getDimensionType().shouldLoadSpawn() || !this.world.isSpawnChunk(x, z);
    }
}
