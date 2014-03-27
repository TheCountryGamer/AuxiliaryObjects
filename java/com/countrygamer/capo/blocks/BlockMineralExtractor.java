package com.countrygamer.capo.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.core.Base.block.BlockContainerBase;

public class BlockMineralExtractor extends BlockContainerBase {
	
	public BlockMineralExtractor(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}
	
}
