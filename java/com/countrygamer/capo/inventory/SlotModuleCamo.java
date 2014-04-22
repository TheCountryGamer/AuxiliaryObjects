package com.countrygamer.capo.inventory;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.countrygamer.core.Base.inventory.GhostSlot;

public class SlotModuleCamo extends GhostSlot {
	
	
	public SlotModuleCamo(IInventory inv, int slotID, int x, int y) {
		super(inv, slotID, x, y);
	}

	public boolean isItemValid(ItemStack itemStack) {
		if (itemStack != null) {
			Block block = Block.getBlockFromItem(itemStack.getItem());
			if (block != Blocks.air) {
				return block.getRenderType() == 0;// && block.renderAsNormalBlock();
			}
		}
		return false;
	}
	
}
