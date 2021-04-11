package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.client.SkyRenderer;
import com.bloodnbonesgaming.topography.client.events.ClientEventHandler;
import com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorVoid;

import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
    
    public static final BiomeGeneratorTypeScreens worldType = new BiomeGeneratorTypeScreens("topography") {
//        protected ChunkGenerator func_230484_a_(long seed) {
//           //return new AsyncVanillaNoiseChunkGenerator(new SingleBiomeProvider(Biomes.PLAINS), p_230484_1_, DimensionSettings.Preset.field_236127_g_.func_236137_b_());
////        	new DimensionSettings.Preset("overworld", (preset) -> {
////                return new DimensionSettings(new DimensionStructuresSettings(true), false, preset);
////             });
//           return new ChunkGeneratorVoid(new SingleBiomeProvider(ForgeRegistries.BIOMES.getValue(new ResourceLocation("plains"))), new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false), seed);
//        }

		@Override
		protected ChunkGenerator func_241869_a(Registry<Biome> p_241869_1_, Registry<DimensionSettings> settingsRegistry, long seed) {
			//May want to register the custom dimension settings? Unsure why making a new DimensionSettings instead of using a preset
			//return new ChunkGeneratorVoid(new SingleBiomeProvider(ForgeRegistries.BIOMES.getValue(new ResourceLocation("plains"))), new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false), seed);

			return new ChunkGeneratorVoid(new SingleBiomeProvider(p_241869_1_.getOrThrow(Biomes.PLAINS)), () -> { return new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false);}, seed) ;
		}
     };
     
    @Override
    public void setup() {
    	super.setup();
    	BiomeGeneratorTypeScreens.field_239068_c_.add(worldType);
    	DimensionRenderInfo renderInfo = new DimensionRenderInfo.Nether();
    	renderInfo.setSkyRenderHandler(new SkyRenderer());
    	DimensionRenderInfo.field_239208_a_.put(new ResourceLocation(ModInfo.MODID, "black"), renderInfo);
    }

//	@Override
//	public Impl getRegistries() {
//		return Minecraft.getInstance().getIntegratedServer().field_240767_f_;
//	}
}
