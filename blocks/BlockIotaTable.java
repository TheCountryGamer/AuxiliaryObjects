package com.countrygamer.capo.blocks;

import com.countrygamer.capo.blocks.tiles.TileEntityIotaTable;
import com.countrygamer.core.Base.block.BlockContainerBase;
import com.countrygamer.core.lib.CoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Country_Gamer on 3/17/14.
 */
public class BlockIotaTable extends BlockContainerBase {

	public BlockIotaTable (
			Material mat, String modid, String name,
			Class<? extends TileEntity> tileEntityClass) {
		super(mat, modid, name, tileEntityClass);
	}

	public int getRenderType() {
		return -1;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean onBlockActivated(
			World world, int x, int y, int z, EntityPlayer player,
			int side, float x1, float y1, float z1) {
		boolean didSomething = false;
		ItemStack heldStack = player.getHeldItem();

		String textToLog = "\n";

		if (side == 1) {
			boolean validX = 0.1F <= x1 && x1 <= 0.9F;
			boolean validZ = 0.1F <= z1 && z1 <= 0.9F;

			//textToLog += "0.4F < " + x1 + " < 0.6F" + "\n";
			//textToLog += "0.4F < " + z1 + " < 0.6F" + "\n";

			if (validX && validZ) {
				TileEntityIotaTable tileEnt = (TileEntityIotaTable)world.getTileEntity(x, y, z);
				ItemStack currentOreStack = tileEnt.getStackInSlot(0);
				if (currentOreStack != null) {
					if (heldStack != null) {
						if (heldStack.getItem() == Items.stick) {
							tileEnt.addHit();
							return true;
						}
					}
					else {
						this.pullFromTileEntOre(world, tileEnt, currentOreStack, x, y, z);
					}
				}
				else if (heldStack != null && tileEnt.isItemValidForSlot(0, heldStack.copy())) {
					ItemStack stackToInsert = heldStack.copy();
					stackToInsert.stackSize = 1;
					tileEnt.setInventorySlotContents(0, stackToInsert);

					ItemStack returnStack = heldStack.copy();
					--returnStack.stackSize;
					if (returnStack.stackSize <= 0)
						returnStack = null;
					player.setCurrentItemOrArmor(0, returnStack);
					return true;
				}
			}
		}
		return didSomething;
	}

	private void pullFromTileEntOre(
			World world, TileEntityIotaTable tileEnt, ItemStack currentOreStack,
			int x, int y, int z) {
		CoreUtil.dropItemStack(world, currentOreStack, x, y + 1, z);
		tileEnt.setInventorySlotContents(0, null);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5,
	                       int par6) {
		TileEntityIotaTable tileEnt = (TileEntityIotaTable) world.getTileEntity(x, y, z);
		Random rand = new Random();

		if (tileEnt != null) {
			for (int j1 = 0; j1 < tileEnt.getSizeInventory(); j1++) {
				ItemStack itemstack = tileEnt.getStackInSlot(j1);
				if (itemstack != null) {
					float f = rand.nextFloat() * 0.8F + 0.1F;
					float f1 = rand.nextFloat() * 0.8F + 0.1F;
					float f2 = rand.nextFloat() * 0.8F + 0.1F;
					EntityItem entityitem;

					entityitem = new EntityItem(world,
							(double) ((float) x + f),
							(double) ((float) y + f1),
							(double) ((float) z + f2), itemstack.copy());
					float f3 = 0.05F;
					entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
					entityitem.motionY = (double) ((float) rand.nextGaussian()
							* f3 + 0.2F);
					entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);

					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound(
								(NBTTagCompound) itemstack.getTagCompound()
										.copy());
					}
					world.spawnEntityInWorld(entityitem);

				}
			}

			world.func_147453_f(x, y, z, par5);
		}

		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public boolean canHarvestBlock (EntityPlayer player, int meta) {
		return true;
	}


	
}
