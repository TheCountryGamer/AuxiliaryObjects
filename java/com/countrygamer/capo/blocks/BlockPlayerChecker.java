package com.countrygamer.capo.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityPlayerChecker;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.core.Base.block.BlockContainerBase;

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
		super.onNeighborBlockChange(world, x, y, z, block);
		// this.isProvidingStrongPower(world, x, y, z, 0);
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess access, int x, int y, int z, int side) {
		// return 0 if off
		// else return 15 (max power output of a torch)
		TileEntityPlayerChecker tileEnt = (TileEntityPlayerChecker) access.getTileEntity(x, y, z);
		int power = tileEnt.getRedstonePower();
		Capo.log.info(power + "");
		
		
		
		return power;
		/*
		if (access.getTileEntity(x, y, z) != null
				&& access.getTileEntity(x, y, z) instanceof TileEntityPlayerChecker) {
			TileEntityPlayerChecker tileEnt = (TileEntityPlayerChecker) access.getTileEntity(x, y,
					z);
			if (tileEnt.isActivePlayerOnline()) {
				return 15;
			}
			else {
				//Capo.log.info("No online players");
			}
		}
		else {
			Capo.log.info("Error Getting TileEntityPlayerChecker");
		}
		return 0;
		 */
	}
	
	@Override
	public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int var1) {
		return this.isProvidingWeakPower(access, x, y, z, var1);
	}
	
	public void updateTick(World world, int x, int y, int z, Random random) {
		super.updateTick(world, x, y, z, random);
		if (world.getTileEntity(x, y, z) != null
				&& world.getTileEntity(x, y, z) instanceof TileEntityPlayerChecker) {
			TileEntityPlayerChecker tileEnt = (TileEntityPlayerChecker) world
					.getTileEntity(x, y, z);
			tileEnt.refreshPlayers();
		}
	}
	
}
