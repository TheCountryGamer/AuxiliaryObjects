package com.countrygamer.capo.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.inventory.InventorySack;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.core.Items.ItemBase;
import com.countrygamer.core.inventory.ContainerItemBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInventorySack extends ItemBase {
	
	public static String	sackName	= Reference.MOD_ID + "_ItemInventorySack";
	
	public ItemInventorySack(String modid, String name) {
		super(modid, name);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (player.isSneaking()) {
			player.openGui(Capo.instance, Reference.guiInvSack, world, (int) player.posX,
					(int) player.posY, (int) player.posZ);
		}
		else if (itemStack.getItem() instanceof ItemInventorySack) {
			InventorySack invSack = new InventorySack(itemStack);
			InventoryPlayer invPlayer = player.inventory;
			if (invSack == null || invPlayer == null) return itemStack;
			if (invSack.getSizeInventory() == (invPlayer.getSizeInventory() - 4)) {
				int size = invSack.getSizeInventory();
				for (int i = 0; i < size; i++) {
					ItemStack playerStack = invPlayer.getStackInSlot(i);
					boolean valid = playerStack == null ? true : invSack.isItemValidForSlot(i,
							playerStack);
					if (valid) {
						invPlayer.setInventorySlotContents(i, invSack.getStackInSlot(i));
						invSack.setInventorySlotContents(i, playerStack);
						invSack.writeToNBT(itemStack.getTagCompound());
					}
				}
			}
			else {
				System.err.println("Error inventories are not the same size. This is a bug.");
			}
			
		}
		return itemStack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack itemstack) {
		return 1;
	}
	
	/**
	 * Called every tick while there is an ItemInventory in the player's
	 * inventory
	 * This is the method we will use to access the GUI and also to write to NBT
	 * when necessary
	 */
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4,
			boolean isCurrentItem) {
		// Only Player's will be accessing the GUI
		if (!world.isRemote && entity instanceof EntityPlayer) {
			// Cast Entity parameter as an EntityPlayer
			EntityPlayer player = (EntityPlayer) entity;
			
			
			// If our ContainerSack is currently open, write contents to NBT
			// when needsUpdate is true
			if (player.openContainer != null && player.openContainer instanceof ContainerItemBase
					&& ((ContainerItemBase) player.openContainer).needsUpdate) {
				((ContainerItemBase) player.openContainer).writeToNBT();
				// Set needsUpdate back to false so we don't continually write
				// to NBT
				((ContainerItemBase) player.openContainer).needsUpdate = false;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		NBTTagCompound tagCom = itemStack.getTagCompound();
		if (tagCom != null) {
			String name = tagCom.getString(sackName);
			if (!name.equals("")) list.add(1, name);
		}
	}
	
}
