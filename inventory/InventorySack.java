package com.countrygamer.auxiliaryobjects.inventory;

import net.minecraft.item.ItemStack;

import com.countrygamer.auxiliaryobjects.items.ItemInventorySack;
import com.countrygamer.core.inventory.InventoryItemBase;

public class InventorySack extends InventoryItemBase {
	
	public InventorySack(ItemStack itemstack) {
		super("Player's Sack", 36, itemstack, ItemInventorySack.class);
	}
	
}
