package com.countrygamer.capo.common.inventory;

import net.minecraft.item.ItemStack;

import com.countrygamer.capo.common.item.ItemInventorySack;
import com.countrygamer.core.Base.common.inventory.InventoryItemBase;

public class InventorySack extends InventoryItemBase {
	
	public InventorySack(ItemStack itemstack) {
		super("Player's Sack", 36, itemstack, ItemInventorySack.class);
	}
	
}
