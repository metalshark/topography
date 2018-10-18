package com.bloodnbonesgaming.topography.world;

import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class WorldSavedDataTopography extends WorldSavedData{

	private static final String DATA_NAME = ModInfo.MODID + "_world_data";

	public WorldSavedDataTopography(final String s) {
		super(s);
	}

	public WorldSavedDataTopography(){
		super(WorldSavedDataTopography.DATA_NAME);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		return compound;
	}

	public static boolean exists(final World world){
		WorldSavedDataTopography data = (WorldSavedDataTopography) world.getMapStorage().getOrLoadData(WorldSavedDataTopography.class, WorldSavedDataTopography.DATA_NAME);
		if(data == null){
			data = new WorldSavedDataTopography();
			world.getMapStorage().setData(WorldSavedDataTopography.DATA_NAME, data);
			data.markDirty();
			return false;
		}
		return true;
	}

}