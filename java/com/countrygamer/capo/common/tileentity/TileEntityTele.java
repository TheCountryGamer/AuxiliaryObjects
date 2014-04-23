package com.countrygamer.capo.common.tileentity;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.BlockSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.item.ItemTeleCore;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;
import com.countrygamer.core.common.Core;
import com.countrygamer.core.common.lib.CoreUtil;

public class TileEntityTele extends TileEntityInventoryBase {

	private HashMap<String, Integer> slotIDs = new HashMap<String, Integer>();
	public float[] coreModelRotations = new float[3];
	public boolean isPowered = false;

	public TileEntityTele() {
		super("Teleporter", 5, 1);
		this.slotIDs.put("core", 0);
		this.slotIDs.put("redstone", 1);
		this.slotIDs.put("ender", 2);
		this.slotIDs.put("gold", 3);
		this.slotIDs.put("entity", 4);
	}

	@Override
	public boolean isItemValidForSlot(int slotiD, ItemStack itemStack) {
		if (itemStack == null)
			return false;
		boolean valid = false;
		switch (slotiD) {
		case 0: // core
			valid = itemStack.getItem() instanceof ItemTeleCore;
			break;
		case 1: // redstone
			valid = itemStack.getItem() == Items.redstone;
			break;
		case 2: // ender
			valid = itemStack.getItem() == Items.ender_pearl;
			break;
		case 3: // gold
			valid = itemStack.getItem() == Item
					.getItemFromBlock(Blocks.gold_block);
			break;
		case 4: // entity
			valid = itemStack.getItem() == Items.egg;
			break;
		}
		if (Core.DEBUG)
			Capo.log.info("Empty:" + this.isSlotEmpty(itemStack) + " Valid:"
					+ valid);
		return this.isSlotEmpty(itemStack) && valid;
	}

	// NBT, in first, out last

	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		// -
		tagCom.setFloat("coreRotZ", this.coreModelRotations[2]);
		tagCom.setFloat("coreRotY", this.coreModelRotations[1]);
		tagCom.setFloat("coreRotX", this.coreModelRotations[0]);
		// :
		tagCom.setBoolean("isPowered", this.isPowered);
		// :

		// -
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		// -

		// :
		this.isPowered = tagCom.getBoolean("isPowered");
		// :
		this.coreModelRotations[0] = tagCom.getFloat("coreRotX");
		this.coreModelRotations[1] = tagCom.getFloat("coreRotY");
		this.coreModelRotations[2] = tagCom.getFloat("coreRotZ");
		// -
	}

	// Updater
	public void updateEntity() {
		if (this.isPowered && this.inv[0] != null) {
			ItemTeleCore core = (ItemTeleCore) this.inv[0].getItem();
			int dimID = this.inv[0].getTagCompound().getCompoundTag("coords")
					.getInteger(core.dimensionID);
			double x = this.inv[0].getTagCompound().getCompoundTag("coords")
					.getDouble(core.coorX);
			double y = this.inv[0].getTagCompound().getCompoundTag("coords")
					.getDouble(core.coorY);
			double z = this.inv[0].getTagCompound().getCompoundTag("coords")
					.getDouble(core.coorZ);
			// ~~~
			//int blockMaxRadius = 10;

			int r = 3;

			List<?> players = this.worldObj.getEntitiesWithinAABB(
					EntityPlayer.class, AxisAlignedBB.getBoundingBox(
							this.xCoord - r, this.yCoord - 0, this.zCoord - r,
							this.xCoord + r, this.yCoord + 1, this.zCoord + r));
			for (Object thing : players) {
				if (thing instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) thing;
					if (this.getStackInSlot(this.slotIDs.get("ender")) != null) {
						CoreUtil.teleportPlayerToDimension(player, dimID);
					}
					CoreUtil.teleportPlayer(player, x, y, z,
							this.inv[0].getItem() == Capo.stableCore, false);
				}
			}
		}
	}

	// Client Server Sync
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound tagCom = pkt.func_148857_g();
		this.readFromNBT(tagCom);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCom = new NBTTagCompound();
		this.writeToNBT(tagCom);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord,
				this.zCoord, this.blockMetadata, tagCom);
	}

	// Util for Renderer
	public boolean hasBlockOnTop() {
		return this.worldObj
				.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) instanceof BlockSlab;
	}

	public boolean isSlotEmpty(String slotName) {
		return this.getStackInSlot(this.slotIDs.get(slotName)) == null;
	}

	public boolean isSlotEmpty(ItemStack itemStack) {
		boolean slotEmpty = false;
		boolean validStack = false;
		if (itemStack.getItem() instanceof ItemTeleCore) {
			slotEmpty = this.getStackInSlot(0) == null;
			validStack = true;
		}
		if (itemStack.getItem() == Items.redstone) {
			slotEmpty = this.isSlotEmpty("redstone");
			validStack = true;
		}
		if (itemStack.getItem() == Items.ender_pearl) {
			slotEmpty = this.isSlotEmpty("ender");
			validStack = true;
		}
		if (itemStack.getItem() == Item.getItemFromBlock(Blocks.gold_block)) {
			slotEmpty = this.isSlotEmpty("gold");
			validStack = true;
		}
		if (itemStack.getItem() == Items.egg) {
			slotEmpty = this.isSlotEmpty("entity");
			validStack = true;
		}
		return slotEmpty && validStack;
	}

}
