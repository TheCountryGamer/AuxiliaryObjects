package com.countrygamer.auxiliaryobjects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.countrygamer.auxiliaryobjects.AuxiliaryObjects;
import com.countrygamer.auxiliaryobjects.blocks.tiles.TileEntityPlayerChecker;
import com.countrygamer.auxiliaryobjects.lib.Reference;
import com.countrygamer.core.block.BlockContainerBase;

public class BlockPlayerChecker extends BlockContainerBase {
	
	public BlockPlayerChecker(String modid, String name, Class<? extends TileEntity> entityClass) {
		super(Material.rock, modid, name, entityClass);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		TileEntityPlayerChecker tileEnt = (TileEntityPlayerChecker) world.getTileEntity(x, y, z);
		tileEnt.refreshPlayers();
		// for (String name : tileEnt.onlinePlayers.keySet())
		// player.addChatMessage(new ChatComponentText(name));
		
		player.openGui(Reference.MOD_ID, Reference.guiPlayerChecker, world, x, y, z);
		
		return true;
	}
	
	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		this.isProvidingStrongPower(world, x, y, z, 0);
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int var1) {
		// return 0 if off
		// else return 15 (max power output of a torch)
		if (access.getTileEntity(x, y, z) != null
				&& access.getTileEntity(x, y, z) instanceof TileEntityPlayerChecker) {
			TileEntityPlayerChecker tileEnt = (TileEntityPlayerChecker) access.getTileEntity(x, y,
					z);
			if (tileEnt.isActivePlayerOnline()) {
				for (String name : tileEnt.activePlayerNames)
					AuxiliaryObjects.log.info(name);
				return 15;
			}
			else {
				AuxiliaryObjects.log.info("No online players");
			}
		}
		else {
			AuxiliaryObjects.log.info("Error Getting TileEntityPlayerChecker");
		}
		return 0;
	}
	
	@Override
	public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int var1) {
		return this.isProvidingWeakPower(access, x, y, z, var1);
	}
	
}