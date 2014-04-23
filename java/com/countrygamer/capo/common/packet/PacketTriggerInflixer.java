package com.countrygamer.capo.common.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.capo.common.tileentity.TileEntityInflixer;
import com.countrygamer.core.Base.common.packet.AbstractPacket;

public class PacketTriggerInflixer extends AbstractPacket {
	
	private int	x, y, z, type, partitionID;
	
	public PacketTriggerInflixer() {
	}
	
	public PacketTriggerInflixer(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = 0;
	}
	
	public PacketTriggerInflixer(int x, int y, int z, int type, int partitionID) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.partitionID = partitionID;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		buffer.writeInt(this.type);
		buffer.writeInt(this.partitionID);
		
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.type = buffer.readInt();
		this.partitionID = buffer.readInt();
		
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		TileEntityInflixer tileEnt = (TileEntityInflixer) player.worldObj.getTileEntity(this.x,
				this.y, this.z);
		if (this.type == 0)
			tileEnt.inflixItem();
		if (this.type == 1)
			tileEnt.partitionItem(this.partitionID);
		
	}
	
}
