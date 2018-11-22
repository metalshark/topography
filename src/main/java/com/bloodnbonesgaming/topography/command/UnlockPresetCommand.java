package com.bloodnbonesgaming.topography.command;

import java.util.ArrayList;
import java.util.List;

import com.bloodnbonesgaming.topography.config.ConfigPreset;
import com.bloodnbonesgaming.topography.config.ConfigurationManager;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class UnlockPresetCommand implements ICommand 
{
    final List<String> aliases = new ArrayList<String>();

    @Override
    public int compareTo(ICommand arg0)
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "unlock";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "Use /topography unlock <presetName>";
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
            StringBuilder builder = new StringBuilder();
            
            for (int i = 0; i < args.length; i++)
            {
                if(i == 0)
                {
                    builder.append(args[i]);
                }
                else
                {
                    builder.append(" " + args[i]);
                }
            }
            final ConfigPreset preset = ConfigurationManager.getInstance().getPresets().get(builder.toString());
            
            if (preset != null)
            {
                ConfigurationManager.getInstance().unlockPreset(preset.getName());
                sender.sendMessage(new TextComponentString("Topography preset `" + preset.getName() + "' unlocked!"));
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
        return 3;
    }
    
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }
}
