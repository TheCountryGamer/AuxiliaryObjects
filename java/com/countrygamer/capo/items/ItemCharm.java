package com.countrygamer.capo.items;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.core.Base.item.ItemMetadataBase;

public class ItemCharm extends ItemMetadataBase {
	
	/**
	 * Holds the names of the different charms
	 */
	@SuppressWarnings("unused")
	private final ArrayList<String>	typeNames;
	
	public ItemCharm(String modid, String[] names) {
		super(modid, getCharmedArray(names));
		this.typeNames = getStringList(names);
		this.setMaxStackSize(1);
	}
	
	/**
	 * Converts a List of Strings into a String[]
	 * 
	 * @param listOfNames
	 * @return
	 */
	private static ArrayList<String> getStringList(String[] listOfNames) {
		ArrayList<String> names = new ArrayList<String>();
		for (String name : listOfNames) {
			names.add(name);
		}
		return names;
	}
	
	/**
	 * Used to convert simple names to Charm names
	 */
	private static String[] getCharmedArray(String[] simpleNames) {
		String[] newNames = new String[simpleNames.length];
		for (int index = 0; index < simpleNames.length; index++) {
			newNames[index] = "Charm of " + simpleNames[index];
		}
		return newNames;
	}
	
	//
	public static NBTTagCompound getNewCharmTagCom() {
		NBTTagCompound charmTagCom = new NBTTagCompound();
		
		charmTagCom.setInteger("tick", 0);
		
		return charmTagCom;
	}
	
	public static NBTTagCompound getCharmTagCom(ItemStack itemStack) {
		if (itemStack.hasTagCompound()
				&& itemStack.getTagCompound().getCompoundTag("charmTagCom") != (new NBTTagCompound()))
			return itemStack.getTagCompound().getCompoundTag("charmTagCom");
		else
			return ItemCharm.getNewCharmTagCom();
	}
	
	public static void setCharmTagCom(ItemStack itemStack, NBTTagCompound charmTagCom) {
		NBTTagCompound tagCom = new NBTTagCompound();
		if (itemStack.hasTagCompound()) tagCom = itemStack.getTagCompound();
		tagCom.setTag("charmTagCom", charmTagCom);
		itemStack.setTagCompound(tagCom);
	}
	
	//
	
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4,
			boolean isCurrentItem) {
		
		if (!itemStack.hasTagCompound()) {
			NBTTagCompound tagCom = new NBTTagCompound();
			
			tagCom.setTag("charmTagCom", ItemCharm.getNewCharmTagCom());
			
			itemStack.setTagCompound(tagCom);
			return;
		}
		
		NBTTagCompound charmTagCom = ItemCharm.getCharmTagCom(itemStack);
		int tick = charmTagCom.getInteger("tick");
		if (tick > 20) {
			charmTagCom.setInteger("tick", 0);
			tick = 0;
		}
		
		int delay = 0;
		if (entity != null && entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			
			// 0, health, done in item right click
			
			if (itemStack.getItemDamage() == 1) { // hunger
				if (player.getFoodStats().getFoodLevel() < 20) {
					player.getFoodStats().setFoodLevel(20);
					player.getFoodStats().setFoodSaturationLevel(5.0F);
				}
			}
			
			// This code block contains code modified from Azanor's Thaumcraft
			// Mainly the boots of the traveleller
			if (itemStack.getItemDamage() == 2) { // speed
			
				if (player.moveForward > 0.0F) {
					if (player.onGround) {
						float bonus = 0.055F;
						if (player.isInWater()) bonus /= 4.0F;
						player.moveFlying(0.0F, 2.0F, bonus);
					}
				}
			}
			if (itemStack.getItemDamage() == 3) { // jumping
				if (player.worldObj.isRemote) {
					if (!Capo.instance.entIDToStepHeight.containsKey(player.getEntityId())) {
						Capo.instance.entIDToStepHeight.put(player.getEntityId(),
								player.stepHeight);
					}
					player.stepHeight = 1.0F;
				}
				if (!player.onGround) player.jumpMovementFactor = 0.05F;
				if (player.fallDistance > 0.25F) player.fallDistance -= 0.25F;
			}
			// end of code block
			
			if (itemStack.getItemDamage() == 4) { // regen
				delay = 20 * 3;
				if (tick % delay == 0) {
					if (player.getHealth() < player.getMaxHealth()) {
						player.heal(0.5F);
					}
				}
			}
			
			// 5, heat, done as an event
			
			if (itemStack.getItemDamage() == 6) {
				if (player.isInsideOfMaterial(Material.water) && player.getAir() < 300)
					player.setAir(300);
			}
			
			// 7, invisibility, done as an event
			
			if (itemStack.getItemDamage() == 8) { // night vision
				player.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 20, 10));
			}
			
		}
		
		charmTagCom.setInteger("tick", tick + 1);
		ItemCharm.setCharmTagCom(itemStack, charmTagCom);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (itemStack.getItem() == Capo.charm && itemStack.getItemDamage() == 0) {
			if (player.getHealth() < player.getMaxHealth()) player.heal(5.0F);
		}
		
		return itemStack;
	}
	
}
