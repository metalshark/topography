package com.bloodnbonesgaming.topography.common.blocks;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.tileentity.StructureBlockTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraftforge.registries.ForgeRegistries;

public class StructureBlockTileEntityExt extends StructureBlockTileEntity {
	
	public StructureBlockTileEntityExt() {
		super();
	}
	
	@Override
	public TileEntityType<?> getType() {
		return ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation("minecraft:structure_block"));
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		//superclass stuff
	      this.pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
//	      if (nbt.contains("ForgeData")) this.customTileData = nbt.getCompound("ForgeData");
//	      if (getCapabilities() != null && nbt.contains("ForgeCaps")) deserializeCaps(nbt.getCompound("ForgeCaps"));
		//super.read(state, nbt);
	      this.setName(nbt.getString("name"));
	      this.author = nbt.getString("author");
	      this.metadata = nbt.getString("metadata");
	      int i = MathHelper.clamp(nbt.getInt("posX"), -48, 48);
	      int j = MathHelper.clamp(nbt.getInt("posY"), -48, 48);
	      int k = MathHelper.clamp(nbt.getInt("posZ"), -48, 48);
	      this.position = new BlockPos(i, j, k);
	      int l = MathHelper.clamp(nbt.getInt("sizeX"), 0, 1024);
	      int i1 = MathHelper.clamp(nbt.getInt("sizeY"), 0, 256);
	      int j1 = MathHelper.clamp(nbt.getInt("sizeZ"), 0, 1024);
	      this.size = new BlockPos(l, i1, j1);

	      try {
	         this.rotation = Rotation.valueOf(nbt.getString("rotation"));
	      } catch (IllegalArgumentException illegalargumentexception2) {
	         this.rotation = Rotation.NONE;
	      }

	      try {
	         this.mirror = Mirror.valueOf(nbt.getString("mirror"));
	      } catch (IllegalArgumentException illegalargumentexception1) {
	         this.mirror = Mirror.NONE;
	      }

	      try {
	         this.mode = StructureMode.valueOf(nbt.getString("mode"));
	      } catch (IllegalArgumentException illegalargumentexception) {
	         this.mode = StructureMode.DATA;
	      }

	      this.ignoreEntities = nbt.getBoolean("ignoreEntities");
	      this.powered = nbt.getBoolean("powered");
	      this.showAir = nbt.getBoolean("showair");
	      this.showBoundingBox = nbt.getBoolean("showboundingbox");
	      if (nbt.contains("integrity")) {
	         this.integrity = nbt.getFloat("integrity");
	      } else {
	         this.integrity = 1.0F;
	      }

	      this.seed = nbt.getLong("seed");
	      this.updateBlockState();
	}
	
	@Override
	public boolean detectSize() {
		if (this.mode != StructureMode.SAVE) {
	         return false;
	      } else {
	         BlockPos blockpos = this.getPos();
	         int distance = 16*16;
	         BlockPos blockpos1 = new BlockPos(blockpos.getX() - distance, 0, blockpos.getZ() - distance);
	         BlockPos blockpos2 = new BlockPos(blockpos.getX() + distance, 255, blockpos.getZ() + distance);
	         List<StructureBlockTileEntity> list = this.getNearbyCornerBlocks(blockpos1, blockpos2);
	         List<StructureBlockTileEntity> list1 = this.filterRelatedCornerBlocks(list);
	         if (list1.size() < 1) {
	            return false;
	         } else {
	            MutableBoundingBox mutableboundingbox = this.calculateEnclosingBoundingBox(blockpos, list1);
	            if (mutableboundingbox.maxX - mutableboundingbox.minX > 1 && mutableboundingbox.maxY - mutableboundingbox.minY > 1 && mutableboundingbox.maxZ - mutableboundingbox.minZ > 1) {
	               this.position = new BlockPos(mutableboundingbox.minX - blockpos.getX() + 1, mutableboundingbox.minY - blockpos.getY() + 1, mutableboundingbox.minZ - blockpos.getZ() + 1);
	               this.size = new BlockPos(mutableboundingbox.maxX - mutableboundingbox.minX - 1, mutableboundingbox.maxY - mutableboundingbox.minY - 1, mutableboundingbox.maxZ - mutableboundingbox.minZ - 1);
	               this.markDirty();
	               BlockState blockstate = this.world.getBlockState(blockpos);
	               this.world.notifyBlockUpdate(blockpos, blockstate, blockstate, 3);
	               return true;
	            } else {
	               return false;
	            }
	         }
	      }
	}
}
