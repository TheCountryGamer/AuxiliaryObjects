package com.countrygamer.capo.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.common.item.ItemTriDye;

public class SlotTriDye extends Slot {
	
	public SlotTriDye(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return (itemStack.getItem() instanceof ItemTriDye);
	}
	
}
