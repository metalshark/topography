package com.bloodnbonesgaming.topography.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class InviteCommand extends CommandBase
{
    final List<String> aliases = new ArrayList<String>();
    final Random random = new Random();

    @Override
    public int compareTo(ICommand arg0)
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "invite";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "Use /topography invite <player>";
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
            final EntityPlayerMP inviter = (EntityPlayerMP) sender;
            final EntityPlayerMP invitee = (EntityPlayerMP) entity;
            final long identifier = this.random.nextLong();
            TextComponentString component = new TextComponentString("You've been invited by " + inviter.getDisplayNameString() + ". Click message to accept. You will be moved to their structure, and you will be unable to return.");
            component.getStyle().setColor(TextFormatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/topography accept " + inviter.getDisplayNameString() + " " + identifier));
            invitee.sendMessage(component);
            
            if (!AcceptCommand.identifiers.containsKey(inviter.getDisplayNameString()))
            {
            	final Map<String, Long> inner = new HashMap<String, Long>();
            	inner.put(invitee.getDisplayNameString(), identifier);
            	AcceptCommand.identifiers.put(inviter.getDisplayNameString(), inner);
            }
            else
            {
            	final Map<String, Long> inner = AcceptCommand.identifiers.get(inviter.getDisplayNameString());
            	inner.put(invitee.getDisplayNameString(), identifier);
            }
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
