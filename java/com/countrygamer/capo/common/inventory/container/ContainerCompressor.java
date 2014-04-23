package com.countrygamer.capo.common.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import com.countrygamer.capo.common.inventory.container.slot.SlotCompressorOut;
import com.countrygamer.core.Base.common.inventory.ContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

public class ContainerCompressor extends ContainerBlockBase {
	
	public ContainerCompressor(InventoryPlayer invPlayer, TileEntityInventoryBase tileEnt) {
		super(invPlayer, tileEnt);
	}

	@Override
	protected void registerSlots(InventoryPlayer inventoryPlayer) {
		this.addSlotToContainer(new Slot(this.tileEnt, 0, 17, 7));
		this.addSlotToContainer(new SlotCompressorOut(this.tileEnt, 1, 17, 57));
		
		this.registerPlayerSlots(inventoryPlayer, 0, 0);
	}
	
}
