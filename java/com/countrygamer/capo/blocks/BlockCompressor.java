package com.countrygamer.capo.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.capo.lib.Reference;
import com.countrygamer.core.Base.block.BlockContainerBase;

public class BlockCompressor extends BlockContainerBase {
	
	public BlockCompressor(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		if (!player.isSneaking()) {
			player.openGui(Reference.MOD_ID, Reference.guiCompressor, world, x, y, z);
			return true;
		}
		return false;
	}
	
}
