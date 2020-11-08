package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.common.CommonEventHandler;
import com.bloodnbonesgaming.topography.common.util.RegistryHelper;

import net.minecraft.util.registry.DynamicRegistries;
import net.minecraftforge.common.MinecraftForge;

public abstract class CommonProxy {
	
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
	}
	
	public void setup() {
		
	}
	
	public DynamicRegistries.Impl getRegistries() {
		return RegistryHelper.getRegistry();
	}
}
