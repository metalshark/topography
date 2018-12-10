package com.bloodnbonesgaming.topography.client.gui;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiListWorldSelection;
import net.minecraft.client.gui.GuiListWorldSelectionEntry;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;

public class GuiListWorldSelectionOverride extends GuiListWorldSelection {

	public GuiListWorldSelectionOverride(GuiWorldSelection p_i46590_1_, Minecraft clientIn, int widthIn, int heightIn,
			int topIn, int bottomIn, int slotHeightIn) {
		super(p_i46590_1_, clientIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
	}
	
	@Override
	public void refreshList() {
		ISaveFormat isaveformat = this.mc.getSaveLoader();
        List<WorldSummary> list;

        try
        {
            list = isaveformat.getSaveList();
        }
        catch (AnvilConverterException anvilconverterexception)
        {
//            LOGGER.error("Couldn't load level list", (Throwable)anvilconverterexception);
            this.mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load"), anvilconverterexception.getMessage()));
            return;
        }

        Collections.sort(list);

        for (WorldSummary worldsummary : list)
        {
            this.entries.add(new GuiListWorldSelectionEntryOverride(this, worldsummary, this.mc.getSaveLoader()));
        }
	}
}
