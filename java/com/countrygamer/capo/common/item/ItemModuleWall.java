package com.countrygamer.capo.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.core.common.lib.UtilString;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	
	public NBTTagCompound setBound(ItemStack moduleStack, boolean isMinVsMax, char axis, int value) {
		NBTTagCompound tagCom = moduleStack.hasTagCompound() ? moduleStack.getTagCompound()
				: new NBTTagCompound();
		NBTTagCompound itemTagCom = tagCom.hasKey("TagCom" + this.name) ? tagCom
				.getCompoundTag("TagCom" + this.name) : this.getItemNewTagCompound();
		
		String prefix = isMinVsMax ? "min" : "max";
		axis = Character.toUpperCase(axis);
		String key = prefix + "Bound" + axis;
		itemTagCom.setInteger(key, value);
		tagCom.setTag("TagCom" + this.name, itemTagCom);
		return tagCom;
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
	
	public NBTTagCompound setOffset(ItemStack moduleStack, char axis, int value) {
		NBTTagCompound tagCom = moduleStack.hasTagCompound() ? moduleStack.getTagCompound()
				: new NBTTagCompound();
		NBTTagCompound itemTagCom = tagCom.hasKey("TagCom" + this.name) ? tagCom
				.getCompoundTag("TagCom" + this.name) : this.getItemNewTagCompound();
		
		axis = Character.toUpperCase(axis);
		
		String key = "offset" + axis;
		itemTagCom.setInteger(key, value);
		tagCom.setTag("TagCom" + this.name, itemTagCom);
		return tagCom;
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		if (!UtilString.isShiftKeyDown())
			list.add("Hold SHIFT for information");
		else
			list = this.addInformationWithShift(itemStack, player, list, par4);
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	@SideOnly(Side.CLIENT)
	@Override
	public List addInformationWithShift(ItemStack itemStack, EntityPlayer player, List list,
			boolean par4) {
		list = super.addInformationWithShift(itemStack, player, list, par4);
		if (itemStack != null && itemStack.getItem() instanceof ItemModuleWall) {
			ItemStack camoStack = this.loadCamoStack(itemStack);
			if (camoStack != null) {
				list.add("Camoflauge Block:");
				list.add("   " + camoStack.getDisplayName());
			}
			
			list.add("Offsets:");
			list.add("   X: " + this.getOffset(itemStack, 'X'));
			list.add("   Y: " + this.getOffset(itemStack, 'Y'));
			list.add("   Z: " + this.getOffset(itemStack, 'Z'));
			list.add("Bounds:");
			list.add("   Min X: " + this.getBound(itemStack, true, 'X'));
			list.add("   Max X: " + this.getBound(itemStack, false, 'X'));
			list.add("   Min Y: " + this.getBound(itemStack, true, 'Y'));
			list.add("   Max Y: " + this.getBound(itemStack, false, 'Y'));
			list.add("   Min Z: " + this.getBound(itemStack, true, 'Z'));
			list.add("   Max Z: " + this.getBound(itemStack, false, 'Z'));
			
		}
		return list;
	}
	
}
