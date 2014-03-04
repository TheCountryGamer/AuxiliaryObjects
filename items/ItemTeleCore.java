package com.countrygamer.auxiliaryobjects.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.countrygamer.auxiliaryobjects.AuxiliaryObjects;
import com.countrygamer.auxiliaryobjects.blocks.tiles.TileEntityTele;
import com.countrygamer.core.Items.ItemBase;
import com.countrygamer.core.lib.CoreUtil;

public class ItemTeleCore extends ItemBase {
	
	public String	dimensionID	= "Dimension_ID";
	public String	coorX		= "Coordinate_X";
	public String	coorY		= "Coordinate_Y";
	public String	coorZ		= "Coordinate_Z";
	
	public ItemTeleCore(String modid, String name) {
		super(modid, name);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,
			EntityPlayer player) {
		if (itemStack.getItem() instanceof ItemTeleCore) {
			NBTTagCompound tagCom = null;
			if (itemStack.hasTagCompound())
				tagCom = itemStack.getTagCompound();
			else
				tagCom = new NBTTagCompound();
			
			if (player.isSneaking() || tagCom.getBoolean("isActive"))
				tagCom.setBoolean("isActive", false);
			else {
				tagCom.setBoolean("isActive", true);
				NBTTagCompound coords = new NBTTagCompound();
				coords.setInteger(this.dimensionID, player.dimension);
				coords.setDouble(this.coorX, player.posX);
				coords.setDouble(this.coorY, player.posY);
				coords.setDouble(this.coorZ, player.posZ);
				tagCom.setTag("coords", coords);
			}
			itemStack.setTagCompound(tagCom);
		}
		return itemStack;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player,
			World world, int x, int y, int z, int side, float par8, float par9,
			float par10) {
		if (player.isSneaking()
				&& world.getBlock(x, y, z) == AuxiliaryObjects.teleporterBase) {
			TileEntityTele tileEnt = (TileEntityTele) world.getTileEntity(
					x, y, z);
			if (tileEnt != null) {
				ItemStack coreStack = tileEnt.getStackInSlot(0);
				if (coreStack != null) {
					CoreUtil.dropItemStack(world,
							new ItemStack(coreStack.getItem()), x, y, z,
							coreStack.getTagCompound());
					tileEnt.setInventorySlotContents(0, null);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity,
			int par4, boolean isCurrentItem) {
		if (!world.isRemote) {
			if (!itemStack.hasTagCompound()) {
				NBTTagCompound tagCom = new NBTTagCompound();
				tagCom.setBoolean("isActive", false);
				itemStack.setTagCompound(tagCom);
			}
		}
	}
	
}
