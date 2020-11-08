package com.bloodnbonesgaming.topography.common.commands.topography;

import com.bloodnbonesgaming.topography.Topography;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class Hand implements Command<CommandSource> {
	
	private static final Hand INSTANCE = new Hand();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("hand").requires(r -> r.hasPermissionLevel(1)).executes(INSTANCE);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		
		if (player != null) {
			ItemStack stack = player.getHeldItemMainhand();
			
			if (stack != null && !stack.isEmpty()) {
				String message = "ItemHelper.buildItemStack(";
				
				//Add item
				ResourceLocation location = ForgeRegistries.ITEMS.getKey(stack.getItem());
				message = message.concat("\"" + location.toString() + "\"");
				
				//Add count
				message = message.concat(", " + stack.getCount());
				
				//Add nbt
				CompoundNBT compound = stack.getTag();
		        if (compound != null && !compound.isEmpty())
		        {
					message = message.concat(", " + "\"" + compound.toString().replaceAll("\"", "\\\\\"") + "\"");
		        }
				
				message = message + ")";
				Topography.getLog().info(message);
				
				player.sendMessage(new StringTextComponent(message).setStyle(Style.EMPTY.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clipboard"))), Util.DUMMY_UUID);
				player.sendMessage(new StringTextComponent("[Copied To Clipboard]").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN))), Util.DUMMY_UUID);
			}
		}
		return 0;
	}

}
