package com.countrygamer.capo.items;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.inventory.InventorySack;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.core.Base.inventory.ContainerItemBase;
import com.countrygamer.core.Base.item.ItemBase;

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
				NBTTagCompound tagCom =
						itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
				NBTTagCompound chestTag = tagCom.getCompoundTag("chest");
				if (chestTag.getBoolean("hasLink")) {
					int x = chestTag.getInteger("x");
					int y = chestTag.getInteger("y");
					int z = chestTag.getInteger("z");
					TileEntity tileEntity = world.getTileEntity(x, y, z);
					if (tileEntity != null && tileEntity instanceof TileEntityChest) {
						TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
						for (int i = 0; i < tileEntityChest.getSizeInventory(); i++) {
							ItemStack chestStack = tileEntityChest.getStackInSlot(i);
							boolean valid = chestStack == null ? true : invSack.isItemValidForSlot(i+9,
									chestStack);
							if (valid) {
								tileEntityChest.setInventorySlotContents(i, invSack.getStackInSlot(i+9));
								invSack.setInventorySlotContents(i+9, chestStack);
								invSack.writeToNBT(itemStack.getTagCompound());
							}
						}
						if (!world.isRemote)
							player.addChatComponentMessage(new ChatComponentText(
									"Swapped inventory with Chest at " + x + " " + y + " " + z));
					}
					else {
						tagCom.removeTag("chest");
						if (!world.isRemote) {
							player.addChatComponentMessage(new ChatComponentText(
									"No chest was found at " + x + " " + y + " " + z));
							player.addChatComponentMessage(new ChatComponentText(
									"Link to chest has been removed."));
						}
					}
				}
				else {
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
			}
			else {
				//System.err.println("Error inventories are not the same size. This is a bug.");
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
		NBTTagCompound tagCom = itemStack.getTagCompound();
		if (tagCom != null) {
			String name = tagCom.getString(sackName);
			if (!name.equals("")) list.add(1, name);
		}
	}

	@Override
	public boolean onItemUse(
			ItemStack itemStack, EntityPlayer player, World world,
			int x, int y, int z, int side,
			float x1, float y1, float z1) {
		if (player.isSneaking()) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity != null && tileEntity instanceof TileEntityChest) {
				TileEntityChest tileEntChest = (TileEntityChest)tileEntity;
				NBTTagCompound tagCom =
						itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
				NBTTagCompound chestTag = new NBTTagCompound();

				chestTag.setBoolean("hasLink", true);
				chestTag.setInteger("x", x);
				chestTag.setInteger("y", y);
				chestTag.setInteger("z", z);

				tagCom.setTag("chest", chestTag);
				itemStack.setTagCompound(tagCom);

				if (!world.isRemote)
					player.addChatComponentMessage(new ChatComponentText(
							"Linked to Chest at " + x + " " + y + " " + z));
				return true;
			}
		}
		return false;
	}

}
