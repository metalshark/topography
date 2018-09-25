package com.bloodnbonesgaming.topography.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ExtendedDamageSource extends DamageSource
{
    private final String deathMessage;

    public ExtendedDamageSource(String damageTypeIn)
    {
        this(damageTypeIn, null);
    }

    public ExtendedDamageSource(String damageTypeIn, String deathMessage)
    {
        super(damageTypeIn);
        this.deathMessage = deathMessage;
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entity)
    {
        if (this.deathMessage != null)
        {
            return new TextComponentString(this.deathMessage.replaceAll("\\@p", entity.getDisplayName().getFormattedText()));
        }
        else
        {
            return super.getDeathMessage(entity);
        }
    }
}
