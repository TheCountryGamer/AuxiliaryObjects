package com.countrygamer.capo.blocks.tiles;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

import com.countrygamer.capo.items.ItemMultiItem;
import com.countrygamer.capo.lib.EnumPartition;
import com.countrygamer.core.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.lib.CoreUtil;

public class TileEntityInflixer extends TileEntityInventoryBase {
	
	public TileEntityInflixer() {
		super("Inflixer", 2, 1);
	}
	
	public void inflixItem() {
		// MultiMod.log.info("Inflix Recieved");
		ItemStack itemToInflix = this.getStackInSlot(0);
		ItemStack multiStack = this.getStackInSlot(1);
		if (itemToInflix != null && multiStack != null) {
			ItemStack multiStackCopy = multiStack.copy();
			
			// TODO, change to custom item (wipes multi item of partition, spits out held items)
			if (itemToInflix.getItem() instanceof ItemDye && itemToInflix.getItemDamage() == 15) {
				for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
					ItemStack stackInSlot = ItemMultiItem.getStackInSlot(multiStackCopy, i);
					if (stackInSlot != null) {
						multiStackCopy = ItemMultiItem.setStackInSlot(multiStackCopy, i, null);
						
						if (stackInSlot.hasTagCompound())
							CoreUtil.dropItemStack(this.worldObj, stackInSlot, this.xCoord,
									this.yCoord, this.zCoord, stackInSlot.getTagCompound());
						else
							CoreUtil.dropItemStack(this.worldObj, stackInSlot, this.xCoord,
									this.yCoord, this.zCoord);
					}
				}
				
				ItemStack newMultiStack = ItemMultiItem.setPartition(multiStackCopy,
						EnumPartition.NONE);
				this.setInventorySlotContents(0, null);
				this.setInventorySlotContents(1, newMultiStack);
				return;
			}
			
			ItemStack newMultiStack = ItemMultiItem.inflixItemStack(multiStackCopy, itemToInflix);
			if (!ItemStack.areItemStacksEqual(multiStackCopy, newMultiStack)
					|| !ItemStack.areItemStackTagsEqual(multiStackCopy, newMultiStack)) {
				this.setInventorySlotContents(0, null);
				this.setInventorySlotContents(1, newMultiStack);
			}
			
			// MultiMod.log.info("Inflix Ran");
		}
		
	}
	
	public void partitionItem(int partitionID) {
		ItemStack multiStack = this.getStackInSlot(1);
		if (multiStack != null && ItemMultiItem.getPartition(multiStack) == EnumPartition.NONE) {
			ItemStack multiStackCopy = multiStack.copy();
			String enumName = EnumPartition.getEnumForID(partitionID).toString();
			// AuxiliaryObjects.log.info("Partition ID: " + partitionID + "," + enumName);
			ItemStack newMultiStack = ItemMultiItem.setPartition(multiStackCopy,
					EnumPartition.getEnumForID(partitionID));
			this.setInventorySlotContents(1, newMultiStack);
		}
	}
	
}
