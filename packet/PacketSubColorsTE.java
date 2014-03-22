package com.countrygamer.capo.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.capo.blocks.tiles.TileEntityColorizer;
import com.countrygamer.core.Base.packet.AbstractPacket;

public class PacketSubColorsTE extends AbstractPacket {
	
	private int	x, y, z;
	private double	rSum, gSum, bSum;
	
	public PacketSubColorsTE() {
	}
	
	public PacketSubColorsTE(int x, int y, int z, double[] sumHex) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.rSum = sumHex[0];
		this.gSum = sumHex[1];
		this.bSum = sumHex[2];
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		buffer.writeDouble(this.rSum);
		buffer.writeDouble(this.gSum);
		buffer.writeDouble(this.bSum);
		
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.rSum = buffer.readDouble();
		this.gSum = buffer.readDouble();
		this.bSum = buffer.readDouble();
		
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		this.colorUpdate(player);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		this.colorUpdate(player);
	}
	
	private void colorUpdate(EntityPlayer player) {
		TileEntityColorizer tileEnt = (TileEntityColorizer) player.worldObj.getTileEntity(this.x,
				this.y, this.z);
		tileEnt.addDye(new double[] {
				this.rSum, this.gSum, this.bSum
		});
	}
	
}
