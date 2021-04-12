package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.common.util.ClientUtil;
import com.bloodnbonesgaming.topography.dedicated.ServerEventHandler;

import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy {

//	@Override
//	public Impl getRegistries() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
	}

	@Override
	public ClientUtil makeClientUtil() {
		return null;
	}
}
