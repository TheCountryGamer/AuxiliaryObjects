package com.countrygamer.capo.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.Base.inventory.ContainerBlockBase;

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
