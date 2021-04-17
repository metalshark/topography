package com.bloodnbonesgaming.topography.common.blocks;

import net.minecraft.block.StructureBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class StructureBlockExt extends StructureBlock {

	public StructureBlockExt(Properties properties) {
		super(properties);
		this.setRegistryName("minecraft", "structure_block");
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new StructureBlockTileEntityExt();
	}
}
