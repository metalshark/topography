package com.bloodnbonesgaming.topography.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bloodnbonesgaming.topography.IOHelper;
import com.bloodnbonesgaming.topography.StructureHelper;
import com.bloodnbonesgaming.topography.config.DimensionDefinition;
import com.bloodnbonesgaming.topography.config.SkyIslandData;
import com.bloodnbonesgaming.topography.config.SkyIslandType;
import com.bloodnbonesgaming.topography.event.EventSubscriber;
import com.bloodnbonesgaming.topography.event.EventSubscriber.ReTeleporter;
import com.bloodnbonesgaming.topography.util.SpawnStructure;
import com.bloodnbonesgaming.topography.util.capabilities.ITopographyPlayerData;
import com.bloodnbonesgaming.topography.util.capabilities.TopographyPlayerData;
import com.bloodnbonesgaming.topography.world.WorldProviderConfigurable;
import com.bloodnbonesgaming.topography.world.generator.IGenerator;
import com.bloodnbonesgaming.topography.world.generator.SkyIslandGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

public class AcceptCommand extends CommandBase
{
    final List<String> aliases = new ArrayList<String>();
    //Inviter, invitee, random identifier for the invite
    public static final Map<String, Map<String, Long>> identifiers = new HashMap<String, Map<String, Long>>();

    @Override
    public int compareTo(ICommand arg0)
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "accept";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "Use /topography accept <player>";
    }

    @Override
    public List<String> getAliases()
    {
        return this.aliases;
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 0)
        {
        	Entity entity = getEntity(server, sender, args[0], EntityPlayerMP.class);
        	
            if (!checkEntity(entity))
            {
                throw new CommandException("The entity selected (%s) is not valid.", entity.getName());
            }
            final EntityPlayerMP invitee = (EntityPlayerMP) sender;
            final EntityPlayerMP inviter = (EntityPlayerMP) entity;
            
            if (AcceptCommand.identifiers.containsKey(inviter.getDisplayNameString()))
            {
            	final Map<String, Long> inner = AcceptCommand.identifiers.get(inviter.getDisplayNameString());

                if (inner.containsKey(invitee.getDisplayNameString()) && inner.get(invitee.getDisplayNameString()).longValue() == Long.valueOf(args[1]).longValue())
                {
                	inner.remove(invitee.getDisplayNameString());
                	
                	if (inner.isEmpty())
                	{
                		AcceptCommand.identifiers.remove(inviter.getDisplayNameString());
                	}
                	ITopographyPlayerData inviterData = inviter.getCapability(TopographyPlayerData.CAPABILITY_TOPOGRAPHY_PLAYER_DATA, null);
                    
                    if (inviterData != null)
                    {
                    	final World world = server.getWorld(0);
                    	
                    	if (world.provider instanceof WorldProviderConfigurable)
                    	{
                        	final WorldProviderConfigurable provider = (WorldProviderConfigurable) world.provider;
                            DimensionDefinition definition = provider.getDefinition();
                            
                            SpawnStructure structure = definition.getSpawnStructure();
                            
                            if (structure != null)
                            {                    
                                final Template template = IOHelper.loadStructureTemplate(structure.getStructure());

                                if (template != null)
                                {
                                    BlockPos spawn = StructureHelper.getSpawn(template);
                                    
                                    if (spawn != null)
                                    {
                                        spawn = spawn.add(inviterData.getIslandX(), structure.getHeight(), inviterData.getIslandZ());
                                        invitee.setSpawnPoint(spawn, true);
                                        invitee.setPositionAndUpdate(spawn.getX(), spawn.getY(), spawn.getZ());

                                        ITopographyPlayerData inviteeData = invitee.getCapability(TopographyPlayerData.CAPABILITY_TOPOGRAPHY_PLAYER_DATA, null);
                                        inviteeData.setIsland(inviterData.getIslandX(), inviterData.getIslandZ());
                                        
                                        if (invitee.world.provider.getDimension() != 0)
                                        {
                                            invitee.changeDimension(0, new EventSubscriber.ReTeleporter(spawn.up()));
                                        }
                                        return;
                                    }
                                }
                            }
                            else
                            {
                            	for (final IGenerator generator : definition.getGenerators())
                                {
                                    if (generator instanceof SkyIslandGenerator)
                                    {                                        
                                        if (inviterData != null && inviterData.getIslandX() != 0 && inviterData.getIslandZ() != 0)
                                        {
                                            final BlockPos pos = new BlockPos(inviterData.getIslandX(), 0, inviterData.getIslandZ());
                                            final BlockPos topBlock = IslandCommand.getTopSolidOrLiquidBlock(world, pos);
                                            
                                            invitee.setSpawnPoint(topBlock, true);
                                            invitee.setPositionAndUpdate(topBlock.getX(), topBlock.getY(), topBlock.getZ());

                                            ITopographyPlayerData inviteeData = invitee.getCapability(TopographyPlayerData.CAPABILITY_TOPOGRAPHY_PLAYER_DATA, null);
                                            inviteeData.setIsland(inviterData.getIslandX(), inviterData.getIslandZ());
                                            
                                            if (invitee.world.provider.getDimension() != 0)
                                            {
                                                invitee.changeDimension(0, new EventSubscriber.ReTeleporter(topBlock.up()));
                                            }
                                            return;
                                        }
                                        break;
                                    }
                                }
                            }
                    	}
                    }
                }
            }
            sender.sendMessage(new TextComponentString("This invite is not valid."));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return false;
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }
    
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    //From the Forge CommandSetDimension
    private static boolean checkEntity(Entity entity)
    {
        // use vanilla portal logic, try to avoid doing anything too silly
        return !entity.isRiding() && !entity.isBeingRidden() && entity.isNonBoss();
    }
}
