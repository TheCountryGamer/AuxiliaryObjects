package com.countrygamer.auxiliaryobjects.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.auxiliaryobjects.AuxiliaryObjects;
import com.countrygamer.auxiliaryobjects.blocks.tiles.TileEntityPlayerChecker;
import com.countrygamer.core.Handler.AbstractPacket;

public class PacketStorePlayerNames extends AbstractPacket {

	int x, y, z;
	String playerName;
	boolean addToList;
	
	public PacketStorePlayerNames() {
	}

	public PacketStorePlayerNames(int xCoord, int yCoord, int zCoord,
			String playerName, boolean addToList) {
		this.x = xCoord;
		this.y = yCoord;
		this.z = zCoord;
		this.playerName = playerName;
		this.addToList = addToList;
		
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		// set that from this
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		buffer.writeBoolean(this.addToList);
		char[] letters = this.playerName.toCharArray();
		int length = letters.length;
		buffer.writeInt(length);
		for (int i = 0; i < length; i++)
			buffer.writeChar(letters[i]);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		// set this from that
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.addToList = buffer.readBoolean();
		int length = buffer.readInt();
		this.playerName = "";
		for (int i = 0; i < length; i++) {
			this.playerName += buffer.readChar();
		}
		
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		TileEntity tEnt = player.worldObj.getTileEntity(this.x, this.y, this.z);
		if (tEnt instanceof TileEntityPlayerChecker) {
			if (this.addToList)
				((TileEntityPlayerChecker)tEnt).addPlayerToActive(this.playerName);
			else
				((TileEntityPlayerChecker)tEnt).removePlayerFromActive(this.playerName);
		}
	}

}
