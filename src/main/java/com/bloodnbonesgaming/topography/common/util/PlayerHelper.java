package com.bloodnbonesgaming.topography.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameType;

public class PlayerHelper {
	public GameType getGamemode(PlayerEntity player) {
		if (!player.world.isRemote) {
			return ((ServerPlayerEntity)player).interactionManager.getGameType();
		} else {
			return Util.Client.getGamemode();
		}
	}
	
	public String getGamemodeName(PlayerEntity player) {
		return getGamemode(player).name();
	}
}
