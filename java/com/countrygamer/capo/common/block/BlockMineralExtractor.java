package com.countrygamer.capo.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.core.Base.common.block.BlockContainerBase;

public class BlockMineralExtractor extends BlockContainerBase {
	
	public BlockMineralExtractor(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}
	
}
