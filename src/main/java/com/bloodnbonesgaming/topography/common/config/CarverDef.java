package com.bloodnbonesgaming.topography.common.config;

import com.bloodnbonesgaming.topography.common.world.gen.IGenerator;

import net.minecraft.world.gen.carver.ConfiguredCarver;

public class CarverDef {
	
	private ConfiguredCarver<?> carver = null;
	private IGenerator generator = null;
	
	public CarverDef(ConfiguredCarver<?> carver) {
		this.carver = carver;
	}
	
	public CarverDef(IGenerator generator) {
		this.generator = generator;
	}
	
	public void generate() {
		if (this.carver != null) {
			
		}
	}
}
