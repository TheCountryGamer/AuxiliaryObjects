package com.countrygamer.capo.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCompressorOut extends Slot {

	public SlotCompressorOut(IInventory inv, int id, int x, int y) {
		super(inv, id, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return false;
	}
	
}
