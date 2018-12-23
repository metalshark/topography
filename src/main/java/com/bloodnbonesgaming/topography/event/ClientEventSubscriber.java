package com.bloodnbonesgaming.topography.event;

import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.client.gui.GuiCreateWorldTopography;
import com.bloodnbonesgaming.topography.client.gui.GuiWorldSelectionOverride;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.world.WorldProviderConfigurable;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventSubscriber
{
    @SubscribeEvent
    public void onOpenGui(final GuiOpenEvent event)
    {
        if (event.getGui() instanceof GuiCreateWorld && Minecraft.getMinecraft().currentScreen instanceof GuiWorldSelection)
        {
            //ConfigurationManager.setup();
        	event.setGui(new GuiCreateWorldTopography(Minecraft.getMinecraft().currentScreen));
            WorldTypeCustomizable.gui = (GuiCreateWorld) event.getGui();
            
            if (ConfigurationManager.getInstance().defaultWorldType())
            {
                for (int i = 0; i < WorldType.WORLD_TYPES.length; i++)
                {
                    if (WorldType.WORLD_TYPES[i] instanceof WorldTypeCustomizable)
                    {
                        ((GuiCreateWorld) event.getGui()).selectedIndex = i;
                        break;
                    }
                }
            }
        }
        if (event.getGui() instanceof GuiWorldSelection && !(event.getGui() instanceof GuiWorldSelectionOverride))
        {
        	event.setGui(new GuiWorldSelectionOverride(((GuiWorldSelection)event.getGui()).prevScreen));
        }
    }
    
    @SubscribeEvent
    public void getFogColors(final FogColors event)
    {
    	if (event.getEntity().world.provider instanceof WorldProviderConfigurable)
    	{
    		final DimensionDefinition definition = ((WorldProviderConfigurable)event.getEntity().world.provider).getDefinition();

        	final Map<Integer, Map<MinMaxBounds, MinMaxBounds>> fogMap = definition.getFog();
        	
        	if (fogMap != null && !fogMap.isEmpty())
        	{
        		float angle = event.getEntity().world.getCelestialAngle((float) event.getRenderPartialTicks());
        		
        		Vec3d color = new Vec3d(0, 0, 0);
        		
        		for (final Entry<Integer, Map<MinMaxBounds, MinMaxBounds>> fog : fogMap.entrySet())
        		{
        			float alpha = 0.0F;
        			
        			for (final Entry<MinMaxBounds, MinMaxBounds> entry : fog.getValue().entrySet())
        			{
        				if (entry.getKey().test(angle))
        				{
        					final MinMaxBounds key = entry.getKey();
        					final MinMaxBounds value = entry.getValue();
        					
        					if (value.min != null && value.max != null)
        					{
        						float diff = key.max - key.min;
        						float distIntoRange = angle - key.min;
        						float percent = distIntoRange / diff;
        						
        						if (value.min > value.max)
        						{
        							float alphaDiff = value.min - value.max;
        							alpha = value.min - alphaDiff * percent;
        						}
        						else
        						{
        							float alphaDiff = value.max - value.min;
        							alpha = value.min + alphaDiff * percent;
        						}
        						break;
        					}
        				}
        			}
        			float alphaOpposite = 1.0F - alpha;
        			
        			color = new Vec3d((((fog.getKey() >> 16) & 255) / 255F) * alpha + color.x * alphaOpposite, (((fog.getKey() >> 8) & 255) / 255F) * alpha + color.y * alphaOpposite, ((fog.getKey() & 255) / 255F) * alpha + color.z * alphaOpposite);
//        			
//        			float remaining = 1.0F - totalAlpha;
//        			color = color.addVector(((((fog.getKey() >> 16) & 255) / 255F) * alpha) * remaining, ((((fog.getKey() >> 8) & 255) / 255F) * alpha) * remaining, (((fog.getKey() & 255) / 255F) * alpha) * remaining);
//        			totalAlpha += (alpha * remaining);
//        			
//        			if (alpha == 1.0F)
//        			{
//        				break;
//        			}
        		}
        		event.setRed((float) color.x);
        		event.setGreen((float) color.y);
        		event.setBlue((float) color.z);
        	}
    	}
    }
}
