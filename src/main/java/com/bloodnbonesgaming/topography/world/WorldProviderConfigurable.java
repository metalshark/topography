package com.bloodnbonesgaming.topography.world;

import javax.annotation.Nullable;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.client.renderer.CloudRendererDisabled;
import com.bloodnbonesgaming.topography.client.renderer.SkyRendererDisabled;
import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
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
        
        if (this.getDimension() != 0)
        {
            final World world = DimensionManager.getWorld(0);
            this.generatorSettings = world.getWorldInfo().getGeneratorOptions();
        }
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
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
    {
        final Integer fogColor = this.definition.getFogColor();
        
        if (fogColor != null)
        {
            return new Vec3d(((fogColor >> 16) & 255) / 255F, ((fogColor >> 8) & 255) / 255F, (fogColor & 255) / 255F);
        }
        
        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
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
    public net.minecraftforge.client.IRenderHandler getSkyRenderer()
    {
        if (!this.definition.renderSky())
            return SkyRendererDisabled.instance;
        return super.getSkyRenderer();
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
}
