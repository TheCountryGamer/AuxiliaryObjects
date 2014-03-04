package com.countrygamer.auxiliaryobjects.blocks.tiles;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.core.block.tiles.TileEntityInventoryBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityIncubator extends TileEntityInventoryBase {

	public int furnaceCookTime;
	public final int maxCookTime = 400;

	public TileEntityIncubator() {
		super("Incubator", 7, 16);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isItemValidForSlot(int slotiD, ItemStack itemStack) {
		switch (slotiD) {
		case 0:
			return itemStack.getItem() == Items.wheat;
		case 1:
			return itemStack.getItem() == Items.beef;
		case 2:
			return itemStack.getItem() == Items.leather;
		case 3:
			return itemStack.getItem() == Items.leather;
		case 4:
			return itemStack.getItem() == Items.leather;
		case 5:
			return itemStack.getItem() == Items.egg;
		default:
			return false;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setShort("CookTime", (short) this.furnaceCookTime);

	}

	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.furnaceCookTime = tagCom.getShort("CookTime");

	}

	private boolean canIncubate() {
		if (this.hasNullInputs())
			return false;
		else {
			ItemStack itemstack = IncubationRecipies.smelting()
					.getIncubationResult(this.inv[0], this.inv[1],
							this.inv[2], this.inv[3],
							this.inv[4]);

			if (itemstack == null)
				return false;
			if (this.inv[6] == null)
				return true;
			if (!this.inv[6].isItemEqual(itemstack))
				return false;
			int result = inv[6].stackSize + itemstack.stackSize;
			return (result <= getInventoryStackLimit() && result <= itemstack
					.getMaxStackSize());
		}
	}

	private boolean hasNullInputs() {
		boolean hasNull = false;
		for (int i = 0; i < 6; i++) {
			hasNull = this.inv[i] == null;
		}
		return hasNull;
	}

	public void smeltItem() {
		if (this.canIncubate()) {
			ItemStack itemstack = IncubationRecipies.smelting()
					.getIncubationResult(this.inv[0], this.inv[1],
							this.inv[2], this.inv[3],
							this.inv[4]);

			if (this.inv[6] == null)
				this.inv[6] = itemstack.copy();
			else if (this.inv[6].isItemEqual(itemstack))
				inv[5].stackSize += itemstack.stackSize;

			for (int i = 0; i < 6; i++) {
				--this.inv[i].stackSize;
			}

			for (int i = 0; i < 6; i++) {
				if (this.inv[i].stackSize <= 0)
					this.inv[i] = null;
			}
		}
	}

	public void updateEntity() {
		if (this.canIncubate()) {
			++this.furnaceCookTime;
			if (this.furnaceCookTime == this.maxCookTime) {
				this.smeltItem();
				this.furnaceCookTime = 0;
			}
		} else {
			this.furnaceCookTime = 0;
		}

		this.markDirty();
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Returns an integer between 0 and the passed value representing how close the current item is to being completely
	 * cooked
	 */
	public int getCookProgressScaled(int par1) {
		return this.furnaceCookTime * par1 / this.maxCookTime;
	}

	private void spreadInv234() {
		if (this.inv[2] != null || this.inv[3] != null || this.inv[4] != null) {

		}
	}

}
