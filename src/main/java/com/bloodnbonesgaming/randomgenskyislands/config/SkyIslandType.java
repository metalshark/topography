package com.bloodnbonesgaming.randomgenskyislands.config;

import com.bloodnbonesgaming.lib.util.data.ItemBlockData;
import com.bloodnbonesgaming.lib.util.script.ArgType;
import com.bloodnbonesgaming.lib.util.script.ScriptArgs;

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
    
    public SkyIslandType()
    {
        
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
//    public void setGenBiomeBlocks(boolean genBiomeBlocks)
//    {
//        this.genBiomeBlocks = genBiomeBlocks;
//    }
    public boolean isGenDecorations()
    {
        return genDecorations;
    }
//    public void setGenDecorations(boolean genDecorations)
//    {
//        this.genDecorations = genDecorations;
//    }
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
}
