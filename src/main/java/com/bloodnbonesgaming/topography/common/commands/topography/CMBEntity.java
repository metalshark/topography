package com.bloodnbonesgaming.topography.common.commands.topography;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;

public class CMBEntity implements Command<CommandSource> {
	
	private static final CMBEntity INSTANCE = new CMBEntity();
	
	public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
		return Commands.literal("entity").requires(r -> r.hasPermissionLevel(1)).executes(INSTANCE);
	}

	@Override
	public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		
		if (player != null) {
			
			final World world = player.world;
		    
		    Vector3d vec3d = player.getEyePosition(0);
		    Vector3d vec3d1 = player.getLook(0);
		    Vector3d vec3d2 = vec3d.add(vec3d1.x * 4, vec3d1.y * 4, vec3d1.z * 4);
		    final EntityRayTraceResult result = ProjectileHelper.rayTraceEntities(world, player, vec3d, vec3d2, player.getBoundingBox().grow(5), (Entity entity) -> { return true; });
	        
            if (result != null)
            {
            	Entity entity = result.getEntity();
            	
            	CompoundNBT nbt = entity.serializeNBT();
            	nbt.remove("APX");
            	nbt.remove("APY");
            	nbt.remove("APZ");
            	String message = nbt.toString();
//            	JsonParser parser = new JsonParser();
//            	JsonObject json = (JsonObject) parser.parse(message);
//            	
            	
            	message = message.replaceAll("\"", "\\\\\"");
            	message = message.concat("\"");
            	message = "\"".concat(message);
            	
            	player.sendMessage(new StringTextComponent(message).setStyle(Style.EMPTY.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clipboard"))), Util.DUMMY_UUID);
				player.sendMessage(new StringTextComponent("[Copied To Clipboard]").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN))), Util.DUMMY_UUID);
            }
            else
            {
                player.sendMessage(new StringTextComponent("No entity found"), Util.DUMMY_UUID);
            }
		}
		return 0;
	}

}
