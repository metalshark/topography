package com.bloodnbonesgaming.topography.util.capabilities;

import java.util.concurrent.Callable;

import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

public class TopographyPlayerData implements ITopographyPlayerData, INBTSerializable<NBTTagCompound> {

	private int x = 0;
	private int z = 0;
	
	public TopographyPlayerData()
	{
		
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
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound nbt = new NBTTagCompound();
		
		if (this.x != 0)
		{
			nbt.setInteger("X", this.x);
		}
		if (this.z != 0)
		{
			nbt.setInteger("Z", this.z);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("X"))
		{
			this.x = nbt.getInteger("X");
		}
		if (nbt.hasKey("Z"))
		{
			this.z = nbt.getInteger("Z");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@CapabilityInject(ITopographyPlayerData.class)
	public static final Capability<ITopographyPlayerData> CAPABILITY_TOPOGRAPHY_PLAYER_DATA = null;
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(ITopographyPlayerData.class, new TopographyPlayerData.TopographyPlayerDataStorage(), new TopographyPlayerData.TopographyPlayerDataFactory());
	}
	
	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

		public static final ResourceLocation location = new ResourceLocation(ModInfo.MODID, "player_data");
		private final TopographyPlayerData instance = new TopographyPlayerData();
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == CAPABILITY_TOPOGRAPHY_PLAYER_DATA;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == CAPABILITY_TOPOGRAPHY_PLAYER_DATA)
			{
				return CAPABILITY_TOPOGRAPHY_PLAYER_DATA.cast(this.instance);
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
			return instance.serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			instance.deserializeNBT(nbt);
		}
		
		
	}
	
	public static class TopographyPlayerDataFactory implements Callable<ITopographyPlayerData>
	{

		@Override
		public ITopographyPlayerData call() throws Exception {
			return new TopographyPlayerData();
		}
		
	}
	
	//DOES NOTHING
	public static class TopographyPlayerDataStorage implements IStorage<ITopographyPlayerData>
	{
		@Override
		public NBTBase writeNBT(Capability<ITopographyPlayerData> capability, ITopographyPlayerData instance,
				EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<ITopographyPlayerData> capability, ITopographyPlayerData instance,
				EnumFacing side, NBTBase nbt) {
			
		}
	}
}
