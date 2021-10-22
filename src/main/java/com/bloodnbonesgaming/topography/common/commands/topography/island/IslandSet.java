package com.bloodnbonesgaming.topography.common.commands.topography.island;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.Vec2Argument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.vector.Vector2f;

public class IslandSet {
	
//	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
//		return Commands.literal("setdim")
//				.requires(r -> r.hasPermissionLevel(3))
//				.then(Commands.argument("location", Vec2Argument.vec2()))
//				.executes(ctx -> execute(ctx, Vec2Argument.getVec2f(ctx, "location")));
//	}
//	
//	private static int execute(CommandContext<CommandSource> context, Vector2f location) throws CommandSyntaxException {
//		
//		
//		
//		
//		
//		ServerPlayerEntity player = context.getSource().asPlayer();
//		String dimName = dim.getDimensionKey().getLocation().toString();
//		///execute in topography:infinite_dark run tp @s ~ ~ ~
//		return player.server.getCommandManager().handleCommand(context.getSource(), "execute in " + dimName + " run tp @s ~ ~ ~");
//	}
}
