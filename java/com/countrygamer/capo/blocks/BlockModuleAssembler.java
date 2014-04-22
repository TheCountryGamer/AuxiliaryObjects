package com.countrygamer.capo.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.capo.blocks.tiles.TileEntityAssembler;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.core.Base.block.BlockContainerBase;

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
