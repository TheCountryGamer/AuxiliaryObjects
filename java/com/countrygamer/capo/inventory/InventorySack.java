package com.countrygamer.capo.inventory;

import net.minecraft.item.ItemStack;

import com.countrygamer.capo.items.ItemInventorySack;
import com.countrygamer.core.Base.inventory.InventoryItemBase;

public class InventorySack extends InventoryItemBase {
	
	public InventorySack(ItemStack itemstack) {
		super("Player's Sack", 36, itemstack, ItemInventorySack.class);
	}
	
}
