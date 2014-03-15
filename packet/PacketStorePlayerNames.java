package com.countrygamer.capo.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.capo.blocks.tiles.TileEntityPlayerChecker;
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
