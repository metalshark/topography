package com.bloodnbonesgaming.topography.common.util;

import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class TopographyWorldData extends WorldSavedData {

	private static final String DATA_NAME = ModInfo.MODID + "_world_data";
	
	private int islandIndex = 1;

	public TopographyWorldData(final String s) {
		super(s);
	}

	public TopographyWorldData(){
		super(TopographyWorldData.DATA_NAME);
	}

	@Override
	public void read(final CompoundNBT nbt) {
		if (nbt.contains("Island Index"))
		{
			this.islandIndex = nbt.getInt("Island Index");
		}
	}
	
	@Override
	public CompoundNBT write(final CompoundNBT compound) {
		compound.putInt("Island Index", this.islandIndex);
		return compound;
	}

	public static boolean exists(final ServerWorld world){
		TopographyWorldData data = (TopographyWorldData) world.getSavedData().get(TopographyWorldData::new, TopographyWorldData.DATA_NAME);
		if(data == null){
			data = new TopographyWorldData();
			world.getSavedData().set(data);
			data.markDirty();
			return false;
		}
		return true;
	}
	
	public static void saveIslandIndex(final int index, final ServerWorld world)
	{
		TopographyWorldData data = (TopographyWorldData) world.getSavedData().getOrCreate(TopographyWorldData::new, TopographyWorldData.DATA_NAME);
		if(data != null){
			data.islandIndex = index;
			data.markDirty();
		}
	}
	
	public static int getIslandIndex(final ServerWorld world)
	{
		TopographyWorldData data = (TopographyWorldData) world.getSavedData().getOrCreate(TopographyWorldData::new, TopographyWorldData.DATA_NAME);
		if(data != null){
			return data.islandIndex;
		}
		return 0;
	}
}
