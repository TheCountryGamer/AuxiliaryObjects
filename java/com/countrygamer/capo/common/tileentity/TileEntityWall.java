package com.countrygamer.capo.common.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.core.Base.common.tileentity.TileEntityBase;

public class TileEntityWall extends TileEntityBase {
	
	public boolean hasCamo = false;
	public Block block;
	public int blockMeta;
	
	public TileEntityWall() {
		this.resetBlockData();
	}
	
	@Override
	public boolean canUpdate() {
		return false;
	}
	
	public void resetBlockData() {
		this.setBlockWithMeta(Blocks.air, 0);
	}
	
	public void setBlock(Block block) {
		this.setBlockWithMeta(block, 0);
	}
	
	public void setBlockWithMeta(Block block, int metadata) {
		if (block != null)
			this.hasCamo = true;
		else
			this.hasCamo = false;
		this.block = block;
		this.blockMeta = metadata;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setBoolean("Has_Camo", this.hasCamo);
		tagCom.setInteger("BlockID", Block.getIdFromBlock(this.block));
		tagCom.setInteger("BlockMeta", this.blockMeta);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.hasCamo = tagCom.getBoolean("Has_Camo");
		this.block = Block.getBlockById(tagCom.getInteger("BlockID"));
		this.blockMeta = tagCom.getInteger("BlockMeta");
		
	}
	
}
