package com.bloodnbonesgaming.topography.config;

import com.bloodnbonesgaming.topography.config.definitions.SkyIslandDefinition;
import com.bloodnbonesgaming.topography.config.definitions.SuperflatDefinition;
import com.bloodnbonesgaming.topography.config.definitions.VoidDefinition;

public enum DimensionTypes
{
    SKY_ISLANDS {

        @Override
        protected DimensionDefinition getDefinition()
        {
            return new SkyIslandDefinition();
        }
        
    },
    VOID {

        @Override
        protected DimensionDefinition getDefinition()
        {
            return new VoidDefinition();
        }
        
    },
    SUPERFLAT {

        @Override
        protected DimensionDefinition getDefinition()
        {
            return new SuperflatDefinition();
        }
        
    };
    
    protected abstract DimensionDefinition getDefinition();
    
    public static DimensionTypes getType(final String name)
    {
        return DimensionTypes.valueOf(name.toUpperCase());
    }
    
    public static DimensionDefinition getDefinition(final String name)
    {
        final DimensionTypes type = DimensionTypes.getType(name);
        
        if (type != null)
        {
            return type.getDefinition();
        }
        return null;
    }
}