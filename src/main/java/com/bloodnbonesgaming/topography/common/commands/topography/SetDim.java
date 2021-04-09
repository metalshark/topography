package com.bloodnbonesgaming.topography.common.commands.topography;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class SetDim {
		
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("setdim")
				.requires(r -> r.hasPermissionLevel(3))
				.then(Commands.argument("dim", DimensionArgument.getDimension()))
				.executes(ctx -> execute(ctx, DimensionArgument.getDimensionArgument(ctx, "dim")));
	}
	private static int execute(CommandContext<CommandSource> context, ServerWorld dim) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		String dimName = dim.getDimensionKey().getLocation().toString();
		return player.server.getCommandManager().handleCommand(context.getSource(), "execute in " + dimName + " run tp @p ~ ~ ~");
	}
}
