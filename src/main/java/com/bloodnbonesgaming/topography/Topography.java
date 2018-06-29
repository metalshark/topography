package com.bloodnbonesgaming.topography;

import java.io.IOException;

import com.bloodnbonesgaming.lib.BNBGamingMod;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION, dependencies = "required-after:bnbgaminglib@[2.11.1,)", acceptedMinecraftVersions = "[1.12,1.13)")
public class Topography extends BNBGamingMod
{
    @Instance(ModInfo.MODID)
    public static Topography instance;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        Topography.proxy.registerEventHandlers();
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
    }

    @EventHandler
    public void loadComplete(final FMLLoadCompleteEvent event)
    {
        ConfigurationManager.setup();
    }
    
    @EventHandler
    public void serverAboutToSTart(final FMLServerAboutToStartEvent event)
    {
        Topography.proxy.onServerAboutToStart();
    }

    @EventHandler
    public void serverStarting(final FMLServerStartingEvent event) throws IOException
    {
    }

    @EventHandler
    public void serverStopped(final FMLServerStoppedEvent event)
    {
    }
}