package com.bloodnbonesgaming.topography.event;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.world.BiomeProviderSkyIslands;
import com.bloodnbonesgaming.topography.world.SkyIslandDataHandler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeProvider;
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
            final SkyIslandDataHandler handler = ((BiomeProviderSkyIslands) provider).getHandler();
            
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
