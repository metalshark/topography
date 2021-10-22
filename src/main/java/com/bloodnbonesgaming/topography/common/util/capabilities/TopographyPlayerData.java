package com.bloodnbonesgaming.topography.common.util.capabilities;

import java.util.concurrent.Callable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class TopographyPlayerData implements ITopographyPlayerData {

	private int x = 0;
	private int z = 0;
	
	public TopographyPlayerData() {
		
	}
	
	@Override
	public void setIsland(final int x, final int z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public int getIslandX() {
		return this.x;
	}

	@Override
	public int getIslandZ() {
		return this.z;
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT nbt = new CompoundNBT();
		
		if (this.x != 0) {
			nbt.putInt("X", this.x);
		}
		if (this.z != 0) {
			nbt.putInt("Z", this.z);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		if (nbt.contains("X")) {
			this.x = nbt.getInt("X");
		}
		if (nbt.contains("Z")) {
			this.z = nbt.getInt("Z");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@CapabilityInject(ITopographyPlayerData.class)
	public static final Capability<ITopographyPlayerData> CAPABILITY_TOPOGRAPHY_PLAYER_DATA = null;
	
	public static void register() {
		CapabilityManager.INSTANCE.register(ITopographyPlayerData.class, new TopographyPlayerData.PlayerDataStorage(), new TopographyPlayerData.PlayerDataFactory());
	}
	
	private static class PlayerDataFactory implements Callable<ITopographyPlayerData> {

		@Override
		public ITopographyPlayerData call() throws Exception {
			return new TopographyPlayerData();
		}
		
	}
	
	private static class PlayerDataStorage implements IStorage<ITopographyPlayerData> {
		@Override
		public INBT writeNBT(Capability<ITopographyPlayerData> capability, ITopographyPlayerData instance, Direction side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<ITopographyPlayerData> capability, ITopographyPlayerData instance, Direction side, INBT nbt) {
			instance.deserializeNBT((CompoundNBT) nbt);
		}
	}
}
