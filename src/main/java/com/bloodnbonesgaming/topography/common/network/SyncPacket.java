package com.bloodnbonesgaming.topography.common.network;

import java.util.function.Supplier;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncPacket {
	public String preset;
	
	public SyncPacket setPreset(String preset) {
		this.preset = preset;
		return this;
	}
	
	public static void encode(SyncPacket packet, PacketBuffer buffer) {
		buffer.writeString(packet.preset);
	}
	
	public static SyncPacket decode(PacketBuffer buffer) {
		SyncPacket packet = new SyncPacket();
		packet.preset = buffer.readString();
		return packet;
	}
	
	public static void handleMsg(SyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Topography.getLog().info("Read syncpacket " + packet.preset);
			ConfigurationManager.getGlobalConfig().init();
			ConfigurationManager.getGlobalConfig().setPreset(packet.preset);
			if (ConfigurationManager.getGlobalConfig().getPreset() != null) {
				ConfigurationManager.getGlobalConfig().getPreset().readDimensionDefs();
			}
		});
	}
}
