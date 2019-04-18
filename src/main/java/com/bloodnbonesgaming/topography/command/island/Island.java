package com.bloodnbonesgaming.topography.command.island;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

public class Island extends CommandTreeBase {

	public Island()
    {
		this.addSubcommand(new IslandNew());
		this.addSubcommand(new IslandHome());
		this.addSubcommand(new IslandInvite());
		this.addSubcommand(new IslandAccept());
		this.addSubcommand(new IslandInfo());
		this.addSubcommand(new IslandSet());
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getName()
    {
        return "island";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
    
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    	return true;
    }

    @Override
    public String getUsage(ICommandSender icommandsender)
    {
        return "Use /topography island <subcommand>";
    }

}
