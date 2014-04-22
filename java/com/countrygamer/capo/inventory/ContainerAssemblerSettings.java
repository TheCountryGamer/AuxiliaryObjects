package com.countrygamer.capo.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.items.ItemModuleWall;
import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.Base.inventory.ContainerBlockBase;
import com.countrygamer.core.Base.inventory.FakeInventory;
import com.countrygamer.core.Base.inventory.GhostSlot;

public class ContainerAssemblerSettings extends ContainerBlockBase {
	
	FakeInventory fi;
	
	public ContainerAssemblerSettings(InventoryPlayer invPlayer, TileEntityInventoryBase tileEnt) {
		super(invPlayer, tileEnt);
		ItemStack moduleStack = tileEnt.getStackInSlot(0);
		if (moduleStack != null && moduleStack.getItem() instanceof ItemModuleWall) {
			this.fi = new FakeInventory(moduleStack, 1, 1);
			this.fi.setInventorySlotContents(0,
					((ItemModuleWall) moduleStack.getItem()).loadCamoStack(moduleStack));
		}
		
		this.inventorySlots.clear();
		this.registerSlots(invPlayer);
	}
	
	public void registerSlots(InventoryPlayer invPlayer) {
		if (fi != null) {
			this.addSlotToContainer(new SlotModuleCamo(this.fi, 0, 10, 10));
		}
		
		this.registerPlayerSlots(invPlayer, 0, 0);
	}
	
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		
		ItemStack moduleStack = this.tileEnt.getStackInSlot(0);
		if (moduleStack != null && moduleStack.getItem() instanceof ItemModuleWall) {
			ItemModuleWall module = (ItemModuleWall) moduleStack.getItem();
			
			ItemStack camoStack = this.getSlotFromInventory(this.fi, 0).getStack();
			moduleStack.setTagCompound(module.saveCamoStack(moduleStack, camoStack));
		}
		
	}
	
	@Override
	public ItemStack slotClick(int slotid, int mouseButton, int modifier, EntityPlayer player) {
		Slot slot = slotid < 0 ? null : (Slot) this.inventorySlots.get(slotid);
		if (slot != null && slot instanceof GhostSlot) {
			GhostSlot ghSlot = (GhostSlot) slot;
			return ghSlot.ghostSlotClick(ghSlot, mouseButton, modifier, player);
		}
		return super.slotClick(slotid, mouseButton, modifier, player);
	}
	
}
