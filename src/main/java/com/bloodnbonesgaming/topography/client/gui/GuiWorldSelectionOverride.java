package com.bloodnbonesgaming.topography.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.resources.I18n;

public class GuiWorldSelectionOverride extends GuiWorldSelection {

	public GuiWorldSelectionOverride(GuiScreen screenIn) {
		super(screenIn);
	}
	
	@Override
	public void initGui() {
		this.title = I18n.format("selectWorld.title");
        this.selectionList = new GuiListWorldSelectionOverride(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
        this.postInit();
	}
}
