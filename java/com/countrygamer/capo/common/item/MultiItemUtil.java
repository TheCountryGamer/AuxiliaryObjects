package com.countrygamer.capo.common.item;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public class MultiItemUtil {
	
	public static ItemStack bowCheckAndAction(ItemStack multiStack, World world, EntityPlayer player) {
		int slot = 0;
		ItemStack bowStack = null;
		for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
			ItemStack stackInSlot = ItemMultiItem.getStackInSlot(multiStack, i);
			if (stackInSlot != null && stackInSlot.getItem() == Items.bow) {
				slot = i;
				bowStack = stackInSlot;
				break;
			}
		}
		
		if (bowStack != null) {
			ArrowNockEvent event = new ArrowNockEvent(player, bowStack);
			MinecraftForge.EVENT_BUS.post(event);
			if (event.isCanceled()) {
				return event.result;
			}
			
			if (player.capabilities.isCreativeMode || player.inventory.hasItem(Items.arrow)) {
				int j = 72000;
				ArrowLooseEvent event2 = new ArrowLooseEvent(player, bowStack, j);
				MinecraftForge.EVENT_BUS.post(event2);
				if (event2.isCanceled()) {
					return multiStack;
				}
				j = event2.charge;
				
				boolean flag = player.capabilities.isCreativeMode
						|| EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId,
								bowStack) > 0;
				
				if (flag || player.inventory.hasItem(Items.arrow)) {
					float f = (float) j / 20.0F;
					f = (f * f + f * 2.0F) / 3.0F;
					
					if ((double) f < 0.1D) {
						return multiStack;
					}
					
					if (f > 1.0F) {
						f = 1.0F;
					}
					
					EntityArrow entityarrow = new EntityArrow(world, player, f * 2.0F);
					
					if (f == 1.0F) {
						entityarrow.setIsCritical(true);
					}
					
					int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId,
							bowStack);
					
					if (k > 0) {
						entityarrow.setDamage(entityarrow.getDamage() + (double) k * 0.5D + 0.5D);
					}
					
					int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId,
							bowStack);
					
					if (l > 0) {
						entityarrow.setKnockbackStrength(l);
					}
					
					if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, bowStack) > 0) {
						entityarrow.setFire(100);
					}
					
					bowStack.damageItem(1, player);
					world.playSoundAtEntity(player, "random.bow", 1.0F,
							1.0F / ((new Random()).nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					
					if (flag) {
						entityarrow.canBePickedUp = 2;
					}
					else {
						player.inventory.consumeInventoryItem(Items.arrow);
					}
					
					if (!world.isRemote) {
						world.spawnEntityInWorld(entityarrow);
					}
					
					multiStack = ItemMultiItem.setStackInSlot(multiStack, slot, bowStack);
				}
			}
		}
		
		return multiStack;
	}
	
	
}
