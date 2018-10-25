package com.bloodnbonesgaming.topography.event;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.network.PacketSyncPreset;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.netty.channel.ChannelFutureListener;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import net.minecraftforge.fml.relauncher.Side;

public class ServerEventSubscriber
{
    @SubscribeEvent
    public void onPlayerJoinServer(final ServerConnectionFromClientEvent event) {
    	
    	String settings = ((DedicatedServer) FMLCommonHandler.instance().getMinecraftServerInstance()).getStringProperty("generator-settings", "");
        
        if (!settings.isEmpty())
        {
            final JsonParser parser = new JsonParser();
            Topography.instance.getLog().info("reading json " + settings);
            JsonElement element = parser.parse(settings);
            if (element.isJsonObject())
            {
                JsonObject obj = (JsonObject) element;
                JsonElement member = obj.get("Topography-Preset");
                if (member != null)
                {
                    settings = member.getAsString();
                    FMLEmbeddedChannel channel = NetworkRegistry.INSTANCE.getChannel(ModInfo.MODID, Side.SERVER);
                    channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DISPATCHER);
                    channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(NetworkDispatcher.get(event.getManager()));
                    channel.writeAndFlush(new PacketSyncPreset()).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            }
        }
    }
}
