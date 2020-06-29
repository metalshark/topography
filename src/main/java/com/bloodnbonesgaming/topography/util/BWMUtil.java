package com.bloodnbonesgaming.topography.util;

import com.bloodnbonesgaming.topography.world.generator.SkyIslandGeneratorV2;
import com.bloodnbonesgaming.topography.world.generator.structure.BWMSkyIslandMineshaftGenerator;
import com.bloodnbonesgaming.topography.world.generator.structure.BWMSkyIslandVillageGenerator;

import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenVillage;

public class BWMUtil {
	
	public static MapGenVillage getVillage(SkyIslandGeneratorV2 generator) {
		return new BWMSkyIslandVillageGenerator(generator);
	}
	
	public static MapGenMineshaft getMineshaft(SkyIslandGeneratorV2 generator) {
		return new BWMSkyIslandMineshaftGenerator(generator);
	}
}
