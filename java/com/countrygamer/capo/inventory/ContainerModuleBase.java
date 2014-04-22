package com.countrygamer.capo.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.Base.inventory.ContainerBlockBase;

public class ContainerModuleBase extends ContainerBlockBase {

	public ContainerModuleBase(InventoryPlayer invPlayer, TileEntityInventoryBase tileEnt) {
		super(invPlayer, tileEnt);
	}
	
	@Override
	protected void registerSlots(InventoryPlayer inv) {
		
		this.addSlotToContainer(new Slot(this.tileEnt, 0, 20, 20));
		this.addSlotToContainer(new SlotModuleCamo(this.tileEnt, 1, 40, 20));
		
		
		this.registerPlayerSlots(inv, 0, 0);
	}

}

