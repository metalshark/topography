package com.bloodnbonesgaming.topography.client.gui;

import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;

public class GuiListWorldSelectionEntryOverride extends GuiListWorldSelectionEntry {

	public GuiListWorldSelectionEntryOverride(GuiListWorldSelection listWorldSelIn, WorldSummary worldSummaryIn,
			ISaveFormat saveFormat) {
		super(listWorldSelIn, worldSummaryIn, saveFormat);
	}
	
	@Override
	public void recreateWorld() {
		final Minecraft minecraft = Minecraft.getMinecraft();
		minecraft.displayGuiScreen(new GuiScreenWorking());
		
        GuiCreateWorld guicreateworld = new GuiCreateWorldTopography(this.worldSelScreen);
        ISaveHandler isavehandler = this.client.getSaveLoader().getSaveLoader(this.worldSummary.getFileName(), false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        isavehandler.flush();

        if (worldinfo != null)
        {
        	WorldTypeCustomizable.gui = guicreateworld;
            minecraft.displayGuiScreen(guicreateworld);
            guicreateworld.recreateFromExistingWorld(worldinfo);
        }
	}
}
