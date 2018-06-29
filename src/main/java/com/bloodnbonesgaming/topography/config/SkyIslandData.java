package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkyIslandData
{
    private int count = 1;
    private int radius = 100;
    private List<SkyIslandType> types = new ArrayList<SkyIslandType>();
    private boolean randomTypes = true;
    
    
    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }
    public int getRadius()
    {
        return radius;
    }
    public void setRadius(int radius)
    {
        this.radius = radius;
    }
    
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
}
