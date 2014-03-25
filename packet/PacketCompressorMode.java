package com.countrygamer.capo.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.capo.blocks.tiles.TileEntityCompressor;
import com.countrygamer.core.Base.packet.AbstractPacket;

public class PacketCompressorMode extends AbstractPacket {
	
	int x, y, z, mode, upDown;
	
	public PacketCompressorMode() {
	}
	
	/**
	 * @param x
	 *            tile entity xCoord
	 * @param y
	 *            tile entity yCoord
	 * @param z
	 *            tileEntity zCoord
	 * @param mode
	 *            which mode variable it will send to. 1 = mode, 2 = redstoneState
	 * @param state
	 *            get the next or previous value. <0 = previous, >0 = next, 0 = do nothing
	 *            (pointless packet)
	 */
	public PacketCompressorMode(int x, int y, int z, int mode, int upDown) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mode = mode;
		this.upDown = upDown;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		buffer.writeInt(this.mode);
		buffer.writeInt(this.upDown);
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.mode = buffer.readInt();
		this.upDown = buffer.readInt();
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		this.compress(player);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		this.compress(player);
		if (!player.worldObj.isRemote) {
			
			//player.worldObj.scheduleBlockUpdate(this.x, this.y, this.z,
			//		player.worldObj.getBlock(this.x, this.y, this.z), 10);
		}
	}
	
	private void compress(EntityPlayer player) {
		TileEntityCompressor tileEnt = (TileEntityCompressor) player.worldObj.getTileEntity(
				this.x, this.y, this.z);
		if (this.mode == 1) {
			if (this.upDown < 0) tileEnt.getPreviousMode();
			if (this.upDown > 0) tileEnt.getNextMode();
		}
		else if (this.mode == 2) {
			if (this.upDown < 0) tileEnt.getPreviousRedstoneState();
			if (this.upDown > 0) tileEnt.getNextRedstoneState();
		}
	}
}
