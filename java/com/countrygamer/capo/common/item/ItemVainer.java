package com.countrygamer.capo.common.item;

import com.countrygamer.capo.common.lib.UtilHarvest;
import com.countrygamer.core.Base.common.item.ItemToolBase;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Country_Gamer on 3/15/14.
 */
public class ItemVainer extends ItemToolBase {

	public static final Block[] stones = new Block[] {
			Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab,
			Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone,
			Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block,
			Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice,
			Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block,
			Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail,
			Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail};

	public static final int[] ores = new int[] {
			Block.getIdFromBlock(Blocks.coal_ore),
			Block.getIdFromBlock(Blocks.iron_ore),
			Block.getIdFromBlock(Blocks.gold_ore),
			Block.getIdFromBlock(Blocks.diamond_ore),
			Block.getIdFromBlock(Blocks.lapis_ore),
			Block.getIdFromBlock(Blocks.redstone_ore),
			Block.getIdFromBlock(Blocks.emerald_ore)
	};

	public ItemVainer(String modid, String name, ToolMaterial mat) {
		super(modid, name, 1.0F, mat, stones);
		this.setHarvestLevel("pickaxe", ToolMaterial.IRON.getHarvestLevel());
	}


	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world,
	                         int x, int y, int z, int par7,
	                         float par8, float par9, float par10) {
		Block bi = world.getBlock(x, y, z);
		if ((!player.isSneaking()) && (bi != Blocks.air) && (UtilHarvest.isWoodLog(world, x, y, z))) {
			if (!world.isRemote) {
				if (UtilHarvest.breakFurthestBlock(world, x, y, z, bi, player)) {
					//world.playSoundEffect(x, y, z, "thaumcraft:bubble", 0.15F, 1.0F);
					itemstack.damageItem(1, player);
					//this.alternateServer = (!this.alternateServer);
				}
			} else {
				player.swingItem();
				itemstack.damageItem(1, player);
				//this.alternateClient = (!this.alternateClient);
			}
		}
		return super.onItemUse(itemstack, player, world, x, y, z, par7, par8, par9, par10);
	}

	public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player)
	{
		World world = player.worldObj;
		Block bi = world.getBlock(x, y, z);

		if ((!player.isSneaking()) && (bi != Blocks.air) && (UtilHarvest.isWoodLog(world, x, y, z))) {
			if (!world.isRemote) {
				UtilHarvest.breakFurthestBlock(world, x, y, z, bi, player);
				//PacketHandler.sendBlockBoilFXPacket(x, y, z, 0.33F, 0.33F, 1.0F, player);
				//world.playSoundEffect(x, y, z, "thaumcraft:bubble", 0.15F, 1.0F);
			}
			itemstack.damageItem(1, player);
			return true;
		}

		return super.onBlockStartBreak(itemstack, x, y, z, player);
	}



}
