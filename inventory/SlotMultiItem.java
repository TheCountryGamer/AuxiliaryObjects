package com.countrygamer.capo.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.items.ItemMultiItem;

public class SlotMultiItem extends Slot {
	
	public SlotMultiItem(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	public int getSlotStackLimit() {
		return 1;
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
		return (itemStack.getItem() instanceof ItemMultiItem);
	}
}
