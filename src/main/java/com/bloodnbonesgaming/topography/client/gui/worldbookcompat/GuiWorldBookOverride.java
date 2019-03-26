package com.bloodnbonesgaming.topography.client.gui.worldbookcompat;

import com.blamejared.worldbook.client.gui.GuiWorldBook;

import net.minecraft.client.gui.GuiScreen;

public class GuiWorldBookOverride extends GuiWorldBook {

	public GuiWorldBookOverride(GuiScreen screenIn) {
		super(screenIn);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.selectionList = new GuiListWorldBookSelectionOverride(this, this.mc, this.width, this.height, 60, this.height - 64, 36);
	}
}
