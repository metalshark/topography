package com.bloodnbonesgaming.topography;

import java.io.IOException;

import com.bloodnbonesgaming.lib.BNBGamingMod;
import com.bloodnbonesgaming.lib.util.script.ScriptDocumentationHandler;
import com.bloodnbonesgaming.topography.command.TopographyTreeCommand;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.network.PacketSyncPreset;
import com.bloodnbonesgaming.topography.proxy.CommonProxy;
import com.bloodnbonesgaming.topography.util.capabilities.TopographyPlayerData;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraftforge.fml.common.Loader;
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

@Mod(modid = ModInfo.MODID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION, dependencies = "required-after:bnbgaminglib@[2.14.0,);after:crafttweaker;after:worldbook;",
        acceptedMinecraftVersions = "[1.12,1.13)")
public class Topography extends BNBGamingMod
{
    @Instance(ModInfo.MODID)
    public static Topography instance;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY, serverSide = ModInfo.SERVER_PROXY)
    public static CommonProxy proxy;
    
    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);
    
    public static boolean worldbook = false;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event)
    {
//    	ConfigurationManager.setup();
//        double[] smallNoiseArray = new double[825];
//        double[] largeNoiseArray = new double[65536];
//    	
//    	FastNoise noise = new FastNoise((int) 6969420);
//		noise.SetNoiseType(FastNoise.NoiseType.Cellular);
//        noise.SetFrequency(0.005f);
//        noise.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
//        noise.SetCellularReturnType(FastNoise.CellularReturnType.Distance3Div);
////        noise.SetSeed((int) 6969420);
//        
//        for (int iteration = 0; iteration < 60; iteration++)
//        {
//        	long start = System.nanoTime();
//            
//            for (int i = 0; i < 1000; i++)
//            {
//            	for (int x = 0; x < 5; x++)
//                {
//                	for (int z = 0; z < 5; z++)
//                	{
//                		for (int y = 0; y < 33; y++)
//                		{
//                			noise.GetNoise(x * 4 + 16 * i, y * 8, z * 4 + 16 * i);
//                		}
//                	}
//                }
////            	NumberHelper.interpolate(smallNoiseArray, largeNoiseArray, 5, 33, 5, 4, 8, 4);
//            }
//            
//            long time = System.nanoTime() - start;
//            if (iteration > 50)
//            	Topography.instance.getLog().info(time);
//            else
//            	Topography.clearTimes();
//        }
//        Topography.printTimes();
//    	Topography.instance.getLog().info("-------------------");
//        
//        for (int iteration = 0; iteration < 60; iteration++)
//        {
//        	long start = System.nanoTime();
//            
//        	for (int chunk = 0; chunk < 1000; chunk++)
//            {
//
//            	RunnableSimplexSkewedCellNoise.getNoise(smallNoiseArray, 6969420, chunk * 16, 0, chunk * 16, 5, 33, 4, 8, 0.005f);
//            	NumberHelper.interpolate(smallNoiseArray, largeNoiseArray, 5, 33, 5, 4, 8, 4);
//            }
//            
//            long time = System.nanoTime() - start;
//            if (iteration > 50)
//            	Topography.instance.getLog().info(time);
//        }
        
        
        
    	
    	
    	
    	
		if (Loader.isModLoaded("worldbook"))
        {
			Topography.worldbook = true;
        }
		ScriptDocumentationHandler.setScriptDocs(event.getAsmData());
        Topography.proxy.registerEventHandlers();
        TopographyPlayerData.register();
    }

    @EventHandler
    public void init(final FMLInitializationEvent event)
    {
        new WorldTypeCustomizable("topography");
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
        event.registerServerCommand(new TopographyTreeCommand());
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