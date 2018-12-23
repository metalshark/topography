package com.bloodnbonesgaming.topography.event;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.StructureHelper;
import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.config.EntityEffect;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.util.SpawnStructure;
import com.bloodnbonesgaming.topography.util.capabilities.ITopographyPlayerData;
import com.bloodnbonesgaming.topography.util.capabilities.TopographyPlayerData;
import com.bloodnbonesgaming.topography.world.WorldProviderConfigurable;
import com.bloodnbonesgaming.topography.world.WorldSavedDataTopography;
import com.bloodnbonesgaming.topography.world.WorldTypeCustomizable;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGenerator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.command.FunctionObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

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
    
    @SubscribeEvent
    public void playerLoginEvent(final PlayerLoggedInEvent event)
    {
    	if (!event.player.world.isRemote)
    	{
    		if (ConfigurationManager.getInstance() != null)
    		{
    			final ResourceLocation function = ConfigurationManager.getInstance().getPreset().getInitialPlayerFunction();
    			
    			if (function != null)
    			{
            		final NBTTagCompound nbt = event.player.getEntityData();
            		
            		if (!nbt.hasKey(EntityPlayer.PERSISTED_NBT_TAG))
            		{
            			nbt.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
            		}
            		
            		final NBTTagCompound persistent = nbt.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            		
            		if (!persistent.hasKey("topography_initial"))
            		{
            			final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
            			
                        FunctionObject functionobject = server.getFunctionManager().getFunction(function);
                        server.getFunctionManager().execute(functionobject, CommandSenderWrapper.create(event.player).computePositionVector().withPermissionLevel(2).withSendCommandFeedback(false));
            			persistent.setBoolean("topography_initial", true);
            		}
    			}
    		}
    	}
    }
    
    @SubscribeEvent
    public void onWorldTick(final WorldTickEvent event)
    {
    	if (event.phase == TickEvent.Phase.START)
    	{
    		if (!event.world.isRemote)
    		{
    			if (event.world.provider.getDimension() == 0)
                {
        			if (!WorldSavedDataTopography.exists(event.world))
    				{
        				String settings = event.world.getWorldInfo().getGeneratorOptions();
                        
                    	if (!settings.isEmpty())
                    	{
                            final JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(settings);
                            
                            if (element.isJsonObject())
                            {
                                if (((JsonObject) element).has("Topography-Preset"))
                                {
                                	final ConfigPreset preset = ConfigurationManager.getInstance().getPreset();
                                    
                                    if (preset.getDifficulty() != null)
                                    {
                                    	event.world.getWorldInfo().setDifficulty(preset.getDifficulty());
                                    }
                                    
                                    if (preset.shouldLockDifficulty())
                                    {
                                    	event.world.getWorldInfo().setDifficultyLocked(true);
                                    }
                                	
                                    if (preset.hardcore())
                                    {
                                    	event.world.getWorldInfo().setHardcore(true);
                                    }
                                	
                                	final ResourceLocation function = ConfigurationManager.getInstance().getPreset().getInitialServerFunction();
                        			
                        			if (function != null)
                        			{
                        				final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
                            			
                                        FunctionObject functionobject = server.getFunctionManager().getFunction(function);
                                        
                                        if (functionobject != null)
                                        {
                                        	Topography.instance.getLog().info("Running initial server function.");
        	                                server.getFunctionManager().execute(functionobject, CommandSenderWrapper.create(server).computePositionVector().withPermissionLevel(2).withSendCommandFeedback(false));
                                        }
                        			}
//                        			if (event.world.provider instanceof WorldProviderConfigurable)
//                        			{
//                        				final SpawnStructure structure = ((WorldProviderConfigurable)event.world.provider).getDefinition().getSpawnStructure();
//                                        
//                                        if (structure != null)
//                                        {
//                                            for (int x = -3; x < 4; x++)
//                                            {
//                                                for (int z = -4; z < 4; z++)
//                                                {
//                                                    event.world.getChunkProvider().provideChunk(x, z);
//                                                }
//                                            }
//                                            
//                                            final Template template = IOHelper.loadStructureTemplate(structure.getStructure());
//                        
//                                            Topography.instance.getLog().info("Spawning structure");
//                        
//                                            template.addBlocksToWorld(event.world, new BlockPos(0, structure.getHeight(), 0), new PlacementSettings(), 0);
//                                            
//                                            final BlockPos spawn = StructureHelper.getSpawn(template);
//                                            
//                                            if (spawn != null)
//                                            {
//                                                event.world.getGameRules().setOrCreateGameRule("spawnRadius", "0");
//                                                event.world.getWorldInfo().setSpawn(spawn.add(0, structure.getHeight(), 0));
////                                                event.setCanceled(true);
//                                            }
//                                        }
//                        			}
                        			for (final int dimension : ConfigurationManager.getInstance().getPreset().getDimensions())
                        			{
                        				String script = ConfigurationManager.getInstance().getPreset().getScript(dimension);
                        	            DimensionDefinition definition = new DimensionDefinition();
                        	            IOHelper.loadDimensionDefinition(script, definition);
                        	            
                        	            SpawnStructure structure = definition.getSpawnStructure();
                        	            
                        	            if (structure != null)
                        	            {
                        	            	final World dimWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
                                            
                                            final Template template = IOHelper.loadStructureTemplate(structure.getStructure());
                                            
                                            if (template != null)
                                            {
                                                int preloadArea = template.getSize().getX();
                                                preloadArea = template.getSize().getZ() > preloadArea ? template.getSize().getZ() : preloadArea;
                                                preloadArea = preloadArea / 16;
                                                preloadArea += 4;
                                                Topography.instance.getLog().info("Preloading " + ((preloadArea * 2 + 1) * (preloadArea * 2 + 1)) + " chunks for spawn structure in dimension " + dimension);
                            	            	
                            	            	for (int x = -preloadArea; x < preloadArea; x++)
                                                {
                                                    for (int z = -preloadArea; z < preloadArea; z++)
                                                    {
                                                       dimWorld.getChunkProvider().provideChunk(x, z);
                                                    }
                                                }
                            	            	Topography.instance.getLog().info("Spawning structure for dimension " + dimension);
                            
                                                template.addBlocksToWorld(dimWorld, new BlockPos(0, structure.getHeight(), 0), new PlacementSettings(), 2);
                                                
                                                if (dimension == 0)
                                                {
                                                    final BlockPos spawn = StructureHelper.getSpawn(template);
                                                    
                                                    if (spawn != null)
                                                    {
                                                        dimWorld.getGameRules().setOrCreateGameRule("spawnRadius", "0");
                                                        dimWorld.getWorldInfo().setSpawn(spawn.add(0, structure.getHeight(), 0));
                                                    }
                                                }
                                            }
                        	            }
                        			}
                                }
                            }
                    	}
    				}
                }
    		}
    	}
    }
    
    private boolean teleporting = false;
    
    @SubscribeEvent
    public void onDimensionChange(final EntityTravelToDimensionEvent event)
    {
    	if (!teleporting)
    	{
        	final World world = event.getEntity().getEntityWorld().getMinecraftServer().getWorld(event.getDimension());
        	
        	if (world.provider instanceof WorldProviderConfigurable)
        	{
        		final WorldProviderConfigurable provider = (WorldProviderConfigurable) world.provider;
                DimensionDefinition definition = provider.getDefinition();
                
                if (definition.shouldCaptureTeleports())
                {
                    SpawnStructure structure = definition.getSpawnStructure();
                    
                    if (structure != null)
                    {                    
                        final Template template = IOHelper.loadStructureTemplate(structure.getStructure());

                        if (template != null)
                        {
                            BlockPos spawn = StructureHelper.getSpawn(template);
                            
                            if (spawn != null)
                            {
                            	ITopographyPlayerData data = event.getEntity().getCapability(TopographyPlayerData.CAPABILITY_TOPOGRAPHY_PLAYER_DATA, null);
                                if (data != null)
                                {
                                    spawn = spawn.add(data.getIslandX(), 0, data.getIslandZ());
                                }
                            	
                                event.setCanceled(true);
                                this.teleporting = true;
                                event.getEntity().changeDimension(event.getDimension(), new ReTeleporter(spawn.add(0, structure.getHeight(), 0)));
                                this.teleporting = false;
                            }
                        }
                    }
                    else
                    {
                    	for (final IGenerator generator : definition.getGenerators())
                        {
                            if (generator instanceof SkyIslandGenerator)
                            {
                                final SkyIslandGenerator islandGenerator = (SkyIslandGenerator) generator;
                                
                                final Iterator<Entry<SkyIslandData, Map<BlockPos, SkyIslandType>>> iterator = islandGenerator.getIslandPositions(world.getSeed(), 0, 0).entrySet().iterator();
                                
                                if (iterator.hasNext())
                                {
                                    final Entry<SkyIslandData, Map<BlockPos, SkyIslandType>> islands = iterator.next();
                                    
                                    final Iterator<Entry<BlockPos, SkyIslandType>> positions = islands.getValue().entrySet().iterator();
                                    
                                    if (positions.hasNext())
                                    {
                                        final Entry<BlockPos, SkyIslandType> island = positions.next();
                                        
                                        final BlockPos pos = island.getKey();
                                        final BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                                        
                                        event.setCanceled(true);
                                        this.teleporting = true;
                                        event.getEntity().changeDimension(event.getDimension(), new ReTeleporter(topBlock.up()));
                                        this.teleporting = false;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
        	}
    	}
    }

    public static class ReTeleporter implements ITeleporter
    {
        private final BlockPos targetPos;

        public ReTeleporter(BlockPos targetPos)
        {
            this.targetPos = targetPos;
        }

        @Override
        public void placeEntity(World world, Entity entity, float yaw)
        {
            entity.moveToBlockPosAndAngles(targetPos, yaw, entity.rotationPitch);
        }
    }
    
    @SubscribeEvent
    public void onAttachCapability(final AttachCapabilitiesEvent<Entity> event)
    {
    	if (event.getObject() instanceof EntityPlayerMP)
    	{
    		event.addCapability(TopographyPlayerData.Provider.location, new TopographyPlayerData.Provider());
    	}
    }
}
