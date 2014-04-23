package com.countrygamer.capo.common.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemTriDye extends ItemMultiDye {
	
	public ItemTriDye(String modid, String name) {
		super(modid, name);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		return itemStack;
	}
	
	@Override
	public NBTTagCompound createNewTagCom() {
		NBTTagCompound tagCom = new NBTTagCompound();
		tagCom.setInteger(colorTagStr + "0", this.colors.get("white"));
		tagCom.setInteger(colorTagStr + "1", this.colors.get("white"));
		tagCom.setInteger(colorTagStr + "2", this.colors.get("white"));
		return tagCom;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		return 16777215;
	}
	
	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		if (itemStack != null && itemStack.getItem() instanceof ItemTriDye) {
			NBTTagCompound tagCom = itemStack.hasTagCompound() ? itemStack.getTagCompound()
					: ((ItemTriDye) itemStack.getItem()).createNewTagCom();
			
			for (int i = 0; i < 3; i++) {
				list.add(Integer.toHexString(tagCom.getInteger(colorTagStr + i)));
			}
		}
	}
	
}
