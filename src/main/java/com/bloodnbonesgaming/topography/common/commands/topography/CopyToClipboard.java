package com.bloodnbonesgaming.topography.common.commands.topography;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;

public class CopyToClipboard implements Command<CommandSource> {
	
	private static final CopyToClipboard INSTANCE = new CopyToClipboard();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("copytoclipboard").then(Commands.argument("msg", MessageArgument.message())).executes(INSTANCE);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
//		ServerPlayerEntity player = context.getSource().asPlayer();
//		
//		if (player != null) {
//			ITextComponent msg = MessageArgument.getMessage(context, "msg");
//			//String msg = context.getArgument("msg", String.class);
//			
//            ClipboardHelper.copyToClipboard(msg.getString());
//		}
		return 0;
	}
}
