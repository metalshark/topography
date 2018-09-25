package com.bloodnbonesgaming.topography.util;

import net.minecraft.util.DamageSource;

public enum EDamageSource
{
    IN_FIRE(DamageSource.IN_FIRE),
    LIGHTNING_BOLT(DamageSource.LIGHTNING_BOLT),
    ON_FIRE(DamageSource.ON_FIRE),
    LAVA(DamageSource.LAVA),
    HOT_FLOOR(DamageSource.HOT_FLOOR),
    IN_WALL(DamageSource.IN_WALL),
    CRAMMING(DamageSource.CRAMMING),
    DROWN(DamageSource.DROWN),
    STARVE(DamageSource.STARVE),
    CACTUS(DamageSource.CACTUS),
    FALL(DamageSource.FALL),
    FLY_INTO_WALL(DamageSource.FLY_INTO_WALL),
    OUT_OF_WORLD(DamageSource.OUT_OF_WORLD),
    GENERIC(DamageSource.GENERIC),
    MAGIC(DamageSource.MAGIC),
    WITHER(DamageSource.WITHER),
    ANVIL(DamageSource.ANVIL),
    FALLING_BLOCK(DamageSource.FALLING_BLOCK),
    DRAGON_BREATH(DamageSource.DRAGON_BREATH);
    
    
    private final DamageSource source;
    
    private EDamageSource(final DamageSource source)
    {
        this.source = source;
    }
    
    public DamageSource getSource()
    {
        return this.source;
    }
}
