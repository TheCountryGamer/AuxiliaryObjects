package com.countrygamer.capo.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityColorizerII;
import com.countrygamer.core.Base.packet.AbstractPacket;
import com.countrygamer.core.lib.LogBlock;


public class PacketTriggerColorizerII extends AbstractPacket {
	
	public static List<String> actionFromString = Arrays.asList((new String[] {
			"saveColor", "setTriColors"
	}));
	
	private int x, y, z, action;
	private int[] hexColors;
	
	public PacketTriggerColorizerII() {
	}
	
	public PacketTriggerColorizerII(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public PacketTriggerColorizerII(int x, int y, int z, String action) {
		this(x, y, z);
		this.action = actionFromString.indexOf(action);
	}
	
	public PacketTriggerColorizerII(int x, int y, int z, String action, int hexColor) {
		this(x, y, z, action);
		this.hexColors = new int[] {
			hexColor
		};
	}
	
	public PacketTriggerColorizerII(int x, int y, int z, String action, int[] colors) {
		this(x, y, z, action);
		this.hexColors = colors;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.x);
		buffer.writeInt(this.y);
		buffer.writeInt(this.z);
		buffer.writeInt(this.action);
		buffer.writeInt(this.hexColors.length);
		if (this.hexColors.length > 0) {
			for (int i = 0; i < this.hexColors.length; i++) {
				buffer.writeInt(this.hexColors[i]);
			}
		}
		
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.action = buffer.readInt();
		int length = buffer.readInt();
		if (length > 0) {
			this.hexColors = new int[length];
			for (int i = 0; i < length; i++) {
				this.hexColors[i] = buffer.readInt();
			}
		}
		
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		this.action(player);
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		this.action(player);
	}
	
	private void action(EntityPlayer player) {
		Capo.log.info("Recieved general packet message");
		TileEntityColorizerII tileEnt = (TileEntityColorizerII) player.worldObj.getTileEntity(
				this.x, this.y, this.z);
		if (this.action == actionFromString.indexOf("saveColor")) {
			int[] hexColors = tileEnt.getHexColors();
			if (this.hexColors.length == 1) {
				for (int i = 0; i < hexColors.length; i++) {
					if (hexColors[i] == -1) {
						tileEnt.setHexColor(i, this.hexColors[0]);
						break;
					}
					else if (i == hexColors.length - 1) {
						tileEnt.setHexColor(0, this.hexColors[0]);
					}
				}
			}
			else
				for (int i = 0; i < this.hexColors.length; i++) {
					tileEnt.setHexColor(i, this.hexColors[i]);
				}
		}
		
		if (this.action == actionFromString.indexOf("setTriColors")) {
			Capo.log.info("Got setTriColors message and triggering");
			tileEnt.setHexValuesToTriDye();
		}
		
		int[] colors = tileEnt.getHexColors();
		LogBlock log = new LogBlock(Capo.log, "\n");
		for (int i = 0; i < colors.length; i++) {
			log.add(colors[i] + " : ");
			log.addWithLine(Integer.toHexString(colors[i]));
		}
		log.log();
		
	}
	
}
