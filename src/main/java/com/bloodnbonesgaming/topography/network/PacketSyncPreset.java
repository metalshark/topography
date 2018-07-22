package com.bloodnbonesgaming.topography.network;

import java.nio.charset.StandardCharsets;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncPreset implements IMessage, IMessageHandler<PacketSyncPreset, IMessage>
{
    public String preset;

    @Override
    public void fromBytes(final ByteBuf buf) {
        this.preset = buf.readCharSequence(buf.readInt(), StandardCharsets.UTF_8).toString();
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(ConfigurationManager.getInstance().getGeneratorSettings().length());
        
        buf.writeCharSequence(ConfigurationManager.getInstance().getGeneratorSettings(), StandardCharsets.UTF_8);
        Topography.instance.getLog().info("Sending sync packet with preset " + ConfigurationManager.getInstance().getGeneratorSettings());
    }

    @Override
    public IMessage onMessage(PacketSyncPreset message, MessageContext ctx)
    {
        final String preset = message.preset;
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Topography.instance.getLog().info("Received sync message from server with preset '" + preset + "'.");
            ConfigurationManager.setGeneratorSettings(preset);
            ConfigurationManager.getInstance().registerDimensions();
        });
        return null;
    }
}
