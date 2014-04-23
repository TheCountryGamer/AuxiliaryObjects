package com.countrygamer.capo.common.inventory.container.slot;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.oredict.OreDictionary;

import com.countrygamer.capo.common.item.ItemMultiItem;

public class SlotInflix extends Slot {
	
	public SlotInflix(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	/**
	 * Check if the stack is a valid item for this slot.
	 */
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		boolean valid = false;
		
		if (itemStack == null) return valid;
		
		Item item = itemStack.getItem();
		
		// TODO check if armor- this works, but need to implement allowing the MultiItem in armor
		// slots
		// for (int i = 0; i < 4; i++)
		// valid = valid || item.isValidArmor(itemStack, i, null);
		// check if tool
		valid = valid || (item.isItemTool(itemStack) && item instanceof ItemTool);
		// check if hoe
		valid = valid || item instanceof ItemHoe;
		// check if potion
		valid = valid || item instanceof ItemPotion;
		// check if bucket
		valid = valid || item instanceof ItemBucket;
		// check for bow
		valid = valid || item instanceof ItemBow;
		// check for shears
		valid = valid || item instanceof ItemShears;
		// check for sword
		valid = valid || item instanceof ItemSword;
		// check for dyes
		// ArrayList<ItemStack> dyeList;
		for (int i = 0; i < 16; i++) {
			ItemStack dyeStack = new ItemStack(Items.dye, 1, i);
			int oreID = OreDictionary.getOreID(dyeStack);
			int insertOreID = OreDictionary.getOreID(itemStack);
			valid = valid || (insertOreID == oreID);
		}
		//valid = valid || item instanceof ItemDye;
		
		
		return valid && !(item instanceof ItemMultiItem);
	}
}
