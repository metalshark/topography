package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.topography.world.decorator.DecorationData;
import com.bloodnbonesgaming.topography.world.decorator.DecoratorScattered;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class SkyIslandType
{
    private IBlockState mainBlock = Blocks.STONE.getDefaultState();
    private boolean genBiomeBlocks = true;
    private boolean genDecorations = true;
    private int biome = Biome.getIdForBiome(Biomes.VOID);
    private final Map<MinMaxBounds, IBlockState> boundsToState = new LinkedHashMap<MinMaxBounds, IBlockState>();
    private boolean genAnimals = true;
    private final List<DecorationData> decorators = new ArrayList<DecorationData>();
    
    public SkyIslandType()
    {
//        this.decorators.put(new DecoratorScattered(Blocks.BEDROCK.getDefaultState()), 64);
    }
    
    @ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
    public SkyIslandType(final int biome)
    {
        this();
        this.biome = biome;
    }
    
    public IBlockState getMainBlock()
    {
        return mainBlock;
    }
    public void setMainBlock(IBlockState mainBlock)
    {
        this.mainBlock = mainBlock;
    }
    public boolean isGenBiomeBlocks()
    {
        return genBiomeBlocks;
    }
    
    public boolean isGenDecorations()
    {
        return genDecorations;
    }
    
    public boolean genAnimals()
    {
        return this.genAnimals;
    }
    
    public int getBiome()
    {
        return biome;
    }
    
    @ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
    public void setBiome(int biome)
    {
        this.biome = biome;
    }
    
    public void setMainBlock(final ItemBlockData data) throws Exception
    {
        this.mainBlock = data.buildBlockState();
    }
    
    public void disableBiomeBlockReplacement()
    {
        this.genBiomeBlocks = false;
    }
    
    public void disableDecorations()
    {
        this.genDecorations = false;
    }
    
    public void disableAnimals()
    {
        this.genAnimals = false;
    }
    
    public void setBlockInRange(final ItemBlockData data, final MinMaxBounds bounds) throws Exception
    {
        final IBlockState state = data.buildBlockState();
        
        this.boundsToState.put(bounds, state);
    }

    public Map<MinMaxBounds, IBlockState> getBoundsToStateMap()
    {
        return boundsToState;
    }
    
    public void addDecorator(final DecoratorScattered decorator, final int count, final int regionSize)
    {
        this.decorators.add(new DecorationData(decorator, count, regionSize));
    }
    
    public List<DecorationData> getDecorators()
    {
        return this.decorators;
    }
}
