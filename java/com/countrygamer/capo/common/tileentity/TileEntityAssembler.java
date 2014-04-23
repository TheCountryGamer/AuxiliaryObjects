package com.countrygamer.capo.common.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.item.ItemModule;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;
import com.countrygamer.core.common.lib.LogBlock;

public class TileEntityAssembler extends TileEntityInventoryBase {
	
	public boolean hasLoadedUpgrades = true;
	
	public TileEntityAssembler() {
		super("Module Assembler", 5, 64);
	}
	
	@Override
	public void updateEntity() {
		if (this.getStackInSlot(0) != null && !this.hasLoadedUpgrades) {
			this.loadUpgrades();
			this.hasLoadedUpgrades = true;
		}
	}
	
	public void loadUpgrades() {
		ItemStack moduleStack = this.getStackInSlot(0);
		if (moduleStack != null && moduleStack.getItem() instanceof ItemModule) {
			ItemStack[] upgrades = ((ItemModule) moduleStack.getItem()).loadUpgrades(moduleStack);
			for (int i = 0; i < upgrades.length; i++) {
				this.setInventorySlotContents(i + 1, upgrades[i]);
			}
		}
		else {
			this.eraseUpgradeSlots();
		}
	}
	
	public void saveUpgrades() {
		ItemStack moduleStack = this.getStackInSlot(0);
		if (moduleStack != null && moduleStack.getItem() instanceof ItemModule) {
			ItemStack[] upgrades = new ItemStack[4];
			LogBlock log = new LogBlock(Capo.log, "\n\t");
			for (int i = 0; i < 4; i++) {
				upgrades[i] = this.getStackInSlot(i + 1);
				log.addWithLine("Saved Slot " + (i + 1) + " to upgrade slot " + i);
				String name = upgrades[i] != null ? upgrades[i].getDisplayName() : "NULL";
				log.addWithLine("\t" + name);
			}
			if (!this.getWorldObj().isRemote)
				log.log();
			((ItemModule)moduleStack.getItem()).saveUpgrades(moduleStack, upgrades);
			
			this.eraseUpgradeSlots();
			this.loadUpgrades();
		}
	}
	
	public void eraseUpgradeSlots() {
		for (int i = 1; i < 5; i++) {
			this.setInventorySlotContents(i, null);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setBoolean("hasLoadedUpgrades", this.hasLoadedUpgrades);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.hasLoadedUpgrades = tagCom.getBoolean("hasLoadedUpgrade");
	}
	
}
