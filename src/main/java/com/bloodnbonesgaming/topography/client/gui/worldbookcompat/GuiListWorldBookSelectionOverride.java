package com.bloodnbonesgaming.topography.client.gui.worldbookcompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blamejared.worldbook.client.gui.GuiListWorldBookSelection;
import com.blamejared.worldbook.client.gui.GuiListWorldBookSelectionEntry;
import com.blamejared.worldbook.client.gui.GuiWorldBook;

import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;

public class GuiListWorldBookSelectionOverride extends GuiListWorldBookSelection {
	
    private static final Logger LOGGER = LogManager.getLogger();

	public GuiListWorldBookSelectionOverride(GuiWorldBook p_i46590_1_, Minecraft clientIn, int widthIn, int heightIn,
			int topIn, int bottomIn, int slotHeightIn) {
		super(p_i46590_1_, clientIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
	}
	
	@Override
	public void refreshList() {
		allEntries = new ArrayList<>();
        ISaveFormat isaveformat = this.mc.getSaveLoader();
        List<WorldSummary> list;
        
        try {
            list = isaveformat.getSaveList();
        } catch(AnvilConverterException anvilconverterexception) {
            LOGGER.error("Couldn't load level list", (Throwable) anvilconverterexception);
            this.mc.displayGuiScreen(new GuiErrorScreen(I18n.format("selectWorld.unable_to_load"), anvilconverterexception.getMessage()));
            return;
        }
        
        Collections.sort(list);
        
        for(WorldSummary worldsummary : list) {
            GuiListWorldBookSelectionEntry e = new GuiListWorldBookSelectionEntryOverride(this, worldsummary, this.mc.getSaveLoader());
            this.entries.add(e);
            this.allEntries.add(e);
        }
	}
}
