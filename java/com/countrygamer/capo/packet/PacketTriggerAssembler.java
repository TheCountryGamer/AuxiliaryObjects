package com.countrygamer.capo.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityAssembler;
import com.countrygamer.core.Base.packet.AbstractPacket;

public class PacketTriggerAssembler extends AbstractPacket {
	
	int x, y, z;
	int function;
	
	public PacketTriggerAssembler() {}
	
	public PacketTriggerAssembler(int tileX, int tileY, int tileZ, String function) {
		this.x = tileX;
		this.y = tileY;
		this.z = tileZ;
		if (function.equals("loadUpgrades"))
			this.function = 0;
		else if (function.equals("saveUpgrades"))
			this.function = 1;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(this.function);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.function = buffer.readInt();
	}
	

	@Override
	public void handleClientSide(EntityPlayer player) {
		this.trigger(player);
	}
	

	@Override
	public void handleServerSide(EntityPlayer player) {
		this.trigger(player);
	}
	
	private void trigger(EntityPlayer player) {
		TileEntity tile = player.worldObj.getTileEntity(this.x, this.y, this.z);
		if (tile instanceof TileEntityAssembler) {
			TileEntityAssembler tileEnt = (TileEntityAssembler)tile;
			switch (this.function) {
				case 0:
					tileEnt.loadUpgrades();
					break;
				case 1:
					Capo.log.info("Send Save");
					tileEnt.saveUpgrades();
					break;
				default:
					break;
			}
		}
	}
	
}
