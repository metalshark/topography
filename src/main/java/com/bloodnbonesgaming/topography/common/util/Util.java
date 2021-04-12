package com.bloodnbonesgaming.topography.common.util;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.features.FeatureHelper;

public class Util {
	public static ClientUtil Client = Topography.proxy.makeClientUtil();
	public static FeatureHelper Feature = new FeatureHelper();
	public static BiomeHelper Biome = new BiomeHelper();
	
}
