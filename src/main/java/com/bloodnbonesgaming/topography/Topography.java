package com.bloodnbonesgaming.topography;

import java.io.IOException;

import com.bloodnbonesgaming.lib.BNBGamingMod;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.network.PacketSyncPreset;
import com.bloodnbonesgaming.topography.proxy.CommonProxy;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ModInfo.MODID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION, dependencies = "required-after:bnbgaminglib@[2.14.0,)",
        acceptedMinecraftVersions = "[1.12,1.13)")
public class Topography extends BNBGamingMod
{
    @Instance(ModInfo.MODID)
    public static Topography instance;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
    public static CommonProxy proxy;
    
    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        Topography.proxy.registerEventHandlers();
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        new WorldTypeCustomizable("topography");
//        DimensionManager.unregisterDimension(0);
//        DimensionManager.registerDimension(0, DimensionType.register("Overworld", "", 0, WorldProviderConfigurable.class, true));
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
        network.registerMessage(PacketSyncPreset.class, PacketSyncPreset.class, 0, Side.CLIENT);
    }

    @EventHandler
    public void loadComplete(final FMLLoadCompleteEvent event)
    {
    }

    @EventHandler
    public void serverAboutToSTart(final FMLServerAboutToStartEvent event)
    {
        Topography.proxy.onServerAboutToStart(event);
    }

    @EventHandler
    public void serverStarting(final FMLServerStartingEvent event) throws IOException
    {
    }

    @EventHandler
    public void serverStarted(final FMLServerStartedEvent event) throws IOException
    {
        
    }

    @EventHandler
    public void serverStopped(final FMLServerStoppedEvent event)
    {
        ConfigurationManager.cleanUp();
    }
}