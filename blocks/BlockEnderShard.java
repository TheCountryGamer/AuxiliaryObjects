package com.countrygamer.capo.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityEnderShard;
import com.countrygamer.core.Base.block.BlockContainerBase;

public class BlockEnderShard extends BlockContainerBase {

	public BlockEnderShard(Material mat, String modid, String name) {
		super(mat, modid, name, TileEntityEnderShard.class);
		float f = 0.4F;
		this.setBlockBounds(0.5F - f, f * 0.0F, 0.5F - f, 0.5F + f, f * 1.5F,
				0.5F + f);

	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float par7, float par8, float par9) {

		if (!world.isRemote) {
			TileEntityEnderShard tileEnt = (TileEntityEnderShard) world
					.getTileEntity(x, y, z);
			if (tileEnt == null) {
				Capo.log.info("No Tile Ent");
				return false;
			} else {
				if (tileEnt.getStackInSlot(0) != null) {
					this.dropContents(world, new Random(), tileEnt, x, y, z);
					Capo.log.info("Dropped");
					tileEnt.markDirty();
				}
				return true;
			}

		}

		return false;
	}

	public void breakBlock(World world, int x, int y, int z, Block par5,
			int par6) {
		TileEntityEnderShard tileEnt = (TileEntityEnderShard) world
				.getTileEntity(x, y, z);
		Random random = new Random();
		if (tileEnt != null) {
			this.dropContents(world, random, tileEnt, x, y, z);
			world.func_147453_f(x, y, z, par5);
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	private void dropContents(World world, Random random,
			TileEntityEnderShard tileEnt, double x, double y, double z) {

		for (int j1 = 0; j1 < tileEnt.getSizeInventory(); ++j1) {
			ItemStack itemstack = tileEnt.getStackInSlot(j1);

			if (itemstack != null) {
				float f = random.nextFloat() * 0.8F + 0.1F;
				float f1 = random.nextFloat() * 0.8F + 0.1F;
				EntityItem entityitem;

				for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world
						.spawnEntityInWorld(entityitem)) {
					int k1 = random.nextInt(21) + 10;

					if (k1 > itemstack.stackSize) {
						k1 = itemstack.stackSize;
					}

					itemstack.stackSize -= k1;
					entityitem = new EntityItem(world,
							(double) ((float) x + f),
							(double) ((float) y + f1),
							(double) ((float) z + f2), new ItemStack(
									itemstack.getItem(), k1,
									itemstack.getItemDamage()));
					float f3 = 0.05F;
					entityitem.motionX = (double) ((float) random
							.nextGaussian() * f3);
					entityitem.motionY = (double) ((float) random
							.nextGaussian() * f3 + 0.2F);
					entityitem.motionZ = (double) ((float) random
							.nextGaussian() * f3);

					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound(
								(NBTTagCompound) itemstack.getTagCompound()
										.copy());
					}
				}
				tileEnt.setInventorySlotContents(j1, null);
			}
		}

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

	/*
	 * @SideOnly(Side.CLIENT) public int idPicked(World world, int par2, int
	 * par3, int par4) { return AuxiliaryObjectsMain.endShardItem.itemID; }
	 */

}
