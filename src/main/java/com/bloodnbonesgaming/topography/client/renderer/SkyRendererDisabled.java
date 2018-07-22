package com.bloodnbonesgaming.topography.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

public class SkyRendererDisabled extends IRenderHandler
{
    public static final SkyRendererDisabled instance = new SkyRendererDisabled();

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
        
    }

}
