package com.bloodnbonesgaming.topography.util;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;

public enum EntityType
{
    ANIMAL {
        @Override
        public boolean isAcceptable(EntityLivingBase entity)
        {
            return entity instanceof EntityAnimal;
        }
    },
    MOB {
        @Override
        public boolean isAcceptable(EntityLivingBase entity)
        {
            return entity instanceof IMob;
        }
    },
    CREATURE {
        @Override
        public boolean isAcceptable(EntityLivingBase entity)
        {
            return entity instanceof EntityCreature;
        }
    };
    
    public abstract boolean isAcceptable(final EntityLivingBase entity);
}
