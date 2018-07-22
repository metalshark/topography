package com.bloodnbonesgaming.topography.event;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.StructureHelper;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.config.definitions.SkyIslandDefinition;
import com.bloodnbonesgaming.topography.world.BiomeProviderSkyIslands;
import com.bloodnbonesgaming.topography.world.WorldProviderConfigurable;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventSubscriber
{
    @SubscribeEvent
    public void onCreateWorldSpawn(final WorldEvent.CreateSpawnPosition event)
    {
        final BiomeProvider provider = event.getWorld().provider.getBiomeProvider();
        
        if (provider instanceof BiomeProviderSkyIslands)
        {
            final SkyIslandDefinition handler = ((BiomeProviderSkyIslands) provider).getHandler();
            
            final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = handler.getIslandPositions(event.getWorld().getSeed(), 0, 0).entrySet().iterator();
            
            if (iterator.hasNext())
            {
                final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> islands = iterator.next();
                
                final Iterator<Entry<BlockPos, SkyIslandType>> positions = islands.getValue().entrySet().iterator();
                
                if (positions.hasNext())
                {
                    final Entry<BlockPos, SkyIslandType> island = positions.next();
                    
                    final BlockPos pos = island.getKey();
                    final BlockPos topBlock = event.getWorld().getTopSolidOrLiquidBlock(pos);
                    
                    event.getWorld().getWorldInfo().setSpawn(topBlock.up());
                    event.setCanceled(true);
                }
            }
        }
        
        if (event.getWorld().getWorldType() instanceof WorldTypeCustomizable)
        {
            if (!event.getWorld().getWorldInfo().isInitialized())
            {
                final WorldProviderConfigurable worldProvider = (WorldProviderConfigurable) event.getWorld().provider;
                
                final String structure = worldProvider.getDefinition().getSpawnStructure();
                
                if (structure != null)
                {
                    final Template template = IOHelper.loadStructureTemplate(structure);

                    Topography.instance.getLog().info("Spawning structure");

                    template.addBlocksToWorld(event.getWorld(), new BlockPos(0, 128, 0), new PlacementSettings(), 0);
                    
                    final BlockPos spawn = StructureHelper.getSpawn(template);
                    
                    if (spawn != null)
                    {
                        event.getWorld().getWorldInfo().setSpawn(spawn.add(0, 128, 0));
                        event.setCanceled(true);
                    }
                }
            }
        }
        
        
//      if (template != null)
      {          
          
      }
    }
    
    @SubscribeEvent
    public void onFossilGenerate(final DecorateBiomeEvent.Decorate event)
    {
        final BiomeProvider provider = event.getWorld().provider.getBiomeProvider();
        
        if (provider instanceof BiomeProviderSkyIslands)
        {
            if (event.getType() == DecorateBiomeEvent.Decorate.EventType.FOSSIL)
            {
                event.setCanceled(true);
            }
        }
    }
}
