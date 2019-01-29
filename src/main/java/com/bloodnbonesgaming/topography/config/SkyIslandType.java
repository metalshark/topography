package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;
import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.world.decorator.DecorationData;
import com.bloodnbonesgaming.topography.world.decorator.DecoratorScattered;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

@ScriptClassDocumentation(documentationFile = ModInfo.SKY_ISLANDS_DOCUMENTATION_FOLDER + "SkyIslandType", classExplaination = 
"This file is for the SkyIslandType. This data object is for creating island types for the SkyIslandGenerator. "
+ "These can be created in a dimension file using 'new SkyIslandType(biomeID)', with the biome id being the biome you want the island to be, or 'new SkyIslandType()' to default to the void biome.")
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
    
    @ScriptMethodDocumentation(usage = "biome id", notes = "Sets the biome to be used for the island type.")
	@ScriptArgs(args = {ArgType.NON_NULL_BIOME_ID})
    public void setBiome(int biome)
    {
        this.biome = biome;
    }
    
    @ScriptMethodDocumentation(args = "ItemBlockData", usage = "block", notes = "Sets the main block for the island type to be made of.")
	public void setMainBlock(final ItemBlockData data) throws Exception
    {
        this.mainBlock = data.buildBlockState();
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Disables biome block replacement for the island type.")
	public void disableBiomeBlockReplacement()
    {
        this.genBiomeBlocks = false;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Disables decorations for the island type.")
	public void disableDecorations()
    {
        this.genDecorations = false;
    }
    
    @ScriptMethodDocumentation(usage = "", notes = "Disables initial animal spawning for the island type.")
	public void disableAnimals()
    {
        this.genAnimals = false;
    }
    
    @ScriptMethodDocumentation(args = "ItemBlockData, MinMaxBounds", usage = "block, bounds", notes = "Sets a block to be used for generation within a range of distance from the center of the island type. "
    		+ "0.0 being the center of the island, 1.0 being the outermost possible blocks.")
	public void setBlockInRange(final ItemBlockData data, final MinMaxBounds bounds) throws Exception
    {
        final IBlockState state = data.buildBlockState();
        
        this.boundsToState.put(bounds, state);
    }

    public Map<MinMaxBounds, IBlockState> getBoundsToStateMap()
    {
        return boundsToState;
    }
    
    //TODO Make something better than this.
    public void addDecorator(final DecoratorScattered decorator, final int count, final int regionSize)
    {
        this.decorators.add(new DecorationData(decorator, count, regionSize));
    }
    
    public List<DecorationData> getDecorators()
    {
        return this.decorators;
    }
}
