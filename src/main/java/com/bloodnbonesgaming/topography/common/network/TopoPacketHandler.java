package com.bloodnbonesgaming.topography.common.network;

import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class TopoPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModInfo.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int msgID = 0;
	
	public static void init() {
		INSTANCE.registerMessage(msgID++, SyncPacket.class, SyncPacket::encode, SyncPacket::decode, SyncPacket::handleMsg);
	}
}
