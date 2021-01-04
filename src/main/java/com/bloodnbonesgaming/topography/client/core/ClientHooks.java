package com.bloodnbonesgaming.topography.client.core;

import com.bloodnbonesgaming.topography.client.gui.element.EnumGuiLocation;
import com.bloodnbonesgaming.topography.client.gui.element.GuiElementTexture;
import com.bloodnbonesgaming.topography.client.gui.element.GuiElementTextureStretch;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.common.config.DimensionDef;
import com.bloodnbonesgaming.topography.common.config.GlobalConfig;
import com.bloodnbonesgaming.topography.common.config.Preset;
import com.bloodnbonesgaming.topography.common.util.IOHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.AbstractList;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ClientHooks {
	
	private static ResourceLocation backgroundLoc = null;
	private static GuiElementTexture texture = null;
	private static String currentImage = null;

	public static void onAbstractListBackgroundDrawn(AbstractList<?> list) {
		list.field_244603_t = false;
		list.renderHeader = false;
		list.field_244604_u = false;
		
		Minecraft minecraft = list.minecraft;
		
		if (backgroundLoc == null) {
			backgroundLoc = minecraft.getTextureManager().getDynamicTextureLocation("background_img", MissingTextureSprite.getDynamicTexture());
		}
		final GlobalConfig config = ConfigurationManager.getGlobalConfig();
		
		if (config != null) {
			String imageName = null;
			
			if (config.getGuiBackground() != null) {
				imageName = config.getGuiBackground();
			}
			final Preset preset = config.getPreset();
			
			if (preset != null) {
				if (preset.getGuiBackground() != null) {
					imageName = preset.getGuiBackground();
				}
				World world = minecraft.world;
				
				if (world != null) {
					DimensionDef def = preset.defs.get(world.getDimensionKey().getLocation());
					
					if (def != null) {
						if (def.getGuiBackground() != null) {
							imageName = def.getGuiBackground();
						}
					}
				}
			}
			
			if (imageName != null && !imageName.isEmpty() && !imageName.equals(currentImage)) {
				final NativeImage image = IOHelper.loadNativeImage(imageName);

				if (image != null) {
					minecraft.getTextureManager().loadTexture(backgroundLoc, new DynamicTexture(image));
					ClientHooks.texture = new GuiElementTextureStretch(EnumGuiLocation.TOP_LEFT, backgroundLoc, image.getWidth(), image.getHeight());
					//ClientHooks.texture = new GuiElementTextureStretch(EnumGuiLocation.TOP_LEFT, new ResourceLocation("textures/gui/options_background.png"), image.getWidth(), image.getHeight());
					ClientHooks.texture.setAbsRender(list.width, list.height);;
					ClientHooks.texture.setAbsXOffset(list.getLeft());
				}
			}
		}
		if (texture != null) {
			texture.render(minecraft, list.width, list.height);
		}
	}
	
	public static void onBackgroundDrawn(final Screen gui) {
		if (backgroundLoc == null) {
			backgroundLoc = gui.getMinecraft().getTextureManager().getDynamicTextureLocation("background_img", MissingTextureSprite.getDynamicTexture());
		}
		final GlobalConfig config = ConfigurationManager.getGlobalConfig();
		
		if (config != null) {
			String imageName = null;
			
			if (config.getGuiBackground() != null) {
				imageName = config.getGuiBackground();
			}
			final Preset preset = config.getPreset();
			
			if (preset != null) {
				if (preset.getGuiBackground() != null) {
					imageName = preset.getGuiBackground();
				}
				World world = gui.getMinecraft().world;
				
				if (world != null) {
					DimensionDef def = preset.defs.get(world.getDimensionKey().getLocation());
					
					if (def != null) {
						if (def.getGuiBackground() != null) {
							imageName = def.getGuiBackground();
						}
					}
				}
			}
			
			if (imageName != null && !imageName.isEmpty() && !imageName.equals(currentImage)) {
				final NativeImage image = IOHelper.loadNativeImage(imageName);

				if (image != null) {
					gui.getMinecraft().getTextureManager().loadTexture(backgroundLoc, new DynamicTexture(image));
					texture = new GuiElementTextureStretch(EnumGuiLocation.TOP_LEFT, backgroundLoc, image.getWidth(), image.getHeight());
					texture.setRelRender(1, 1);
				}
			}
		}
		if (texture != null) {
			texture.render(gui.getMinecraft(), gui.width, gui.height);
		}
	}
}
