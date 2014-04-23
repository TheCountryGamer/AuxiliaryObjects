package com.countrygamer.capo.client.gui;

import java.io.UnsupportedEncodingException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.inventory.container.ContainerColorizerII;
import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.packet.PacketTriggerColorizerII;
import com.countrygamer.capo.common.tileentity.TileEntityColorizerII;
import com.countrygamer.core.Base.client.gui.GuiContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;
import com.countrygamer.core.common.lib.HexStringConverter;

public class GuiColorizerII extends GuiContainerBlockBase {
	
	private GuiTextField input;
	private GuiButton saveColor;
	private GuiButton setTriColors;
	
	public GuiColorizerII(EntityPlayer player, TileEntityInventoryBase tileEnt) {
		super(player, new ContainerColorizerII(player.inventory, tileEnt));
		this.setupGui("Colorizer Mark II", new ResourceLocation(Reference.MOD_ID,
				"textures/gui/colorizerMII.png"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		int bID = -1;
		this.buttonList.clear();
		this.buttonList.add(this.saveColor = new GuiButton(++bID, this.guiLeft
				+ (3 * (this.xSize / 4)), this.guiTop + (this.ySize / 5), 30, 20, "Store Color"));
		this.buttonList.add(this.setTriColors = new GuiButton(++bID, this.guiLeft
				+ (this.xSize / 3) + 23, this.guiTop + (2 * (this.ySize / 5)) - 7, 60, 20,
				"Set Colors"));
		
		this.input = new GuiTextField(this.fontRendererObj, this.guiLeft, this.guiTop, 80, 10);
		this.setupTextField(this.input, 9);
		
	}
	
	@Override
	protected void buttonPress(int id) {
		if (id == this.saveColor.id) {
			String text = this.input.getText();
			if (text.length() == 9) {
				String hexString = "";
				try {
					hexString = HexStringConverter.getHexStringConverterInstance()
							.stringToHex(text);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				String[] hexStrings = new String[3];
				int[] colors = new int[3];
				for (int i = 0; i < 3; i++) {
					hexStrings[i] = hexString.substring(i * 6, i * 6 + 6);
					try {
						colors[i] = Integer.parseInt(hexStrings[i], 16);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				/*
				LogBlock log = new LogBlock(Capo.log, "\n");
				for (int i = 0; i < 3; i++) {
					log.addWithLine(hexStrings[i] + "");
					log.addWithLine("\t" + Integer.parseInt(hexStrings[i], 16));
					log.addWithLine(colors[i] + "");
					log.addWithLine("\t" + Integer.toHexString(colors[i]));
					log.addWithLine("");
				}
				log.log();
				 */
				PacketTriggerColorizerII packet = new PacketTriggerColorizerII(this.tileEnt.xCoord,
						this.tileEnt.yCoord, this.tileEnt.zCoord, "saveColor", colors);
				Capo.packetChannel.sendToServer(packet);
				Capo.packetChannel.sendToAll(packet);
			}
		}
		else if (id == this.setTriColors.id) {
			Capo.log.info("SetTriColors hit, sending packet");
			PacketTriggerColorizerII packet = new PacketTriggerColorizerII(this.tileEnt.xCoord,
					this.tileEnt.yCoord, this.tileEnt.zCoord, "setTriColors");
			Capo.packetChannel.sendToServer(packet);
			Capo.packetChannel.sendToAll(packet);
		}
	}
	
	@Override
	protected void foregroundText() {
		super.foregroundText();
		
		TileEntityColorizerII tileEnt = (TileEntityColorizerII) this.tileEnt;
		
		int[] colors = tileEnt.getHexColors();
		for (int i = 0; i < colors.length; i++) {
			if (colors[i] != -1)
				this.string(Integer.toHexString(colors[i]), 10, 50 + (i * 10), colors[i]);
		}
		
	}
	
	@Override
	protected void backgroundObjects() {
		super.backgroundObjects();
		this.input.drawTextBox();
	}
	
}
