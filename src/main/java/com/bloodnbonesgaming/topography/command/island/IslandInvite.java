package com.bloodnbonesgaming.topography.command.island;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.server.permission.PermissionAPI;

public class IslandInvite extends CommandBase
{
    final List<String> aliases = new ArrayList<String>();
    private final Random random = new Random();

    @Override
    public String getName()
    {
        return "invite";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "Use /topography island invite <player>";
    }

    @Override
    public List<String> getAliases()
    {
        return this.aliases;
    }
    
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    	return !(sender instanceof EntityPlayerMP) || PermissionAPI.hasPermission((EntityPlayerMP) sender, "topography.island.invite");
    }
    
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 0)
        {
            final EntityPlayerMP invitee = CommandBase.getPlayer(server, sender, args[0]);
        	
            if (invitee == null)
            {
                throw new CommandException("The entity selected (%s) is not valid.", args[0]);
            }
            final EntityPlayerMP inviter = (EntityPlayerMP) sender;
            final long identifier = this.random.nextLong();
            TextComponentString component = new TextComponentString("You've been invited by " + inviter.getDisplayNameString() + ". Click message to accept. You will be moved to their structure, and you will be unable to return.");
            component.getStyle().setColor(TextFormatting.GREEN).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/topography island accept " + inviter.getDisplayNameString() + " " + identifier));
            invitee.sendMessage(component);
            
            if (!IslandAccept.identifiers.containsKey(inviter.getDisplayNameString()))
            {
            	final Map<String, Long> inner = new HashMap<String, Long>();
            	inner.put(invitee.getDisplayNameString(), identifier);
            	IslandAccept.identifiers.put(inviter.getDisplayNameString(), inner);
            }
            else
            {
            	final Map<String, Long> inner = IslandAccept.identifiers.get(inviter.getDisplayNameString());
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
        return index == 0;
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}
