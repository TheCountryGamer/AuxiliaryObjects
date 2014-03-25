package com.countrygamer.capo.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityCompressor;
import com.countrygamer.capo.inventory.ContainerCompressor;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.capo.packet.PacketCompressorMode;
import com.countrygamer.core.Base.client.gui.GuiButtonArrow;
import com.countrygamer.core.Base.client.gui.GuiButtonArrow.ButtonType;
import com.countrygamer.core.Base.client.gui.GuiContainerBlockBase;

public class GuiCompressor extends GuiContainerBlockBase {
	
	private GuiButtonArrow leftMode, rightMode;
	private GuiButtonArrow leftState, rightState;
	
	public GuiCompressor(EntityPlayer player, TileEntityCompressor tileEnt) {
		super(player, new ContainerCompressor(player.inventory, tileEnt));
		this.setupGui("Compressor", new ResourceLocation(Reference.MOD_ID,
				"textures/gui/compressor.png"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		int bID = -1;
		this.buttonList.clear();
		this.buttonList.add(this.leftMode = new GuiButtonArrow(++bID, this.guiLeft
				+ (this.xSize / 3), this.ySize / 3 + 7, ButtonType.LEFT));
		this.buttonList.add(this.rightMode = new GuiButtonArrow(++bID, this.guiLeft + this.xSize
				- (this.xSize / 7), this.ySize / 3 + 7, ButtonType.RIGHT));
		
		this.buttonList.add(this.leftState = new GuiButtonArrow(++bID, this.guiLeft
				+ (this.xSize / 3), this.ySize / 2 + 15, ButtonType.LEFT));
		this.buttonList.add(this.rightState = new GuiButtonArrow(++bID, this.guiLeft + this.xSize
				- (this.xSize / 7), this.ySize / 2 + 15, ButtonType.RIGHT));
		
	}
	
	@Override
	protected void buttonPress(int id) {
		super.buttonPress(id);
		PacketCompressorMode packet = null;
		if (id == this.leftMode.id) {
			packet = new PacketCompressorMode(this.tileEnt.xCoord, this.tileEnt.yCoord,
					this.tileEnt.zCoord, 1, -1);
		}
		else if (id == this.rightMode.id) {
			packet = new PacketCompressorMode(this.tileEnt.xCoord, this.tileEnt.yCoord,
					this.tileEnt.zCoord, 1, 1);
		}
		if (id == this.leftState.id) {
			packet = new PacketCompressorMode(this.tileEnt.xCoord, this.tileEnt.yCoord,
					this.tileEnt.zCoord, 2, -1);
		}
		else if (id == this.rightState.id) {
			packet = new PacketCompressorMode(this.tileEnt.xCoord, this.tileEnt.yCoord,
					this.tileEnt.zCoord, 2, 1);
		}
		if (packet != null) {
			Capo.packetChannel.sendToServer(packet);
			Capo.packetChannel.sendToAll(packet);
		}
	}
	
	@Override
	protected void foregroundText() {
		super.foregroundText();
		String text = "";
		int strLength = 0;
		
		text = "Compression Mode";
		strLength = this.fontRendererObj.getStringWidth(text);
		this.string(text, (this.xSize / 2) - (strLength / 4), this.ySize / 10);
		
		text = "Redstone State";
		strLength = this.fontRendererObj.getStringWidth(text);
		this.string(text, (this.xSize / 2) - (strLength / 4) + 5, this.ySize / 4 + 10);
		
		if (this.tileEnt instanceof TileEntityCompressor) {
			TileEntityCompressor compressor = (TileEntityCompressor) tileEnt;
			text = "MODE";
			if (compressor.mode >= 1) {
				text = "Compress";
				if (compressor.mode >= 2) {
					text = "Decompress";
					if (compressor.mode >= 3) {
						text = "Auto";
					}
				}
			}
			strLength = this.fontRendererObj.getStringWidth(text);
			
			this.string(text, (this.xSize / 2), this.ySize / 10 + 13);
			
			text = "STATE";
			if (compressor.redstoneMode >= 1) {
				text = "Ignore";
				if (compressor.redstoneMode >= 2) {
					text = "Low Signal";
					if (compressor.redstoneMode >= 3) {
						text = "High Signal";
					}
				}
			}
			strLength = this.fontRendererObj.getStringWidth(text);
			
			this.string(text, (this.xSize / 2), this.ySize / 3 + 10);
		}
		
	}
	
}
