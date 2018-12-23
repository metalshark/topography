package com.bloodnbonesgaming.topography.world;

import com.bloodnbonesgaming.topography.ModInfo;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class WorldSavedDataTopography extends WorldSavedData {

	private static final String DATA_NAME = ModInfo.MODID + "_world_data";
	
	private int islandIndex = 1;

	public WorldSavedDataTopography(final String s) {
		super(s);
	}

	public WorldSavedDataTopography(){
		super(WorldSavedDataTopography.DATA_NAME);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		if (nbt.hasKey("Island Index"))
		{
			this.islandIndex = nbt.getInteger("Island Index");
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
		compound.setInteger("Island Index", this.islandIndex);
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
	
	public static void saveIslandIndex(final int index, final World world)
	{
		WorldSavedDataTopography data = (WorldSavedDataTopography) world.getMapStorage().getOrLoadData(WorldSavedDataTopography.class, WorldSavedDataTopography.DATA_NAME);
		if(data != null){
			data.islandIndex = index;
			data.markDirty();
		}
	}
	
	public static int getIslandIndex(final World world)
	{
		WorldSavedDataTopography data = (WorldSavedDataTopography) world.getMapStorage().getOrLoadData(WorldSavedDataTopography.class, WorldSavedDataTopography.DATA_NAME);
		if(data != null){
			return data.islandIndex;
		}
		return 0;
	}

}