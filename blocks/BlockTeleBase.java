package com.countrygamer.capo.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityTele;
import com.countrygamer.capo.items.ItemTeleCore;
import com.countrygamer.core.Core;
import com.countrygamer.core.Base.block.BlockContainerBase;
import com.countrygamer.core.lib.CoreUtil;

public class BlockTeleBase extends BlockContainerBase {

	private ItemStack redstoneStack = new ItemStack(Items.redstone);
	private ItemStack enderStack = new ItemStack(Items.ender_pearl);
	private ItemStack eggStack = new ItemStack(Items.egg);
	private ItemStack goldStack = new ItemStack(Blocks.gold_block);

	public BlockTeleBase(Material mat, String modid, String name) {
		super(mat, modid, name, TileEntityTele.class);
		this.setBlockBounds(0.19F, 0.0F, 0.19F, 0.81F, 1.0F, 0.81F);
		this.setHardness(2.0F);
		this.setResistance(2000.0F);
		this.setStepSound(Block.soundTypeStone);
		this.setHarvestLevel("pickaxe", 0);
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

	// On Clicked Things
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float par7, float par8, float par9) {
		if (world.getTileEntity(x, y, z) != null) {
			TileEntityTele tileEnt = (TileEntityTele) world.getTileEntity(x, y,
					z);
			ItemStack heldStack = player.getHeldItem();
			ItemStack coreStack = tileEnt.getStackInSlot(0);

			if (heldStack != null
					&& this.activatedWithoutCore(tileEnt, player, heldStack, x,
							y, z))
				return true;

			if (Core.DEBUG)
				Capo.log.info("TeleBase Clicked");
			if (coreStack == null) { // is slot is empty
				if (heldStack != null) {
					if (heldStack.getItem() instanceof ItemTeleCore
							&& heldStack.hasTagCompound()) {
						if (Core.DEBUG)
							Capo.log.info("Try to insert core");
						if (Core.DEBUG)
							Capo.log.info(""
									+ tileEnt.isItemValidForSlot(0, heldStack));
						// Insert Core
						if (tileEnt.isItemValidForSlot(0, heldStack)
								&& heldStack.getTagCompound().getBoolean(
										"isActive")) {
							tileEnt.setInventorySlotContents(0, heldStack);
							player.setCurrentItemOrArmor(0, null);
							return true;
						}
					}
				}
			} else {
				if (!player.isSneaking()) {
					// teleport the player
					this.teleport(coreStack, player, tileEnt);
				} else {
					// drop slot stack
					if (!world.isRemote) {
						CoreUtil.dropItemStack(world,
								new ItemStack(coreStack.getItem()), x, y, z,
								coreStack.getTagCompound());
					}
					tileEnt.setInventorySlotContents(0, null);
				}
				return true;
			}
		}
		return false;
	}

	private void teleport(ItemStack coreStack, EntityPlayer player,
			TileEntityTele tileEnt) {
		ItemTeleCore core = (ItemTeleCore) coreStack.getItem();
		if (coreStack.getItem() == Capo.unStableCore) {
			int potionLengthSec = 20;
			NBTTagCompound coordsTag = coreStack.getTagCompound()
					.getCompoundTag("coords");
			double[] coords = new double[] { coordsTag.getDouble(core.coorX),
					coordsTag.getDouble(core.coorY),
					coordsTag.getDouble(core.coorZ) };
			if (CoreUtil.chance(50)) { // random new coords
				coords = CoreUtil.teleportBase(player.worldObj, player, 0, 20,
						player.posX, player.posZ);
				potionLengthSec *= 2;
			}
			player.addPotionEffect(new PotionEffect(Potion.confusion.id,
					20 * potionLengthSec, 1));
			CoreUtil.teleportPlayer(player, coords[0], coords[1], coords[2],
					true, false);

		}
		if (coreStack.getItem() == Capo.stableCore) {
			if (!tileEnt.isSlotEmpty("ender"))
				CoreUtil.teleportPlayerToDimension(player, coreStack
						.getTagCompound().getInteger(core.dimensionID));
			NBTTagCompound coords = coreStack.getTagCompound().getCompoundTag(
					"coords");
			CoreUtil.teleportPlayer(player, coords.getDouble(core.coorX),
					coords.getDouble(core.coorY), coords.getDouble(core.coorZ),
					false, false);
		}

	}

	private boolean activatedWithoutCore(TileEntityTele tileEnt,
			EntityPlayer player, ItemStack itemStack, int x, int y, int z) {
		for (int slotiD = 0; slotiD < tileEnt.getSizeInventory(); slotiD++) {
			if (tileEnt.isItemValidForSlot(slotiD, itemStack)) {
				ItemStack newStack = itemStack.copy();
				newStack.stackSize = 1;
				tileEnt.setInventorySlotContents(slotiD, newStack);
				player.setCurrentItemOrArmor(
						0,
						new ItemStack(itemStack.getItem(),
								itemStack.stackSize - 1, itemStack
										.getItemDamage()));
				return true;
			}
		}
		return false;
	}

	// With Redstone Stuff
	public void onNeighborBlockChange(World world, int x, int y, int z,
			int newID) {
		TileEntityTele tileEnt = (TileEntityTele) world.getTileEntity(x, y, z);
		if (tileEnt != null) {
			if (!tileEnt.isSlotEmpty("redstone")) {
				boolean powered = world
						.isBlockIndirectlyGettingPowered(x, y, z)
						|| world.isBlockIndirectlyGettingPowered(x, y - 1, z);

				tileEnt.isPowered = powered;
			}
		}
	}

	// When Block Broken
	@Override
	public void breakBlock(World world, int x, int y, int z, Block par5,
			int par6) {
		TileEntityTele tileEnt = (TileEntityTele) world.getTileEntity(x, y, z);
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
	
}
