package com.bloodnbonesgaming.randomgenskyislands;

import java.io.IOException;

import com.bloodnbonesgaming.lib.BNBGamingMod;
import com.bloodnbonesgaming.randomgenskyislands.event.EventSubscriber;
import com.bloodnbonesgaming.randomgenskyislands.world.WorldTypeSkyIslands;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION, dependencies = "required-after:bnbgaminglib@[2.11.1,)", acceptedMinecraftVersions = "[1.12,1.13)")
public class RandomGenSkyIslands extends BNBGamingMod
{
    @Instance(ModInfo.MODID)
    public static RandomGenSkyIslands instance;

//    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
//    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventSubscriber());
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        final WorldTypeSkyIslands skyIslands = new WorldTypeSkyIslands();
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
    }

    @EventHandler
    public void loadComplete(final FMLLoadCompleteEvent event)
    {
    }
    
    @EventHandler
    public void serverAboutToSTart(final FMLServerAboutToStartEvent event)
    {
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