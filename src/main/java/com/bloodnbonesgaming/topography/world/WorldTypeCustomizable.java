package com.bloodnbonesgaming.topography.world;

import com.bloodnbonesgaming.topography.client.gui.GuiCustomizeWorldType;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldTypeCustomizable extends WorldType
{

    public WorldTypeCustomizable(String name)
    {
        super(name);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void onCustomizeButton(net.minecraft.client.Minecraft mc, net.minecraft.client.gui.GuiCreateWorld guiCreateWorld)
    {
        mc.displayGuiScreen(new GuiCustomizeWorldType(guiCreateWorld));
    }

    @Override
    public boolean isCustomizable()
    {
        return true;
    }
    
    @Override
    public int getSpawnFuzz(WorldServer world, MinecraftServer server)
    {
        return 0;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public String getTranslationKey()
    {
        return this.getName();
    }
}