package com.bloodnbonesgaming.topography.common.commands;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.common.commands.topography.CopyToClipboard;
import com.bloodnbonesgaming.topography.common.commands.topography.Hand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ModCommands {

	public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
		//Registers command tree
		LiteralCommandNode<CommandSource> commands = dispatcher.register(
				Commands.literal(ModInfo.MODID)
				.then(Hand.register(dispatcher))
		);
		//Adds an alternative
//		dispatcher.register(Commands.literal("topo").redirect(commands));
	}
}
