package com.countrygamer.capo.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.tileentity.TileEntityModuleBase;
import com.countrygamer.core.Base.common.block.BlockContainerBase;

public class BlockModuleBase extends BlockContainerBase {
	
	public BlockModuleBase(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
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
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		if (!player.isSneaking()) {
			player.openGui(Reference.MOD_ID, Reference.guiModuleBase, world, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		TileEntity tEnt = world.getTileEntity(x, y, z);
		if (tEnt != null && tEnt instanceof TileEntityModuleBase) {
			TileEntityModuleBase tileEnt = (TileEntityModuleBase)tEnt;
			if (tileEnt.hasWall)
				tileEnt.emptyField();
		}
		
		super.breakBlock(world, x, y, z, block, meta);
	}
}
