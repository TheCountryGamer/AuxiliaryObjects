package com.countrygamer.capo.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.tileentity.TileEntityAssembler;
import com.countrygamer.core.Base.common.block.BlockContainerBase;

public class BlockModuleAssembler extends BlockContainerBase {

	public BlockModuleAssembler(Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		if (!player.isSneaking()) {
			player.openGui(Reference.MOD_ID, Reference.guiAssembler, world, x, y, z);
			
			TileEntity tile = player.worldObj.getTileEntity(x, y, z);
			if (tile instanceof TileEntityAssembler) {
				TileEntityAssembler tileEnt = (TileEntityAssembler)tile;
				tileEnt.hasLoadedUpgrades = false;
			}
			
			return true;
		}
		return false;
	}
	
}
