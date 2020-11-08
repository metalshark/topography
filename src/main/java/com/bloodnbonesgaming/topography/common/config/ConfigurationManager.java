package com.bloodnbonesgaming.topography.common.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
}
