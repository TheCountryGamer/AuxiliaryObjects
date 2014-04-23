package com.countrygamer.capo.common.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import com.countrygamer.capo.common.inventory.container.slot.SlotModuleCamo;
import com.countrygamer.core.Base.common.inventory.ContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

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

