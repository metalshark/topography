package com.bloodnbonesgaming.topography.util;

import net.minecraft.world.DimensionType;
import rtg.api.RTGAPI;

public class RTGUtil {
	
	public static void addAllowedDimensionType(DimensionType type) {
		RTGAPI.addAllowedDimensionType(type);
	}
	public static void removeAllowedDimensionType(DimensionType type) {
		RTGAPI.removeAllowedDimensionType(type);
	}
}
