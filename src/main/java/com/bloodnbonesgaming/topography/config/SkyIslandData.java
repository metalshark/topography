package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bloodnbonesgaming.lib.util.script.ScriptClassDocumentation;
import com.bloodnbonesgaming.lib.util.script.ScriptMethodDocumentation;
import com.bloodnbonesgaming.topography.ModInfo;

@ScriptClassDocumentation(documentationFile = ModInfo.SKY_ISLANDS_DOCUMENTATION_FOLDER + "SkyIslandData", classExplaination = 
"This file is for the SkyIslandData. This data object is for holding size and type data of islands for the SkyIslandGenerator.")
public class SkyIslandData
{
    private int count = 1;
    private double horizontalRadius = 100;
    private double verticalRadius = 100;
    private double topHeight = verticalRadius;
    private double bottomHeight = verticalRadius;
    private double waterHeight = -1;
    private List<SkyIslandType> types = new ArrayList<SkyIslandType>();
    private boolean randomTypes = true;
    
    private int minCount = 0;
    
    
    public int getCount()
    {
        return count;
    }
    
	public void setCount(int count)
    {
        this.count = count;
    }
	
    public double getHorizontalRadius()
    {
        return horizontalRadius;
    }
    
	public void setHorizontalRadius(int radius)
    {
        this.horizontalRadius = radius;
    }
	
//    public double getVerticalRadius()
//    {
//        return verticalRadius;
//    }
    
	public void setVerticalRadius(int radius)
    {
        this.verticalRadius = radius;
        this.topHeight = radius;
        this.bottomHeight = radius;
    }
	
	public double getTopHeight()
	{
		return this.topHeight;
	}
	
	public void setTopHeight(double height)
	{
		this.topHeight = height;
	}
	
	public double getBottomHeight()
	{
		return this.bottomHeight;
	}
	
	public void setBottomHeight(double height)
	{
		this.bottomHeight = height;
	}
	
	public double getWaterHeight()
	{
		if (this.waterHeight < 0)
		{
	        waterHeight = Math.floor(this.horizontalRadius / 20D);
		}
		return this.waterHeight;
	}
	
	public void setWaterHeight(final double height)
	{
		this.waterHeight = height;
	}
    
    @ScriptMethodDocumentation(args = "SkyIslandType", usage = "type", notes = "Adds a type of sky island to be generated.")
	public void addType(final SkyIslandType type)
    {
        this.types.add(type);
    }
    
    public SkyIslandType getType(final int index)
    {
        return this.types.get(index % types.size());
    }
    
    public SkyIslandType getType(final Random rand)
    {
        return this.types.get(rand.nextInt(this.types.size()));
    }
    
    public boolean isRandomIslands()
    {
        return randomTypes;
    }
    
    public void setRandomTypes(boolean randomTypes)
    {
        this.randomTypes = randomTypes;
    }
    
    public int getMinCount()
    {
        return this.minCount;
    }
    
    public void setMinCount(final int count)
    {
        this.minCount = count;
    }
}
