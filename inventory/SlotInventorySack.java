package com.countrygamer.auxiliaryobjects.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.auxiliaryobjects.items.ItemInventorySack;

public class SlotInventorySack extends Slot {
	
	String	currentSackName	= "";
	
	public SlotInventorySack(IInventory par1iInventory, int par2, int par3, int par4,
			ItemStack sackStack) {
		super(par1iInventory, par2, par3, par4);
		NBTTagCompound tagCom = new NBTTagCompound();
		if (sackStack.hasTagCompound()) tagCom = sackStack.getTagCompound();
		this.currentSackName = tagCom.getString(ItemInventorySack.sackName);
	}
	
	// This is the only method we need to override so that
	// we can't place our inventory-storing Item within
	// its own inventory (thus making it permanently inaccessible)
	// as well as preventing abuse of storing backpacks within backpacks
	/**
	 * Check if the stack is a valid item for this slot.
	 */
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		// Everything returns true except an instance of our Item
		NBTTagCompound tagCom = itemStack.getTagCompound();
		if (tagCom != null) {
			String sackName = tagCom.getString(ItemInventorySack.sackName);
			if (sackName.equals("") || sackName.equals(this.currentSackName)) {
				return false;
			}
		}
		return true;
	}
}
