package com.bloodnbonesgaming.topography.client.gui.element;

public abstract class GuiElementBase {
	
	protected final EnumGuiLocation location;
	protected double relXOffset = 0;
	protected double relYOffset = 0;
	protected int absXOffset = 0;
	protected int absYOffset = 0;

	public GuiElementBase(final EnumGuiLocation location) {
		this.location = location;
	}

	public void setRelXOffset(final double offset) {
		this.relXOffset = offset;
	}

	public void setRelYOffset(final double offset) {
		this.relYOffset = offset;
	}

	public void setAbsXOffset(final int offset) {
		this.absXOffset = offset;
	}

	public void setAbsYOffset(final int offset) {
		this.absYOffset = offset;
	}
}
