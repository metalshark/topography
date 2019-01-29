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
    private int radius = 100;
    private List<SkyIslandType> types = new ArrayList<SkyIslandType>();
    private boolean randomTypes = true;
    
    private int minCount = 0;
    
    
    public int getCount()
    {
        return count;
    }
    
//    @ScriptMethodDocumentation(args = "int", usage = "count", notes = "Sets the number of times to attempt to generate an island of this size per region.")
	public void setCount(int count)
    {
        this.count = count;
    }
    public int getRadius()
    {
        return radius;
    }
    
//    @ScriptMethodDocumentation(args = "int", usage = "radius", notes = "Sets the radius of the sky islands to be generated.")
	public void setRadius(int radius)
    {
        this.radius = radius;
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
