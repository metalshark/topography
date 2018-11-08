package com.bloodnbonesgaming.topography.util;

public class SpawnStructure {
	
	final String structure;
	final int height;
	
	public SpawnStructure(final String structure, final int height)
	{
		this.structure = structure;
		this.height = height;
	}
	
	public String getStructure() {
		return structure;
	}

	public int getHeight() {
		return height;
	}
}
