package com.bloodnbonesgaming.topography;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.util.FileHelper;
import com.bloodnbonesgaming.topography.common.world.WorldRegistry;
import com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorSimplexSkylands;
import com.bloodnbonesgaming.topography.common.world.gen.feature.ColumnFormation;
import com.bloodnbonesgaming.topography.common.world.gen.feature.RegionFeatureRedirector;
import com.bloodnbonesgaming.topography.common.world.gen.feature.StalactiteFormation;
import com.bloodnbonesgaming.topography.common.world.gen.feature.StalagmiteFormation;
import com.bloodnbonesgaming.topography.common.world.gen.feature.StructureFeature;
import com.bloodnbonesgaming.topography.common.world.gen.feature.VerticalOre;
import com.bloodnbonesgaming.topography.common.world.gen.feature.VoidHoleGenerator;
import com.bloodnbonesgaming.topography.proxy.ClientProxy;
import com.bloodnbonesgaming.topography.proxy.CommonProxy;
import com.bloodnbonesgaming.topography.proxy.ServerProxy;
import com.google.common.collect.ImmutableMap;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
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
        
        WorldRegistry.init();
    	ConfigurationManager.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	//Add custom structures to settings map
    	DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.field_236191_b_)
    			.put(WorldRegistry.FORTRESS.get(), new StructureSeparationSettings(27, 4, 30084232)).build();
    	
    	WorldGenRegistries.NOISE_SETTINGS.getValueForKey(DimensionSettings.field_242734_c).getStructures().func_236195_a_().put(WorldRegistry.FORTRESS.get(), new StructureSeparationSettings(27, 4, 30084232));
    	//^Should add to ALL registered noises
    	proxy.setup();
    	proxy.registerEventHandlers();
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
        public static void onBiomeRegistry(final RegistryEvent.Register<Biome> event) {
            event.getRegistry().register(BiomeMaker.makePlainsBiome(false).setRegistryName(ModInfo.MODID, "dark_deep"));
        }
        
        @SubscribeEvent
        public static void onFeatureRegister(final RegistryEvent.Register<Feature<?>> event) {
        	event.getRegistry().register(VerticalOre.INSTANCE);
        	event.getRegistry().register(ColumnFormation.INSTANCE);
        	event.getRegistry().register(StalactiteFormation.INSTANCE);
        	event.getRegistry().register(StalagmiteFormation.INSTANCE);
        	event.getRegistry().register(VoidHoleGenerator.INSTANCE);
        	event.getRegistry().register(StructureFeature.INSTANCE);
        	event.getRegistry().register(RegionFeatureRedirector.INSTANCE);
        }
        
//        @SubscribeEvent
//        public static void onStructureRegister(final RegistryEvent.Register<Structure<?>> event) {
//        	event.getRegistry().register(FortressStructureTopo.INSTANCE);
//        }
        
        static {
        	Registry.register(Registry.BIOME_PROVIDER_CODEC, "topography_blobs", ChunkGeneratorSimplexSkylands.BP.CODEC);
    		Registry.register(Registry.CHUNK_GENERATOR_CODEC, "topography_blobs", ChunkGeneratorSimplexSkylands.codec);
    	}
    }
    
    public static Logger getLog() {
    	return LOGGER;
    }
}
