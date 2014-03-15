package com.countrygamer.capo.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.capo.items.ItemInventorySack;
import com.countrygamer.core.Handler.AbstractPacket;

public class PacketSackName extends AbstractPacket {
	
	String	name	= "";
	int		length	= 0;
	
	public PacketSackName() {
	}
	
	public PacketSackName(String name) {
		this.name = name;
		this.length = name.length();
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.length);
		char[] chars = this.name.toCharArray();
		for (int i = 0; i < this.length; i++) {
			buffer.writeChar(chars[i]);
		}
	}
	
	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		this.length = buffer.readInt();
		String str = "";
		for (int i = 0; i < this.length; i++) {
			str += buffer.readChar();
		}
		this.name = str;
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		ItemStack held = player.getHeldItem();
		if (!player.worldObj.isRemote) {
			if (held != null && held.getItem() instanceof ItemInventorySack) {
				NBTTagCompound tagCom = null;
				if (held.hasTagCompound())
					tagCom = held.getTagCompound();
				else
					tagCom = new NBTTagCompound();
				tagCom.setString(ItemInventorySack.sackName, this.name);
				player.getHeldItem().setTagCompound(tagCom);
			}
		}
	}
	
}
