package com.countrygamer.capo.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.core.Base.block.BlockContainerBase;

public class BlockLaser extends BlockContainerBase {
	
	public BlockLaser(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}
	
	@Override
	public int getRenderType() {
		return -1;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
}
