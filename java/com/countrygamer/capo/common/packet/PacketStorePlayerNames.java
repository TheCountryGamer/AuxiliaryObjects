package com.countrygamer.capo.common.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.tileentity.TileEntityPlayerChecker;
import com.countrygamer.core.Base.common.packet.AbstractPacket;

public class PacketStorePlayerNames extends AbstractPacket {
	
	int x, y, z;
	String playerName;
	boolean addToList;
	
	public PacketStorePlayerNames() {
	}
	
	public PacketStorePlayerNames(int xCoord, int yCoord, int zCoord, String playerName,
			boolean addToList) {
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
		this.store(player);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		this.store(player);
	}
	
	private void store(EntityPlayer player) {
		TileEntity tEnt = player.worldObj.getTileEntity(this.x, this.y, this.z);
		if (tEnt instanceof TileEntityPlayerChecker) {
			//Capo.log.info(this.playerName);
			if (this.addToList)
				((TileEntityPlayerChecker) tEnt).addPlayerToActive(this.playerName);
			else
				((TileEntityPlayerChecker) tEnt).removePlayerFromActive(this.playerName);
			Capo.log.info("Update on " + (player.worldObj.isRemote ? "Client" : "Server"));
			player.worldObj.scheduleBlockUpdate(this.x, this.y, this.z, 
					player.worldObj.getBlock(this.x, this.y, this.z), 10);
			
		}
	}
	
}
