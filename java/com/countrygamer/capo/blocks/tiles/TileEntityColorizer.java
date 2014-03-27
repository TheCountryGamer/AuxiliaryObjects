package com.countrygamer.capo.blocks.tiles;

import java.text.DecimalFormat;

import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.lib.CoreUtilHex;

public class TileEntityColorizer extends TileEntityInventoryBase {
	
	private double[]		colorStorage;
	private int				maxDelay		= 20 * 2;
	private int				dyeDelay;
	public static double	maxColorStorage	= 4.0F;
	
	public TileEntityColorizer() {
		super("Colorizer", 2, 64);
		this.colorStorage = new double[] {
				0, 0, 0
		};
		
		this.dyeDelay = this.maxDelay;
		
	}
	
	@Override
	public void updateEntity() {
		ItemStack dyeStack = this.getStackInSlot(0);
		if (dyeStack != null) {
			if (dyeStack.getItem() instanceof ItemDye) { // TODO
				double[] addedDye = CoreUtilHex.getColorFromDye(dyeStack);
				if (this.canAbsorb(addedDye)) this.absorbDye(dyeStack, addedDye);
			}
			else if (dyeStack.getItem() instanceof ItemBucketMilk) {
				this.colorStorage = new double[] {
						0, 0, 0
				};
				ItemStack copy = dyeStack.copy();
				copy.stackSize--;
				if (copy.stackSize <= 0)
					this.setInventorySlotContents(0, null);
				else
					this.setInventorySlotContents(0, copy);
				this.markDirty();
			}
		}
		
		// String side = this.worldObj.isRemote ? "client" : "server";
		// MultiMod.log.info(side + ":" + this.colorStorage[0] + "|" +
		// this.colorStorage[1] + "|"
		// + this.colorStorage[2]);
	}
	
	@SuppressWarnings("unused")
	private boolean checkDyeCanAbsorb() {
		if (this.getStackInSlot(0) != null) {
			ItemStack dyeStack = this.getStackInSlot(0);
			double[] addedDye = CoreUtilHex.getColorFromDye(dyeStack);
			return this.canAbsorb(addedDye);
		}
		return false;
	}
	
	private void absorbDye(ItemStack dyeStack, double[] addedDye) {
		if (this.canAbsorb(addedDye)) {
			this.setColorStorage(0, this.getColorStorage()[0] + addedDye[0]);
			this.setColorStorage(1, this.getColorStorage()[1] + addedDye[1]);
			this.setColorStorage(2, this.getColorStorage()[2] + addedDye[2]);
			this.decrStackSize(0, 1);
			this.markDirty();
		}
	}
	
	private boolean canAbsorb(double[] addedDye) {
		double currentR = this.getColorStorage()[0], currentG = this.getColorStorage()[1], currentB = this
				.getColorStorage()[2];
		return currentR + addedDye[0] <= TileEntityColorizer.maxColorStorage
				&& currentG + addedDye[1] <= TileEntityColorizer.maxColorStorage
				&& currentB + addedDye[2] <= TileEntityColorizer.maxColorStorage;
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		// Re-sync server-client colorStorage for GUI
		// PacketSubColorsTE packet = new PacketSubColorsTE(this.xCoord,
		// this.yCoord, this.zCoord,
		// this.colorStorage);
		// MultiMod.packetChannel.sendToAll(packet);
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		
		tagCom.setInteger("delay", this.dyeDelay);
		
		tagCom.setDouble("colorStorageR", this.getColorStorage()[0]);
		tagCom.setDouble("colorStorageG", this.getColorStorage()[1]);
		tagCom.setDouble("colorStorageB", this.getColorStorage()[2]);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
		this.getColorStorage()[0] = tagCom.getDouble("colorStorageR");
		this.getColorStorage()[1] = tagCom.getDouble("colorStorageG");
		this.getColorStorage()[2] = tagCom.getDouble("colorStorageB");
		
		this.dyeDelay = tagCom.getInteger("delay");
	}
	
	// Client Server Sync
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.func_148857_g());
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCom = new NBTTagCompound();
		this.writeToNBT(tagCom);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord,
				this.blockMetadata, tagCom);
	}
	
	public double[] getColorStorage() {
		return colorStorage;
	}
	
	public void setColorStorage(double[] colorStorage) {
		this.colorStorage = colorStorage;
		// String side = this.worldObj.isRemote?"client":"server";
		// MultiDye.log.info(side + ":a:" + this.colorStorage[0] + "|" +
		// this.colorStorage[1] + "|"
		// + this.colorStorage[2]);
	}
	
	public void setColorStorage(int index, double value) {
		this.colorStorage[index] = value;
		// String side = this.worldObj.isRemote?"client":"server";
		// MultiDye.log.info(side + ":b:" + this.colorStorage[0] + "|" +
		// this.colorStorage[1] + "|"
		// + this.colorStorage[2]);
	}
	
	public void addDye(double[] sumHex) {
		double[] costs = sumHex;
		
		DecimalFormat df = new DecimalFormat("##.##");
		
		for (int i = 0; i < costs.length; i++) {
			costs[i] = Double.parseDouble(df.format(costs[i]));
		}
		
		double newR = Double.parseDouble(df.format(this.colorStorage[0] + costs[0]));
		double newG = Double.parseDouble(df.format(this.colorStorage[1] + costs[1]));
		double newB = Double.parseDouble(df.format(this.colorStorage[2] + costs[2]));
		double[] newStorage = new double[] {
				newR, newG, newB
		};
		this.colorStorage = newStorage;
		this.markDirty();
	}
	
}
