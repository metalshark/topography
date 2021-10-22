package com.bloodnbonesgaming.topography.common.util.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITopographyPlayerData extends INBTSerializable<CompoundNBT> {
	
	void setIsland(final int x, final int z);
	
	int getIslandX();
	
	int getIslandZ();
}
