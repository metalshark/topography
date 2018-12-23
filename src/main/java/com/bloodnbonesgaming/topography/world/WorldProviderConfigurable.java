package com.bloodnbonesgaming.topography.world;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.client.renderer.CloudRendererDisabled;
import com.bloodnbonesgaming.topography.client.renderer.SkyRendererCustom;
import com.bloodnbonesgaming.topography.client.renderer.SkyRendererDisabled;
import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderConfigurable extends WorldProvider
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
//        this.nether = true;
//        this.hasSkyLight = false;
        
//        if (this.getDimension() != 0)
//        {
//            final World world = DimensionManager.getWorld(0);
//            this.generatorSettings = world.getWorldInfo().getGeneratorOptions();
//        }
        this.type = DimensionManager.getProviderType(this.getDimension());
        this.hasSkyLight = true;

        Topography.instance.getLog().info("GenSettings: " +  this.generatorSettings);
        final ConfigPreset preset = ConfigurationManager.getInstance().getPreset();
        this.generatorSettings = preset.getName();
        
        Topography.instance.getLog().info("Preset: " + preset.getName());
//        new Exception().printStackTrace();
        if (preset != null)
        {
            String script = preset.getScript(this.getDimension());
            this.definition = new DimensionDefinition();
            IOHelper.loadDimensionDefinition(script, definition);
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
                }
            }
        }
        else
        {
            this.biomeProvider = new BiomeProvider(this.world.getWorldInfo());
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
//    	final Map<Integer, Map<MinMaxBounds, MinMaxBounds>> fogMap = this.definition.getFog();
//    	
//    	if (fogMap != null)
//    	{
//    		
//    		
//    		
//    		
//    		
//    		Vec3d color = new Vec3d(0, 0, 0);
//    		float totalAlpha = 0;
//    		
//    		for (final Entry<Integer, Map<MinMaxBounds, MinMaxBounds>> fog : fogMap.entrySet())
//    		{
//    			float alpha = 0.0F;
//    			
//    			for (final Entry<MinMaxBounds, MinMaxBounds> entry : fog.getValue().entrySet())
//    			{
//    				if (entry.getKey().test(celestialAngle))
//    				{
//    					final MinMaxBounds key = entry.getKey();
//    					final MinMaxBounds value = entry.getValue();
//    					
//    					if (value.min != null && value.max != null)
//    					{
//    						float diff = key.max - key.min;
//    						float distIntoRange = celestialAngle - key.min;
//    						float percent = distIntoRange / diff;
//    						
//    						if (value.min > value.max)
//    						{
//    							float alphaDiff = value.min - value.max;
//    							alpha = value.min - alphaDiff * percent;
//    						}
//    						else
//    						{
//    							float alphaDiff = value.max - value.min;
//    							alpha = value.min + alphaDiff * percent;
//    						}
//    						break;
//    					}
//    				}
//    			}
//    			float remaining = 1.0F - totalAlpha;
//    			color = color.addVector(((((fog.getKey() >> 16) & 255) / 255F) * alpha) * remaining, ((((fog.getKey() >> 8) & 255) / 255F) * alpha) * remaining, (((fog.getKey() & 255) / 255F) * alpha) * remaining);
//    			totalAlpha += (alpha * remaining);
//    			
//    			if (alpha == 1.0F)
//    			{
//    				break;
//    			}
//    		}
//    		return color;
//    	}
//        final Integer fogColor = this.definition.getFogColor();
//        
//        if (fogColor != null)
//        {
//            return new Vec3d(((fogColor >> 16) & 255) / 255F, ((fogColor >> 8) & 255) / 255F, (fogColor & 255) / 255F);
//        }
        
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
    
//    @Override
//    @Nullable
//    @SideOnly(Side.CLIENT)
//    public net.minecraftforge.client.IRenderHandler getSkyRenderer()
//    {
//        if (!this.definition.renderSky())
//            return SkyRendererDisabled.instance;
//        else
//        {
//        	final SkyRendererCustom renderer = this.definition.getSkyRenderer();
//        	
//        	if (renderer != null)
//        	{
//        		return renderer;
//        	}
//        }
//        return super.getSkyRenderer();
//    }
    
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
}
