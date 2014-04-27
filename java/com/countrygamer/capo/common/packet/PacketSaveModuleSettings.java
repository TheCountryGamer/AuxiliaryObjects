package com.countrygamer.capo.common.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.capo.common.item.ItemModuleWall;
import com.countrygamer.capo.common.tileentity.TileEntityAssembler;
import com.countrygamer.core.Base.common.packet.AbstractPacket;

public class PacketSaveModuleSettings extends AbstractPacket {
	
	int x, y, z;
	int[] offsets, bounds;
	ItemStack camoStack;
	
	public PacketSaveModuleSettings() {
	}
	
	public PacketSaveModuleSettings(int x, int y, int z, int[] offsets, int[] bounds,
			ItemStack camoStack) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.offsets = offsets;
		this.bounds = bounds;
		this.camoStack = camoStack;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(this.offsets.length);
		for (int offset : this.offsets)
			buffer.writeInt(offset);
		buffer.writeInt(this.bounds.length);
		for (int bound : this.bounds)
			buffer.writeInt(bound);
		
		buffer.writeBoolean(camoStack != null);
		if (camoStack != null) {
			buffer.writeInt(Item.getIdFromItem(this.camoStack.getItem()));
			buffer.writeInt(this.camoStack.getItemDamage());
		}
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		int l;
		l = buffer.readInt();
		this.offsets = new int[l];
		for (int i = 0; i < l; i++)
			offsets[i] = buffer.readInt();
		l = buffer.readInt();
		this.bounds = new int[l];
		for (int i = 0; i < l; i++)
			bounds[i] = buffer.readInt();
		
		if (buffer.readBoolean()) {
			Item camoItemBlock = Item.getItemById(buffer.readInt());
			int meta = buffer.readInt();
			this.camoStack = new ItemStack(camoItemBlock, 1, meta);
		}
		else {
			this.camoStack = null;
		}
		
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		this.saveSettings(player);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		this.saveSettings(player);
	}
	
	private void saveSettings(EntityPlayer player) {
		TileEntity tileEnt = player.worldObj.getTileEntity(x, y, z);
		if (tileEnt != null && tileEnt instanceof TileEntityAssembler) {
			TileEntityAssembler tileEntAssem = (TileEntityAssembler) tileEnt;
			ItemStack moduleStack = tileEntAssem.getStackInSlot(0);
			if (moduleStack != null && moduleStack.getItem() instanceof ItemModuleWall) {
				ItemModuleWall module = (ItemModuleWall) moduleStack.getItem();
				
				moduleStack.setTagCompound(module.saveCamoStack(moduleStack, camoStack));
				
				moduleStack.setTagCompound(module.setOffset(moduleStack, 'X', offsets[0]));
				moduleStack.setTagCompound(module.setOffset(moduleStack, 'Y', offsets[1]));
				moduleStack.setTagCompound(module.setOffset(moduleStack, 'Z', offsets[2]));
				
				for (int i = 0; i < bounds.length; i++) {
					boolean isMinVsMax = this.isMinVsMaxFromBoundIndex(i);
					char axis = this.getAxisFromBoundIndex(i);
					moduleStack.setTagCompound(module.setBound(moduleStack, isMinVsMax, axis,
							bounds[i]));
				}
			}
		}
		
	}
	
	private char getAxisFromBoundIndex(int index) {
		if (index == 0 || index == 1) {
			return 'Z';
		}
		else if (index == 2 || index == 3) {
			return 'X';
		}
		else if (index == 4 || index == 5) {
			return 'Y';
		}
		else {
			return '~';
		}
	}
	
	// TODO
	private boolean isMinVsMaxFromBoundIndex(int index) {
		// North, West, and Bottom are minimum directions
		return index == 0 || index == 3 || index == 5;
	}
	
}
