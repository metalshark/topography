package com.bloodnbonesgaming.topography.common.util.scripts;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.scripts.java15.NashornHelper15;

public class ScriptUtil {
	
	public static NashornHelperBase getNashornHelper() {
		if (java15OrHigher()) {
			return new NashornHelper15();
		} else {
			
		}
		return null;
	}
	
	private static boolean java15OrHigher() {
		try {
			String version = System.getProperty("java.version");
			String[] split = version.split(".");
			
			if (Integer.parseInt(split[1]) >= 15) {
				return true;
			}
		} catch(Exception e) {
			
		}
		return false;
	}
}
