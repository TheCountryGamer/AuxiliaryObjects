package com.countrygamer.capo.common.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.inventory.container.ContainerColorizer;
import com.countrygamer.capo.common.item.ItemMultiDye;
import com.countrygamer.capo.common.tileentity.TileEntityColorizer;
import com.countrygamer.core.Base.common.packet.AbstractPacket;

public class PacketSaveDyeColor extends AbstractPacket {
	String	colorString	= "";
	int		length		= 0;
	
	public PacketSaveDyeColor() {
	}
	
	public PacketSaveDyeColor(String colorString) {
		this.colorString = colorString;
		this.length = colorString.length();
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		buffer.writeInt(this.length);
		char[] chars = this.colorString.toCharArray();
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
		this.colorString = str;
	}
	
	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}
	
	@Override
	public void handleServerSide(EntityPlayer player) {
		if (!player.worldObj.isRemote && player.openContainer instanceof ContainerColorizer) {
			TileEntityColorizer colorizer = (TileEntityColorizer) ((ContainerColorizer) player.openContainer).tileEnt;
			ItemStack mutliDyeStack = colorizer.getStackInSlot(1);
			if (mutliDyeStack != null && mutliDyeStack.getItem() instanceof ItemMultiDye) {
				NBTTagCompound tagCom = null;
				if (mutliDyeStack.hasTagCompound())
					tagCom = mutliDyeStack.getTagCompound();
				else
					tagCom = new NBTTagCompound();
				
				int color = tagCom.getInteger(ItemMultiDye.colorTagStr);
				try {
					color = Integer.parseInt(this.colorString, 16);
				} catch (NumberFormatException e) {
					Capo.log.info("This is an error. Report to mod author.");
					e.printStackTrace();
				}
				
				tagCom.setInteger(ItemMultiDye.colorTagStr, color);
				mutliDyeStack.setTagCompound(tagCom);
			}
		}
	}
	
}
