package com.bloodnbonesgaming.topography.client.core;

import com.bloodnbonesgaming.topography.Topography;
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
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
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
	
	public static void updateLightmap(LightTexture light, float partialTicks) {
		if (light.needsUpdate) {
			light.needsUpdate = false;
			light.client.getProfiler().startSection("lightTex");
	         ClientWorld clientworld = light.client.world;
	         if (clientworld != null) {
	        	 DimensionDef def = null;
	        	 if (ConfigurationManager.getGlobalConfig().getPreset() != null) {
	        		 def = ConfigurationManager.getGlobalConfig().getPreset().defs.get(clientworld.getDimensionKey().getLocation());
	        	 }
	        	 
	            float f = clientworld.getSunBrightness(1.0F);
	            float f1;
	            if (clientworld.getTimeLightningFlash() > 0) {
	               f1 = 1.0F;
	            } else {
	               f1 = f * 0.95F + 0.05F;
	            }

	            float f3 = light.client.player.getWaterBrightness();
	            float f2;
	            if (light.client.player.isPotionActive(Effects.NIGHT_VISION)) {
	               f2 = GameRenderer.getNightVisionBrightness(light.client.player, partialTicks);
	            } else if (f3 > 0.0F && light.client.player.isPotionActive(Effects.CONDUIT_POWER)) {
	               f2 = f3;
	            } else {
	               f2 = 0.0F;
	            }

	            Vector3f vector3f = new Vector3f(f, f, 1.0F);
	            vector3f.lerp(new Vector3f(1.0F, 1.0F, 1.0F), 0.35F);
	            float f4 = light.torchFlicker + 1.5F;
	            Vector3f vector3f1 = new Vector3f();

	            for(int i = 0; i < 16; ++i) {
	               for(int j = 0; j < 16; ++j) {
	                  float f5 = clientworld.getDimensionType().getAmbientLight(i) * f1;
	                  float f6 = clientworld.getDimensionType().getAmbientLight(j) * f4;
	                  float f7 = f6 * ((f6 * 0.6F + 0.4F) * 0.6F + 0.4F);
	                  float f8 = f6 * (f6 * f6 * 0.6F + 0.4F);
	                  vector3f1.set(f6, f7, f8);
	                  if (clientworld.func_239132_a_().func_241684_d_()) {
	                     vector3f1.lerp(new Vector3f(0.99F, 1.12F, 1.0F), 0.25F);
	                  } else {
	                     Vector3f vector3f2 = vector3f.copy();
	                     vector3f2.mul(f5);
	                     vector3f1.add(vector3f2);
	                     vector3f1.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
	                     if (light.entityRenderer.getBossColorModifier(partialTicks) > 0.0F) {
	                        float f9 = light.entityRenderer.getBossColorModifier(partialTicks);
	                        Vector3f vector3f3 = vector3f1.copy();
	                        vector3f3.mul(0.7F, 0.6F, 0.6F);
	                        vector3f1.lerp(vector3f3, f9);
	                     }
	                  }

	                  vector3f1.clamp(0.0F, 1.0F);
	                  if (f2 > 0.0F) {
	                     float f10 = Math.max(vector3f1.getX(), Math.max(vector3f1.getY(), vector3f1.getZ()));
	                     if (f10 < 1.0F) {
	                        float f12 = 1.0F / f10;
	                        Vector3f vector3f5 = vector3f1.copy();
	                        vector3f5.mul(f12);
	                        vector3f1.lerp(vector3f5, f2);
	                     }
	                  }

	                  float f11 = (float)light.client.gameSettings.gamma;
	                  if (def != null) {
	                	  f11 = Math.max(f11, def.getMinGamma());
	                	  f11 = Math.min(f11, def.getMaxGamma());
	                  } else {

	                	  Topography.getLog().info("def null");
	                  }
	                  Vector3f vector3f4 = vector3f1.copy();
	                  vector3f4.apply(ClientHooks::invGamma);
	                  vector3f1.lerp(vector3f4, f11);
	                  vector3f1.lerp(new Vector3f(0.75F, 0.75F, 0.75F), 0.04F);
	                  vector3f1.clamp(0.0F, 1.0F);
	                  vector3f1.mul(255.0F);
	                  int j1 = 255;
	                  int k = (int)vector3f1.getX();
	                  int l = (int)vector3f1.getY();
	                  int i1 = (int)vector3f1.getZ();
	                  light.nativeImage.setPixelRGBA(j, i, -16777216 | i1 << 16 | l << 8 | k);
	               }
	            }

	            light.dynamicTexture.updateDynamicTexture();
	            light.client.getProfiler().endSection();
	         }
	      }
	   }

	   private static float invGamma(float valueIn) {
	      float f = 1.0F - valueIn;
	      return 1.0F - f * f * f * f;
	   }
}
