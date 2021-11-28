package com.bloodnbonesgaming.topography.proxy;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.client.ChatListenerCopy;
import com.bloodnbonesgaming.topography.client.SkyRendererFogOnly;
import com.bloodnbonesgaming.topography.client.events.ClientEventHandler;
import com.bloodnbonesgaming.topography.common.util.ClientUtil;
import com.bloodnbonesgaming.topography.common.world.gen.ChunkGeneratorVoid;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.BiomeGeneratorTypeScreens;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ChatType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.SingleBiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.NoiseSettings;
import net.minecraft.world.gen.settings.ScalingSettings;
import net.minecraft.world.gen.settings.SlideSettings;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Consumer;

public class ClientProxy extends CommonProxy {

	private Consumer<GuiOpenEvent> guiOpenEventConsumer;

	@Override
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		guiOpenEventConsumer = (event) -> {
			if (!(event.getGui() instanceof MainMenuScreen))
				return;

			Topography.getLog().info("Registering Clipboard chat listener");
			(Minecraft.getInstance().ingameGUI.chatListeners.get(ChatType.SYSTEM)).add(new ChatListenerCopy());
			MinecraftForge.EVENT_BUS.unregister(guiOpenEventConsumer);
		};
		MinecraftForge.EVENT_BUS.addListener(guiOpenEventConsumer);
	}
    
    public static final BiomeGeneratorTypeScreens worldType = new BiomeGeneratorTypeScreens("topography") {
		@Override
		protected ChunkGenerator func_241869_a(Registry<Biome> p_241869_1_, Registry<DimensionSettings> settingsRegistry, long seed) {
			//May want to register the custom dimension settings? Unsure why making a new DimensionSettings instead of using a preset
			return new ChunkGeneratorVoid(new SingleBiomeProvider(p_241869_1_.getOrThrow(Biomes.PLAINS)), () -> { return new DimensionSettings(new DimensionStructuresSettings(false), new NoiseSettings(256, new ScalingSettings(0.9999999814507745D, 0.9999999814507745D, 80.0D, 160.0D), new SlideSettings(-10, 3, 0), new SlideSettings(-30, 0, 0), 1, 2, 1.0D, -0.46875D, true, true, false, false), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), -10, 0, 63, false);}, seed) ;
		}
     };
     
    @Override
    public void setup() {
    	super.setup();
    	BiomeGeneratorTypeScreens.field_239068_c_.add(worldType);
    	DimensionRenderInfo renderInfo = new DimensionRenderInfo.Nether();
    	renderInfo.setSkyRenderHandler(new SkyRendererFogOnly());
    	DimensionRenderInfo.field_239208_a_.put(new ResourceLocation(ModInfo.MODID, "black"), renderInfo);
    }

	@Override
	public ClientUtil makeClientUtil() {
		return new ClientUtil();
	}
}
