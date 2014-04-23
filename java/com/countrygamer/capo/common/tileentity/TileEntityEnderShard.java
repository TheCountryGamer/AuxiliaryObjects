package com.countrygamer.capo.common.tileentity;

import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityEnderShard extends TileEntityInventoryBase {

	// 20 tick = 1 second
	// 60 second = 1 minute
	// 10 minute = 1 day
	private static int controlTime = (20 * 60 * 10) * 2;
	private int oreGenTime = 0;

	public TileEntityEnderShard() {
		super("Ender Shard", 1, 1);
		// TileEntityEnderShard.controlTime = 20 * 30;
		// TileEntityEnderShard.controlTime = (20 * 60 * 60 * 24)*2;
		this.oreGenTime = TileEntityEnderShard.controlTime;
	}

	public void updateEntity() {
		super.updateEntity();
		// AuxiliaryObjectsMain.log.info("Tick Tile Ent");
		int control = TileEntityEnderShard.controlTime;

		if (this.getStackInSlot(0) == null)
			if (this.oreGenTime <= 0) {
				this.oreGenTime = control;
				this.setInventorySlotContents(0, new ItemStack(
						Items.ender_pearl, 1));
				this.markDirty();
				//if (!this.worldObj.isRemote)
				//	Capo.log.info("Gen'd Ender Pearl");
				return;
			} else
				this.oreGenTime--;
		else {
			TileEntity tileEntBelow = this.worldObj.getTileEntity(this.xCoord,
					this.yCoord - 1, this.zCoord);
			if (tileEntBelow != null)
				if (tileEntBelow instanceof IInventory) {
					IInventory tileEntIInv = (IInventory) tileEntBelow;
					if (this.addStack(tileEntIInv, this.getStackInSlot(0))) {
						this.setInventorySlotContents(0, null);
						if (!this.worldObj.isRemote)
							Capo.log.info("Dropped Inv to inventory below");
					}
					this.markDirty();
					tileEntIInv.markDirty();
				}

		}

	}

	private boolean addStack(IInventory inv, ItemStack itemStack) {
		for (int slotiD = 0; slotiD < inv.getSizeInventory(); slotiD++) {
			if (inv.getStackInSlot(slotiD) != null) {
				if (inv.getStackInSlot(slotiD).getItem() == itemStack.getItem()
						&& inv.getStackInSlot(slotiD).getItemDamage() == itemStack
								.getItemDamage()) {
					int size = itemStack.stackSize
							+ inv.getStackInSlot(slotiD).stackSize;
					if (size <= itemStack.getMaxStackSize()) {
						ItemStack addedStack = itemStack.copy();
						addedStack.stackSize = size;
						inv.setInventorySlotContents(slotiD, addedStack);
						return true;
					}
				}
			} else {
				inv.setInventorySlotContents(slotiD, itemStack);
				return true;
			}
		}
		return false;

	}

	@SideOnly(Side.CLIENT)
	private void displayParticles() {
		Random random = new Random();
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		World world = this.getWorldObj();
		for (int i = 0; i < 20; i++) {
			for (int l = 0; l < 4; ++l) {
				double xPar = (double) ((float) x + random.nextFloat());
				double yPar = (double) ((float) y + random.nextFloat());
				double zPar = (double) ((float) z + random.nextFloat());
				double velX = 0.0D;
				double velY = 0.0D;
				double velZ = 0.0D;
				// int i1 = random.nextInt(2) * 2 - 1;
				velX = ((double) random.nextFloat() - 0.5D) * 0.5D;
				velY = ((double) random.nextFloat() - 0.5D) * 0.5D;
				velZ = ((double) random.nextFloat() - 0.5D) * 0.5D;
				// zPar = (double)z + 0.5D + 0.25D * (double)i1;
				// velX = (double)(random.nextFloat() * 2.0F * (float)i1);
				// velZ = (double)(random.nextFloat() * 2.0F * (float)i1);

				world.spawnParticle("portal", xPar, yPar, zPar, velX, velY,
						velZ);
			}
		}
	}

	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);

		this.oreGenTime = tagCom.getShort("OreGenTime");

	}

	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);

		tagCom.setShort("OreGenTime", (short) this.oreGenTime);

	}

	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemStack) {
		return i == 0 && itemStack.getItem() == Items.ender_pearl;
	}

	public void markDirty() {
		super.markDirty();
		if (this.worldObj != null) {
			this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord,
					this.yCoord, this.zCoord);
			this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord,
					this.zCoord, this);

			if (this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord) == Capo.endShard) {
				//this.worldObj.func_96440_m(this.xCoord, this.yCoord,
				//		this.zCoord, CG_AuxiliaryObjects.endShard);
			}
		}

		// this.displayParticles();
	}

}
