package com.countrygamer.capo.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.blocks.tiles.TileEntityIncubator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerIncubator extends Container {
	private TileEntityIncubator tileEnt;
	private int lastCookTime;

	// private int lastBurnTime;
	// private int lastItemBurnTime;

	public ContainerIncubator(InventoryPlayer player,
			TileEntityIncubator tileEnt) {
		this.tileEnt = tileEnt;

		this.addSlotToContainer(new Slot(tileEnt, 1, 27, 17));
		this.addSlotToContainer(new Slot(tileEnt, 0, 27, 35));
		this.addSlotToContainer(new Slot(tileEnt, 2, 9, 35));
		this.addSlotToContainer(new Slot(tileEnt, 4, 45, 35));
		this.addSlotToContainer(new Slot(tileEnt, 3, 27, 53));
		this.addSlotToContainer(new Slot(tileEnt, 5, 88, 35));
		this.addSlotToContainer(new Slot(tileEnt, 6, 135, 35));

		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
		}
	}

	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0,
				this.tileEnt.furnaceCookTime);
		// par1ICrafting.sendProgressBarUpdate(this, 1,
		// this.tileEnt.tileEntBurnTime);
		// par1ICrafting.sendProgressBarUpdate(this, 2,
		// this.tileEnt.currentItemBurnTime);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);

			if (this.lastCookTime != this.tileEnt.furnaceCookTime) {
				icrafting.sendProgressBarUpdate(this, 0,
						this.tileEnt.furnaceCookTime);
			}

			/*
			 * if (this.lastBurnTime != this.tileEnt.tileEntBurnTime) {
			 * icrafting.sendProgressBarUpdate(this, 1,
			 * this.tileEnt.tileEntBurnTime); }
			 * 
			 * if (this.lastItemBurnTime != this.tileEnt.currentItemBurnTime) {
			 * icrafting.sendProgressBarUpdate(this, 2,
			 * this.tileEnt.currentItemBurnTime); }
			 */
		}

		this.lastCookTime = this.tileEnt.furnaceCookTime;
		// this.lastBurnTime = this.tileEnt.tileEntBurnTime;
		// this.lastItemBurnTime = this.tileEnt.currentItemBurnTime;
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		if (par1 == 0) {
			this.tileEnt.furnaceCookTime = par2;
		}
		/*
		 * if (par1 == 1) { this.tileEnt.tileEntBurnTime = par2; }
		 * 
		 * if (par1 == 2) { this.tileEnt.currentItemBurnTime = par2; }
		 */
	}

	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return this.tileEnt.isUseableByPlayer(par1EntityPlayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	/*
	 * MergeItemStack:
	 * 
	 * @param itemStack ItemStack to merge into inventory
	 * 
	 * @param start minimum slot to attempt fill (inclusive)
	 * 
	 * @param end maximum slot to attempt fill (exclisive)
	 * 
	 * @param backwards go backwards
	 * 
	 * @return true if stacks merged successfully
	 */
	public ItemStack transferStackInSlot(EntityPlayer player, int slotiD) {
		// shift out of input
		// shift out of output
		// shift into input

		ItemStack randomItemStack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotiD);

		if (slot != null && slot.getHasStack()) {
			ItemStack stackInClickedSlot = slot.getStack();
			stackInClickedSlot = stackInClickedSlot.copy();

			if (slotiD <= 6) { // clicked on input slots or output slot
				// attempts to send param;ItemStack to slots; if failed,
				// returns false
				if (!this.mergeItemStack(stackInClickedSlot,
						this.tileEnt.getSizeInventory() + 0, 43, false)) {
					// shifted player inventory max excluded = 43
					return null;
				}
			}
			if (slotiD >= 7) { // clicked on player inventory
				insertLoop: for (int i = 0; i < 6; i++) {
					if (this.tileEnt.isItemValidForSlot(i, stackInClickedSlot)) {
						if (!this.mergeItemStack(stackInClickedSlot, i, i + 1,
								false)) {
							return null;
						} else
							break insertLoop;
					}
				}
			}

			if (stackInClickedSlot.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (stackInClickedSlot.stackSize == stackInClickedSlot.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, stackInClickedSlot);

		}
		return randomItemStack;
	}

}
