package com.bloodnbonesgaming.topography.common.commands.topography;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class NightVision implements Command<CommandSource> {
	
	private static final NightVision INSTANCE = new NightVision();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("nightvision").requires(r -> r.hasPermissionLevel(3)).executes(INSTANCE);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		
		player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 255, false, false));
		return 0;
	}

}
