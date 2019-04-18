package com.bloodnbonesgaming.topography.command;

import com.bloodnbonesgaming.topography.command.island.Island;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

public class TopographyTreeCommand extends CommandTreeBase {

	public TopographyTreeCommand()
    {
		super.addSubcommand(new Island());
		super.addSubcommand(new UnlockPresetCommand());
		super.addSubcommand(new LockPresetCommand());
		this.addSubcommand(new Spawn());
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getName()
    {
        return "topography";
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    /**
     * Gets the usage string for the command.
     */
    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "Use /topography <subcommand>";
    }

}
