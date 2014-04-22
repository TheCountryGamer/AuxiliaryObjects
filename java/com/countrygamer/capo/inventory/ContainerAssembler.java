package com.countrygamer.capo.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.items.ItemModule;
import com.countrygamer.capo.packet.PacketTriggerAssembler;
import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.Base.inventory.ContainerBlockBase;

public class ContainerAssembler extends ContainerBlockBase {
	
	public ContainerAssembler(InventoryPlayer invPlayer, TileEntityInventoryBase tileEnt) {
		super(invPlayer, tileEnt);
	}
	
	@Override
	public void registerSlots(InventoryPlayer invPlayer) {
		this.addSlotToContainer(new SlotModule(this.tileEnt, 0, 48, 21));
		
		this.addSlotToContainer(new Slot(this.tileEnt, 1, 101, 21));
		this.addSlotToContainer(new Slot(this.tileEnt, 2, 119, 21));
		this.addSlotToContainer(new Slot(this.tileEnt, 3, 101, 39));
		this.addSlotToContainer(new Slot(this.tileEnt, 4, 119, 39));
		
		this.registerPlayerSlots(invPlayer, 0, 0);
	}
	
	public ItemStack slotClick(int slot, int par2, int par3, EntityPlayer player) {
		/*
		if (slot == 0) {
			InventoryPlayer invPlayer = player.inventory;
			ItemStack cursorStack = invPlayer.getItemStack();
			String name = cursorStack != null ? cursorStack.getDisplayName() : "NULL";
			Capo.log.info("Cursor Stack Name: " + name);
			if (cursorStack != null && cursorStack.getItem() instanceof ItemModule) {
				// took item
				Capo.log.info("Save module upgrades");
				ItemStack[] upgrades = new ItemStack[4];
				for (int i = 0; i < 4; i++) {
					upgrades[i] = this.tileEnt.getStackInSlot(i + 1);
				}
				((ItemModule)cursorStack.getItem()).saveUpgrades(cursorStack, upgrades);
			}
			else if (cursorStack == null){
				PacketTriggerAssembler packet = new PacketTriggerAssembler(this.tileEnt.xCoord,
						this.tileEnt.yCoord, this.tileEnt.zCoord, "loadUpgrades");
				Capo.packetChannel.sendToServer(packet);
				Capo.packetChannel.sendToAll(packet);
			}
		}
		*/
		return super.slotClick(slot, par2, par3, player);
	}
	
}
