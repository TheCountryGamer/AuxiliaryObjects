package com.countrygamer.capo.common.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import com.countrygamer.capo.common.inventory.container.slot.SlotTriDye;
import com.countrygamer.core.Base.common.inventory.ContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

public class ContainerColorizerII extends ContainerBlockBase {

	public ContainerColorizerII(InventoryPlayer invPlayer, TileEntityInventoryBase tileEnt) {
		super(invPlayer, tileEnt);
	}
	
	@Override
	protected void registerSlots(InventoryPlayer invPlayer) {
		this.addSlotToContainer(new Slot(this.tileEnt, 0, 101, 25));
		this.addSlotToContainer(new SlotTriDye(this.tileEnt, 1, 148, 61));
		
		this.registerPlayerSlots(invPlayer, 0, 0);
	}
	
}
