package com.bloodnbonesgaming.topography.event;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.StructureHelper;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.EntityEffect;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.world.WorldProviderConfigurable;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGenerator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventSubscriber
{
    @SubscribeEvent
    public void onCreateWorldSpawn(final WorldEvent.CreateSpawnPosition event)
    {
//        if (event.getWorld().getWorldType() instanceof WorldTypeCustomizable)
//        {
            final WorldProvider provider = event.getWorld().provider;
            
            if (provider instanceof WorldProviderConfigurable)
            {
                final WorldProviderConfigurable worldProvider = (WorldProviderConfigurable) provider;
                
                for (final IGenerator generator : worldProvider.getDefinition().getGenerators())
                {
                    if (generator instanceof SkyIslandGenerator)
                    {
                        final SkyIslandGenerator islandGenerator = (SkyIslandGenerator) generator;
                        
                        final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = islandGenerator.getIslandPositions(event.getWorld().getSeed(), 0, 0).entrySet().iterator();
                        
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
                        break;
                    }
                }
    
                if (!event.getWorld().getWorldInfo().isInitialized())
                {
                    final String structure = worldProvider.getDefinition().getSpawnStructure();
                    
                    if (structure != null)
                    {
                        for (int x = -3; x < 4; x++)
                        {
                            for (int z = -4; z < 4; z++)
                            {
                                event.getWorld().getChunkProvider().provideChunk(x, z);
                            }
                        }
                        
                        final Template template = IOHelper.loadStructureTemplate(structure);
    
                        Topography.instance.getLog().info("Spawning structure");
    
                        template.addBlocksToWorld(event.getWorld(), new BlockPos(0, 64, 0), new PlacementSettings(), 0);
                        
                        final BlockPos spawn = StructureHelper.getSpawn(template);
                        
                        if (spawn != null)
                        {
                            event.getWorld().getGameRules().setOrCreateGameRule("spawnRadius", "0");
                            event.getWorld().getWorldInfo().setSpawn(spawn.add(0, 64, 0));
                            event.setCanceled(true);
                        }
                    }
                }
            }
//        }
    }
    
    @SubscribeEvent
    public void onGetPotentialSpawns(final WorldEvent.PotentialSpawns event)
    {
        if (event.getWorld().getWorldType() instanceof WorldTypeCustomizable)
        {
            if (event.getWorld().getChunkFromBlockCoords(event.getPos()).isEmpty())
            {
                event.setCanceled(true);
            }
        }
    }
    
//    @SubscribeEvent
//    public void onFossilGenerate(final DecorateBiomeEvent.Decorate event)
//    {
//        final BiomeProvider provider = event.getWorld().provider.getBiomeProvider();
//        
//        if (provider instanceof BiomeProviderSkyIslands)
//        {
//            if (event.getType() == DecorateBiomeEvent.Decorate.EventType.FOSSIL)
//            {
//                event.setCanceled(true);
//            }
//        }
//    }
    
    @SubscribeEvent
    public void onEntityTick(final LivingUpdateEvent event)
    {
        if (event.getEntityLiving().world.provider instanceof WorldProviderConfigurable)
        {
            final WorldProviderConfigurable provider = (WorldProviderConfigurable) event.getEntityLiving().world.provider;
            
            for (final EntityEffect effect : provider.getDefinition().getEntityEffects())
            {
                effect.apply(event.getEntityLiving());
            }
        }
    }
}
