package com.bloodnbonesgaming.topography.common.config;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.util.FileHelper;

public class ConfigurationManager {
	
	private static GlobalConfig global;
    private static ExecutorService executor;

	
	public static GlobalConfig getGlobalConfig() {
		return ConfigurationManager.global;
	}
	
	public static ExecutorService getExecutor() {
		if (executor == null) {
	    	executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
				
				private int counter = 0;

				@Override
				public Thread newThread(Runnable r) {
					Thread thread = Executors.defaultThreadFactory().newThread(r);
					thread.setName("Topography-Worker-Thread-" + counter++);
					return thread;
				}
				
			});
		}
		return executor;
	}
	
	public static void init() {
		clean();
		ConfigurationManager.global = new GlobalConfig();
		ConfigurationManager.global.init();
	}
	
	public static void clean() {
		ConfigurationManager.global = null;
	}
	
	private static void CopyDefaultConfigs() {
		//Only print defaults if Topography.js does not exist
		if (!new File(ModInfo.CONFIG_FOLDER + "Topography.js").exists()) {
			Topography.getLog().info("Copying default configs from jar.");
			FileHelper.copyDirectoryFromJar(ConfigurationManager.class, "/defaultconfigs/", ModInfo.CONFIG_FOLDER);
		}
	}
	
	private static void CopyExampleConfigs() {
		String path = ModInfo.CONFIG_FOLDER + "examples/VERSION.txt";
		File versionFile = new File(path);
		
		//TODO test if version in the jar is different
		if (!versionFile.exists()) {
			FileHelper.copyDirectoryFromJar(ConfigurationManager.class, "/exampleconfigs/", ModInfo.CONFIG_FOLDER + "examples/");
		}
	}
}
