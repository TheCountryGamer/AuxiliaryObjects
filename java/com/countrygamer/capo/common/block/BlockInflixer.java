package com.countrygamer.capo.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.core.Base.common.block.BlockContainerBase;

public class BlockInflixer extends BlockContainerBase {
	
	public BlockInflixer(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		if (!player.isSneaking()) {
			player.openGui(Capo.instance, Reference.guiInflixer, world, x, y, z);
			return true;
		}
		return false;
	}
	
}
