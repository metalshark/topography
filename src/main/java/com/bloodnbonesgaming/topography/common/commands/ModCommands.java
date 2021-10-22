package com.bloodnbonesgaming.topography.common.commands;

import com.bloodnbonesgaming.topography.ModInfo;
import com.bloodnbonesgaming.topography.common.commands.topography.CMBEntity;
import com.bloodnbonesgaming.topography.common.commands.topography.Hand;
import com.bloodnbonesgaming.topography.common.commands.topography.NightVision;
import com.bloodnbonesgaming.topography.common.commands.topography.SetDim;
import com.bloodnbonesgaming.topography.common.commands.topography.island.IslandNew;
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
				.then(NightVision.register(dispatcher))
				.then(SetDim.register(dispatcher))
				.then(CMBEntity.register(dispatcher))
				.then(Commands.literal("island")
						.then(IslandNew.register(dispatcher))
				)
		);
		//Adds an alternative
//		dispatcher.register(Commands.literal("topo").redirect(commands));
	}
}



/*
 * New island
 * tp to island
 * invite to island
 * accept invite to island
 * island location info
 * 
 * 
 */