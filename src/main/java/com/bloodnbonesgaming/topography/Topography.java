package com.bloodnbonesgaming.topography;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.util.FileHelper;
import com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorBlobs;
import com.bloodnbonesgaming.topography.proxy.ClientProxy;
import com.bloodnbonesgaming.topography.proxy.CommonProxy;
import com.bloodnbonesgaming.topography.proxy.ServerProxy;

import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("topography")
public class Topography
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static final CommonProxy proxy = DistExecutor.safeRunForDist(()->ClientProxy::new, ()->ServerProxy::new);

    public Topography() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doServerStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	proxy.setup();
    	proxy.registerEventHandlers();
    	ConfigurationManager.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }
    
    private void doServerStuff(final FMLDedicatedServerSetupEvent event) {
    	//Initialize everything. This happens right before the registry is created.
    	ConfigurationManager.init();
    	String[] lines = FileHelper.readLinesFromFile("./server.properties");//TODO Check file existence first;
    	
    	if (lines != null) {
    		for (String line : lines) {
    			if (line.startsWith("topography-preset")) {
    				String[] split = line.split("=");
    				
    				if (split.length == 2) {
    					String presetStr = split[1].trim();
    					
    					if (presetStr.length() > 0) {
    				    	Topography.getLog().info("Read line: " + line + " " + presetStr);
    				    	ConfigurationManager.getGlobalConfig().setPreset(presetStr);
    				    	Preset preset = ConfigurationManager.getGlobalConfig().getPreset();
    				    	
    				    	if (preset != null) {
        				    	Topography.getLog().info("Preset not null");
    				    		//preset.readDimensionDefs();
    				    	}
    					}
    				}
    			}
    		}
    	}
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
    }

    private void processIMC(final InterModProcessEvent event)
    {
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
        }
        
        static {
        	Registry.register(Registry.BIOME_PROVIDER_CODEC, "topography_blobs", ChunkGeneratorBlobs.BP.CODEC);
    		Registry.register(Registry.CHUNK_GENERATOR_CODEC, "topography_blobs", ChunkGeneratorBlobs.codec);
    	}
    }
    
    public static Logger getLog() {
    	return LOGGER;
    }
}
