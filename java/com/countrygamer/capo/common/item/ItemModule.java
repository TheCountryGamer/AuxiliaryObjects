package com.countrygamer.capo.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import com.countrygamer.core.Base.common.item.ItemBase;

public class ItemModule extends ItemBase {
	
	protected final String name;
	
	public ItemModule(String modid, String name) {
		super(modid, name);
		this.name = name;
		this.setMaxStackSize(1);
	}
		
	public NBTTagCompound getItemNewTagCompound() {
		NBTTagCompound itemTagCom = new NBTTagCompound();
		return itemTagCom;
	}
	
	public NBTTagCompound saveUpgrades(ItemStack moduleStack, ItemStack[] upgrades) {
		NBTTagCompound tagCom = moduleStack.getTagCompound();
		NBTTagCompound itemTagCom = tagCom.getCompoundTag("TagCom" + this.name);
		
		NBTTagList upgradesList = new NBTTagList();
		for (int i = 0; i < upgrades.length; i++) {
			if (upgrades[i] != null) {
				NBTTagCompound upgradeTagCom = new NBTTagCompound();
				upgradeTagCom.setByte("Slot", (byte)i);
				upgrades[i].writeToNBT(upgradeTagCom);
				upgradesList.appendTag(upgradeTagCom);
			}
		}
		itemTagCom.setTag("Upgrades", upgradesList);
		
		tagCom.setTag("TagCom" + this.name, itemTagCom);
		return tagCom;
	}
	
	public ItemStack[] loadUpgrades(ItemStack moduleStack) {
		NBTTagCompound tagCom = moduleStack.getTagCompound();
		NBTTagCompound itemTagCom = tagCom.getCompoundTag("TagCom" + this.name);
		
		NBTTagList upgradesList = itemTagCom.getTagList("Upgrades", 10);
		ItemStack[] upgrades = new ItemStack[4];
		for (int i = 0; i < upgradesList.tagCount(); i++) {
			NBTTagCompound upgradeTagCom = upgradesList.getCompoundTagAt(i);
			int slot = upgradeTagCom.getByte("Slot") & 255;
			upgrades[slot] = ItemStack.loadItemStackFromNBT(upgradeTagCom);
		}
		
		return upgrades;
	}
	
	@Override
 	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4,
			boolean isCurrentItem) {
		if (itemStack != null && itemStack.getItem() instanceof ItemModule
				&& !itemStack.hasTagCompound()) {
			NBTTagCompound tagCom = new NBTTagCompound();
			tagCom.setTag("TagCom" + this.name, this.getItemNewTagCompound());
			itemStack.setTagCompound(tagCom);
		}
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		NBTTagCompound tagCom = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
		NBTTagCompound itemTagCom = tagCom.getCompoundTag("TagCom" + this.name);
		NBTTagList upgradeList = itemTagCom.getTagList("Upgrades", 10);
		for (int i = 0; i < upgradeList.tagCount(); i++) {
			NBTTagCompound upgradeTagCom = upgradeList.getCompoundTagAt(i);
			int slot = upgradeTagCom.getByte("Slot") & 255;
			ItemStack upgrade = ItemStack.loadItemStackFromNBT(upgradeTagCom);
			list.add("Slot " + (slot + 1) + ": " + upgrade.getDisplayName() + " x" + upgrade.stackSize);
		}
		
	}
	
}
