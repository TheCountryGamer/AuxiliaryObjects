package com.countrygamer.capo.common.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.common.inventory.container.slot.SlotMultiDye;
import com.countrygamer.capo.common.item.ItemMultiDye;
import com.countrygamer.capo.common.tileentity.TileEntityColorizer;
import com.countrygamer.core.Base.common.inventory.ContainerBlockBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerColorizer extends ContainerBlockBase {
	
	private TileEntityColorizer	tileEntColor;
	public double				r, g, b;
	
	public ContainerColorizer(InventoryPlayer invPlayer, TileEntityColorizer tileEnt) {
		super(invPlayer, tileEnt);
		this.tileEntColor = ((TileEntityColorizer) this.tileEnt);
	}
	
	@Override
	protected void registerSlots(InventoryPlayer inventoryPlayer) {
		this.addSlotToContainer(new Slot(this.tileEnt, 0, 8, 16));
		this.addSlotToContainer(new SlotMultiDye(this.tileEnt, 1, 152, 62));
		
		this.registerPlayerSlots(inventoryPlayer, 0, 0);
		
	}
	
	@Override
	protected boolean shiftClick(ItemStack itemStack, int slotiD) {
		if (itemStack.getItem() instanceof ItemMultiDye)
			return this.mergeItemStack(itemStack, 1, 2, false);
		else
			return this.mergeItemStack(itemStack, 0, 1, false);
	}
	
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0,
				(int) (this.tileEntColor.getColorStorage()[0] * 10));
		par1ICrafting.sendProgressBarUpdate(this, 1,
				(int) (this.tileEntColor.getColorStorage()[1] * 10));
		par1ICrafting.sendProgressBarUpdate(this, 2,
				(int) (this.tileEntColor.getColorStorage()[2] * 10));
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			
			if (this.r != (float) (this.tileEntColor.getColorStorage()[0] * 10)) {
				icrafting.sendProgressBarUpdate(this, 0,
						(int) (this.tileEntColor.getColorStorage()[0] * 10));
			}
			if (this.g != (float) (this.tileEntColor.getColorStorage()[1] * 10)) {
				icrafting.sendProgressBarUpdate(this, 1,
						(int) (this.tileEntColor.getColorStorage()[1] * 10));
			}
			if (this.b != (float) (this.tileEntColor.getColorStorage()[2] * 10)) {
				icrafting.sendProgressBarUpdate(this, 2,
						(int) (this.tileEntColor.getColorStorage()[2] * 10));
			}
		}
		
		this.r = this.tileEntColor.getColorStorage()[0] * 10;
		this.g = this.tileEntColor.getColorStorage()[1] * 10;
		this.b = this.tileEntColor.getColorStorage()[2] * 10;
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int index, int value) {
		if (index == 0 || index == 1 || index == 2) {
			//this.tileEntColor.setColorStorage(index, value / 10);
		}
	}
	
}
