package com.countrygamer.auxiliaryobjects.items;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.countrygamer.core.Core;
import com.countrygamer.core.Items.ItemBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMultiDye extends ItemBase {
	
	public static String			colorTagStr		= "multiDyeColor";
	public static String			defaultColorStr	= "multiDyeDefaults";
	
	LinkedHashMap<String, Integer>	colors			= new LinkedHashMap<String, Integer>();
	LinkedHashMap<String, Integer>	mcDyes			= new LinkedHashMap<String, Integer>();
	
	public ItemMultiDye(String modid, String name) {
		super(modid, name);
		this.colors.put("white", 0xffffff);
		this.colors.put("red", 0xff0000);
		this.colors.put("green", 0x00ff00);
		this.colors.put("blue", 0x0000ff);
		this.colors.put("yellow", 0xffff00);
		this.colors.put("purple", 0xff00ff);
		this.colors.put("orange", 0xff8000);
		this.colors.put("black", 0x000000);
		
		this.mcDyes.put("black", 0x000000);
		this.mcDyes.put("blue", 0x224baf);
		this.mcDyes.put("brown", 0x41220d);
		this.mcDyes.put("cyan", 0x2d7c9d);
		this.mcDyes.put("gray", 0x979797);
		this.mcDyes.put("green", 0x63862e);
		this.mcDyes.put("light_blue", 0x82ace7);
		this.mcDyes.put("lime", 0x83d41c);
		this.mcDyes.put("magenta", 0xa453ce);
		this.mcDyes.put("orange", 0xe69e34);
		this.mcDyes.put("pink", 0xe69e34);
		this.mcDyes.put("purple", 0x41220d);
		this.mcDyes.put("red", 0xa453ce);
		this.mcDyes.put("silver", 0xd9d9d9);
		this.mcDyes.put("white", 0xffffff);
		this.mcDyes.put("yellow", 0xe7e72a);
		
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		NBTTagCompound tagCom = null;
		if (!itemStack.hasTagCompound())
			tagCom = this.createNewTagCom();
		else
			tagCom = itemStack.getTagCompound();
		
		int currentColor = tagCom.getInteger(colorTagStr);
		
		if (Core.DEBUG && player.isSneaking())
			tagCom.setInteger(colorTagStr, this.cycleThroughColors(currentColor, this.colors));
		itemStack.setTagCompound(tagCom);
		return itemStack;
	}
	
	private int cycleThroughColors(int currentColor, LinkedHashMap<String, Integer> colors) {
		String[] colorNames = new String[colors.entrySet().size()];
		int index = 0;
		for (String colorName : colors.keySet()) {
			colorNames[index] = colorName;
			index++;
		}
		
		String currentColorName = "";
		for (String colorName : colorNames) {
			if (colors.get(colorName) == currentColor) currentColorName = colorName;
		}
		
		int newColor = currentColor;
		if (!currentColorName.equals("")) {
			Entry<String, Integer> entry0 = (Entry<String, Integer>) colors.entrySet().iterator()
					.next();
			String colorName0 = entry0.getKey();
			int color0 = entry0.getValue();
			for (Iterator it = colors.entrySet().iterator(); it.hasNext();) {
				Entry<String, Integer> entry = (Entry<String, Integer>) it.next();
				String colorName = entry.getKey();
				int color = entry.getValue();
				
				if (colorName.equals(currentColorName)) {
					Entry<String, Integer> entry2 = null;
					String colorName2 = currentColorName;
					int color2 = currentColor;
					if (it.hasNext()) {
						entry2 = (Entry<String, Integer>) it.next();
						colorName2 = entry2.getKey();
						color2 = entry2.getValue();
					}
					else {
						colorName2 = colorName0;
						color2 = color0;
					}
					//MultiDye.log.info(colorName0 + "|" + colorName + "|" + colorName2);
					newColor = color2;
					continue;
				}
			}
		}
		else
			newColor = this.colors.get("white");
		return newColor;
	}
	
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4,
			boolean isCurrentItem) {
		if (!world.isRemote && !itemStack.hasTagCompound()) {
			itemStack.setTagCompound(this.createNewTagCom());
		}
	}
	
	private NBTTagCompound createNewTagCom() {
		NBTTagCompound tagCom = new NBTTagCompound();
		tagCom.setInteger(colorTagStr, this.colors.get("black"));
		/*
		NBTTagCompound defaults = new NBTTagCompound();
		defaults.setInteger("white", this.colors.get("white"));
		defaults.setInteger("red", this.colors.get("red"));
		defaults.setInteger("green", this.colors.get("green"));
		defaults.setInteger("blue", this.colors.get("blue"));
		defaults.setInteger("yellow", this.colors.get("yellow"));
		defaults.setInteger("purple", this.colors.get("purple"));
		defaults.setInteger("orange", this.colors.get("orange"));
		defaults.setInteger("black", this.colors.get("black"));
		tagCom.setTag(defaultColorStr, defaults);
		 */
		return tagCom;
	}
	
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		NBTTagCompound tagCom = null;
		if (!itemStack.hasTagCompound())
			tagCom = this.createNewTagCom();
		else
			tagCom = itemStack.getTagCompound();
		int color = tagCom.getInteger(colorTagStr);
		return color;
	}
	
}
