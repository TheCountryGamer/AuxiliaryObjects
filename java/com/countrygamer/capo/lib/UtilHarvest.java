package com.countrygamer.capo.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Class Created by Country_Gamer on 3/16/14.
 * @author Methods created by Azanor (Thaumcraft). Modified to fit uses of Vein Miner item (Vainer).
 */
public class UtilHarvest {

	static int lastx = 0;
	static int lasty = 0;
	static int lastz = 0;
	static double lastdistance = 0.0D;

	public static boolean isWoodLog(World world, int x, int y, int z)
	{
		Block bi = world.getBlock(x, y, z);
		//int md = world.getBlockMetadata(x, y, z);
		// if (Blocks.blocksList[bi] == null) return false;
		// if (Blocks.blocksList[bi].canSustainLeaves(world, x, y, z)) return true;

		Block[] ores = new Block[] {
				Blocks.coal_ore,
				Blocks.iron_ore,
				Blocks.gold_ore,
				Blocks.diamond_ore,
				Blocks.lapis_ore,
				Blocks.redstone_ore,
				Blocks.emerald_ore
		};

		ArrayList<Block> oreIDSet = new ArrayList<Block>();
		for (int i = 0; i < ores.length; i++) {
			oreIDSet.add(ores[i]);
		}

		return oreIDSet.contains(bi);

		//return ItemElementalAxe.oreDictLogs.contains(Arrays.asList(new Integer[] { Integer.valueOf(bi), Integer.valueOf(md) }));
	}

	public static boolean breakFurthestBlock(World world, int x, int y, int z, Block bi, EntityPlayer player)
	{
		lastx = x;
		lasty = y;
		lastz = z;
		lastdistance = 0.0D;

		findBlocks(world, x, y, z, bi);

		boolean worked = harvestBlock(world, player, lastx, lasty, lastz);

		if (worked) {
			for (int xx = -3; xx <= 3; xx++) {
				for (int yy = -3; yy <= 3; yy++)
					for (int zz = -3; zz <= 3; zz++)
						world.scheduleBlockUpdate(lastx + xx, lasty + yy, lastz + zz, world.getBlock(lastx + xx, lasty + yy, lastz + zz), 150 + world.rand.nextInt(150));
			}
		}
		return worked;
	}

	private static void findBlocks(World world, int x, int y, int z, Block bi) {
		//int count = 0;
		for (int xx = -2; xx <= 2; xx++)
			for (int yy = 2; yy >= -2; yy--)
				for (int zz = -2; zz <= 2; zz++) {
					if (Math.abs(lastx + xx - x) > 24) return;
					if (Math.abs(lasty + yy - y) > 48) return;
					if (Math.abs(lastz + zz - z) > 24) return;
					if ((world.getBlock(lastx + xx, lasty + yy, lastz + zz) != bi) || (!isWoodLog(world, lastx + xx, lasty + yy, lastz + zz)))
						continue;
					double xd = lastx + xx - x;
					double yd = lasty + yy - y;
					double zd = lastz + zz - z;
					double d = xd * xd + yd * yd + zd * zd;
					if (d > lastdistance) {
						lastdistance = d;
						lastx += xx;
						lasty += yy;
						lastz += zz;
						findBlocks(world, x, y, z, bi);
						return;
					}
				}
	}

	public static boolean harvestBlock(World world, EntityPlayer player, int x, int y, int z)
	{
		Block l = world.getBlock(x, y, z);
		int i1 = world.getBlockMetadata(x, y, z);

		//world.playAuxSFX(2001, x, y, z, l + (i1 << 12));
		boolean flag = false;

		if (player.capabilities.isCreativeMode)
		{
			flag = removeBlock(world, x, y, z, player);
		}
		else
		{
			boolean flag1 = false;
			Block block = l;
			if (block != null)
			{
				flag1 = block.canHarvestBlock(player, i1);
			}

			flag = removeBlock(world, x, y, z, player);
			if ((flag) && (flag1))
			{
				block.harvestBlock(world, player, x, y, z, i1);
			}
		}
		return true;
	}

	private static boolean removeBlock(World world, int par1, int par2, int par3, EntityPlayer player)
	{
		Block block = world.getBlock(par1, par2, par3);
		int l = world.getBlockMetadata(par1, par2, par3);

		if (block != null)
		{
			block.onBlockHarvested(world, par1, par2, par3, l, player);
		}

		boolean flag = (block != null) && (block.removedByPlayer(world, player, par1, par2, par3));

		if ((block != null) && (flag))
		{
			block.onBlockDestroyedByPlayer(world, par1, par2, par3, l);
		}

		return flag;
	}


}
