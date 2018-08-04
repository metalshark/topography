package com.bloodnbonesgaming.topography.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;

public class CloudRendererDisabled extends IRenderHandler
{
    public static final CloudRendererDisabled instance = new CloudRendererDisabled();

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
        
    }

}
