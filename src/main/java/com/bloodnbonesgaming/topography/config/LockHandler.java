package com.bloodnbonesgaming.topography.config;

import java.util.ArrayList;
import java.util.List;

public class LockHandler
{
    private List<String> unlockedPresets = new ArrayList<String>();
    
    public boolean unlock(final String preset)
    {
        if (!this.unlockedPresets.contains(preset))
        {
            this.unlockedPresets.add(preset);
            return true;
        }
        return false;
    }
    
    public boolean lock(final String preset)
    {
        if (this.unlockedPresets.contains(preset))
        {
            this.unlockedPresets.remove(preset);
            return true;
        }
        return false;
    }
    
    public boolean unlocked(final String preset)
    {
        return this.unlockedPresets.contains(preset);
    }
    
    public String getUnlocks()
    {
        final StringBuilder builder = new StringBuilder();
        
        for (final String preset : this.unlockedPresets)
        {
            builder.append("unlock(\"" + preset + "\")" + System.lineSeparator());
        }
        return builder.toString();
    }
}
