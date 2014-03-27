package com.countrygamer.capo.items;

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
	protected NBTTagCompound createNewTagCom() {
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
	
}
