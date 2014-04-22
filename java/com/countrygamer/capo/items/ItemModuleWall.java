package com.countrygamer.capo.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemModuleWall extends ItemModule {
		
	public ItemModuleWall(String modid, String name) {
		super(modid, name);
	}
	
	@Override
	public NBTTagCompound getItemNewTagCompound() {
		NBTTagCompound itemTagCom = super.getItemNewTagCompound();
		
		itemTagCom.setInteger("offestX", 0);
		itemTagCom.setInteger("offsetY", 5);
		itemTagCom.setInteger("offsetZ", 0);
		itemTagCom.setInteger("minBoundX", -3);
		itemTagCom.setInteger("maxBoundX", +3);
		itemTagCom.setInteger("minBoundY", -3);
		itemTagCom.setInteger("maxBoundY", +3);
		itemTagCom.setInteger("minBoundZ", -3);
		itemTagCom.setInteger("maxBoundZ", +3);
		
		
		
		return itemTagCom;
	}
	
	public NBTTagCompound saveCamoStack(ItemStack moduleStack, ItemStack camoStack) {
		NBTTagCompound tagCom = moduleStack.hasTagCompound() ? moduleStack.getTagCompound()
				: new NBTTagCompound();
		NBTTagCompound itemTagCom = tagCom.hasKey("TagCom" + this.name) ? tagCom
				.getCompoundTag("TagCom" + this.name) : this.getItemNewTagCompound();
		
		if (camoStack != null) {
			NBTTagCompound camoTagCom = new NBTTagCompound();
			camoStack.writeToNBT(camoTagCom);
			itemTagCom.setTag("CamoStack", camoTagCom);
		}
		else if (itemTagCom.hasKey("CamoStack")) {
			itemTagCom.removeTag("CamoStack");
		}
		
		tagCom.setTag("TagCom" + this.name, itemTagCom);
		return tagCom;
	}
	
	public ItemStack loadCamoStack(ItemStack moduleStack) {
		NBTTagCompound tagCom = moduleStack.hasTagCompound() ? moduleStack.getTagCompound()
				: new NBTTagCompound();
		NBTTagCompound itemTagCom = tagCom.hasKey("TagCom" + this.name) ? tagCom
				.getCompoundTag("TagCom" + this.name) : this.getItemNewTagCompound();
		
		ItemStack camoStack = null;
		if (itemTagCom.hasKey("CamoStack")) {
			camoStack = ItemStack.loadItemStackFromNBT(itemTagCom.getCompoundTag("CamoStack"));
		}
		
		return camoStack;
	}
	
	public int getBound(ItemStack moduleStack, boolean isMinVsMax, char axis) {
		NBTTagCompound tagCom = moduleStack.hasTagCompound() ? moduleStack.getTagCompound()
				: new NBTTagCompound();
		NBTTagCompound itemTagCom = tagCom.hasKey("TagCom" + this.name) ? tagCom
				.getCompoundTag("TagCom" + this.name) : this.getItemNewTagCompound();
		
		String prefix = isMinVsMax ? "min" : "max";
		axis = Character.toUpperCase(axis);
		String key = prefix + "Bound" + axis;
		if (itemTagCom.hasKey(key))
			return itemTagCom.getInteger(key);
		else
			return 0;
	}
	
	public int getOffset(ItemStack moduleStack, char axis) {
		NBTTagCompound tagCom = moduleStack.hasTagCompound() ? moduleStack.getTagCompound()
				: new NBTTagCompound();
		NBTTagCompound itemTagCom = tagCom.hasKey("TagCom" + this.name) ? tagCom
				.getCompoundTag("TagCom" + this.name) : this.getItemNewTagCompound();
		
		axis = Character.toUpperCase(axis);
		String key = "offset" + axis;
		if (itemTagCom.hasKey(key))
			return itemTagCom.getInteger(key);
		else
			return 0;
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		if (itemStack != null && itemStack.getItem() instanceof ItemModuleWall) {
			ItemStack camoStack = this.loadCamoStack(itemStack);
			if (camoStack != null) {
				list.add("Camoflauge Block:");
				list.add("    " + camoStack.getDisplayName());
			}
		}
	}
	
}
