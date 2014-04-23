package com.countrygamer.capo.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.common.item.ItemModule;

public class SlotModule extends Slot {

	public SlotModule(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	public boolean isItemValid(ItemStack itemStack) {
		if (itemStack != null)
			return itemStack.getItem() instanceof ItemModule;
		return false;
	}
	
}
