package com.countrygamer.capo.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.core.Base.packet.AbstractPacket;

public class PacketOpenAssemblerSettings extends AbstractPacket {
	
	int x, y, z;
	boolean backToAssembler;
	
	public PacketOpenAssemblerSettings() {
	}
	
	public PacketOpenAssemblerSettings(int x, int y, int z, boolean goBack) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.backToAssembler = goBack;
		
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeBoolean(this.backToAssembler);
		
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		this.backToAssembler = buffer.readBoolean();
		
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		this.open(player);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		this.open(player);
	}
	
	private void open(EntityPlayer player) {
		String side = player.worldObj.isRemote ? "Client" : "Server";
		Capo.log.info(side + " recieved");
		int guiID = this.backToAssembler ? Reference.guiAssembler : Reference.guiAssemblerSettings;
		player.openGui(Reference.MOD_ID, guiID, player.worldObj, x, y, z);
	}
	
}
