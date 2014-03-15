package com.countrygamer.capo.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.items.ItemMultiItem;
import com.countrygamer.core.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.inventory.ContainerBlockBase;

public class ContainerInflixer extends ContainerBlockBase {
	
	SlotInflix	slotInflix;
	
	public ContainerInflixer(InventoryPlayer invPlayer, TileEntityInventoryBase tileEnt) {
		super(invPlayer, tileEnt);
	}
	
	@Override
	protected void registerSlots(InventoryPlayer inventoryPlayer) {
		this.addSlotToContainer(this.slotInflix = new SlotInflix(this.tileEnt, 0, 8, 16));
		this.addSlotToContainer(new SlotMultiItem(this.tileEnt, 1, 98, 16));
		
		this.registerPlayerSlots(inventoryPlayer, 0, 0);
		
	}
	
	@Override
	protected boolean shiftClick(ItemStack itemStack, int slotiD) {
		if (itemStack.getItem() instanceof ItemMultiItem)
			return this.mergeItemStack(itemStack, 1, 2, false);
		else if (this.slotInflix.isItemValid(itemStack))
			return this.mergeItemStack(itemStack, 0, 1, false);
		else
			return false;
	}
	
}
