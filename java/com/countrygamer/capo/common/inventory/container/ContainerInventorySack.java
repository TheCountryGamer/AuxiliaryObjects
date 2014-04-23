package com.countrygamer.capo.common.inventory.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import com.countrygamer.capo.common.inventory.container.slot.SlotInventorySack;
import com.countrygamer.core.Base.common.inventory.ContainerItemBase;
import com.countrygamer.core.Base.common.inventory.InventoryItemBase;

public class ContainerInventorySack extends ContainerItemBase {
	
	public ContainerInventorySack(EntityPlayer player, InventoryPlayer inventoryPlayer,
			InventoryItemBase inventoryItem) {
		super(player, inventoryPlayer, inventoryItem);
	}
	
	@Override
	protected void registerSlots(InventoryPlayer inventoryPlayer) {
		int i;
		int yoffset = -82;
		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				int id = j + i * 9 + 9;
				if (id < this.inventory.getSizeInventory())
					this.addSlotToContainer(new SlotInventorySack(this.inventory, id, 8 + j * 18,
							98 + i * 18 + yoffset, inventoryPlayer.getCurrentItem()));
			}
		}
		
		for (i = 0; i < 9; ++i) {
			if (i < this.inventory.getSizeInventory())
				this.addSlotToContainer(new SlotInventorySack(this.inventory, i, 8 + i * 18,
						156 + yoffset, inventoryPlayer.getCurrentItem()));
		}
		
		this.registerPlayerSlots(inventoryPlayer, 0, 14);
	}
	
}
