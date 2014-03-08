package com.countrygamer.auxiliaryobjects.client.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.countrygamer.auxiliaryobjects.Capo;
import com.countrygamer.auxiliaryobjects.blocks.tiles.TileEntityColorizer;
import com.countrygamer.auxiliaryobjects.inventory.ContainerColorizer;
import com.countrygamer.auxiliaryobjects.items.ItemMultiDye;
import com.countrygamer.auxiliaryobjects.lib.Reference;
import com.countrygamer.auxiliaryobjects.packet.PacketSaveDyeColor;
import com.countrygamer.auxiliaryobjects.packet.PacketSubColorsTE;
import com.countrygamer.core.client.gui.GuiContainerBlockBase;
import com.countrygamer.core.lib.CoreUtilHex;

public class GuiColorizer extends GuiContainerBlockBase {
	
	private TileEntityColorizer	tileEntColor;
	
	private GuiButton			setColor_Button;
	private GuiTextField		typeColor_Field;
	private GuiTextField		rVal_Field, gVal_Field, bVal_Field;
	private GuiSlider			rSlider;
	private int					rStorageX1	= 0, rStorageY1 = 0;
	private int					gStorageX1	= 0, gStorageY1 = 0;
	private int					bStorageX1	= 0, bStorageY1 = 0;
	private int					setButtonX	= 0, setButtonY = 0;
	
	public GuiColorizer(EntityPlayer player, TileEntityColorizer tileEnt) {
		super(player, new ContainerColorizer(player.inventory, tileEnt));
		this.tileEntColor = tileEnt;
		this.setupGui(tileEnt.getInventoryName(), new ResourceLocation(Reference.MOD_ID,
				"textures/gui/colorizer.png"));
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.setButtonX = (this.xSize / 2) + 7;
		this.setButtonY = (this.ySize / 4) + 19;
		this.buttonList.add(this.setColor_Button = new GuiButton(0, this.guiLeft + this.setButtonX,
				this.guiTop + this.setButtonY, 55, 20, "Set Color"));
		this.setColor_Button.enabled = this.buttonCheck();
		
		this.setupTextField(this.typeColor_Field = new GuiTextField(this.fontRendererObj,
				this.guiLeft + (this.xSize / 2) - 44, this.guiTop + (this.ySize / 2) - 25, 45, 10),
				6);
		this.setupTextField(this.rVal_Field = new GuiTextField(this.fontRendererObj, this.guiLeft
				+ (this.xSize / 4) + 19, this.guiTop + 10, 26, 10), 3);
		this.setupTextField(this.gVal_Field = new GuiTextField(this.fontRendererObj, this.guiLeft
				+ (this.xSize / 4) + 19, this.guiTop + 26, 26, 10), 3);
		this.setupTextField(this.bVal_Field = new GuiTextField(this.fontRendererObj, this.guiLeft
				+ (this.xSize / 4) + 19, this.guiTop + 42, 26, 10), 3);
		
		//this.buttonList.add(this.rSlider = new GuiSlider(1, this.guiLeft + 94, this.guiTop + 7, 75,
		//		20, 0.0F, 255.0F, ""));
		
	}
	
	@Override
	public void updateScreen() {
		// button checking
		this.setColor_Button.enabled = this.buttonCheck();
		
	}
	
	private boolean buttonCheck() {
		boolean validStack = this.inventorySlots.getSlot(1).getStack() != null;
		return this.hasColorsForDyeing() && validStack;
	}
	
	private boolean hasColorsForDyeing() {
		boolean hasColors = false;
		double[] costs = this.getColorsForDyeing();
		hasColors = this.tileEntColor.getColorStorage()[0] >= costs[0]
				&& this.tileEntColor.getColorStorage()[1] >= costs[1]
				&& this.tileEntColor.getColorStorage()[2] >= costs[2];
		return hasColors;
	}
	
	private double[] getColorsForDyeing() {
		String hexValue = this.typeColor_Field != null ? this.typeColor_Field.getText() : "";
		return CoreUtilHex.getSumHex(hexValue);
	}
	
	@Override
	protected void keyTyped(char letter, int keycode) {
		boolean wasField = false;
		boolean rgb_change = false;
		if (this.typeColor_Field.textboxKeyTyped(letter, keycode)) {
			this.sendKeyPacket(this.rVal_Field);
			if (CoreUtilHex.isValidHexString(this.typeColor_Field.getText())) {
				int[] rgb = CoreUtilHex.convertHexToRGB(this.typeColor_Field.getText());
				this.rVal_Field.setText(rgb[0] + "");
				this.gVal_Field.setText(rgb[1] + "");
				this.bVal_Field.setText(rgb[2] + "");
			}
			wasField = true;
		}
		
		if (this.rVal_Field.textboxKeyTyped(letter, keycode)) {
			this.sendKeyPacket(this.rVal_Field);
			wasField = rgb_change = true;
		}
		if (this.gVal_Field.textboxKeyTyped(letter, keycode)) {
			this.sendKeyPacket(this.gVal_Field);
			wasField = rgb_change = true;
		}
		if (this.bVal_Field.textboxKeyTyped(letter, keycode)) {
			this.sendKeyPacket(this.bVal_Field);
			wasField = rgb_change = true;
		}
		
		if (!wasField) {
			super.keyTyped(letter, keycode);
		}
		
		if (rgb_change) this.updateHexField();
	}
	
	private void updateHexField() {
		int r = 255;
		int g = 255;
		int b = 255;
		try {
			r = Integer.parseInt(this.rVal_Field.getText());
			g = Integer.parseInt(this.gVal_Field.getText());
			b = Integer.parseInt(this.bVal_Field.getText());
		} catch (NumberFormatException e) {
			
		}
		this.typeColor_Field.setText(CoreUtilHex.convertRGBtoHexString(r, g, b));
	}
	
	@Override
	protected void buttonPress(int id) {
		if (id == this.setColor_Button.id) {
			String textColor = this.typeColor_Field.getText();
			if (CoreUtilHex.isValidHexString(textColor)) {
				try {
					Integer.parseInt(textColor, 16);
				} catch (NumberFormatException e) {
					this.invalidColorPrint();
					return;
				}
				PacketSaveDyeColor packet = new PacketSaveDyeColor(this.typeColor_Field.getText());
				Capo.packetChannel.sendToServer(packet);
				Capo.packetChannel.sendToAll(packet);
				
				double[] costs = this.getColorsForDyeing();
				
				DecimalFormat df = new DecimalFormat("##.##");
				double newR = Double.parseDouble(df.format(this.tileEntColor.getColorStorage()[0]
						- costs[0]));
				double newG = Double.parseDouble(df.format(this.tileEntColor.getColorStorage()[1]
						- costs[1]));
				double newB = Double.parseDouble(df.format(this.tileEntColor.getColorStorage()[2]
						- costs[2]));
				
				/*
				AuxiliaryObjects.log.info(this.tileEntColor.getColorStorage()[0] + " : " + costs[0] + " : "
						+ newR);
				AuxiliaryObjects.log.info(this.tileEntColor.getColorStorage()[1] + " : " + costs[1] + " : "
						+ newG);
				AuxiliaryObjects.log.info(this.tileEntColor.getColorStorage()[2] + " : " + costs[2] + " : "
						+ newB);
				*/
				//this.tileEntColor.setColorStorage(0, newR);
				//this.tileEntColor.setColorStorage(1, newG);
				//this.tileEntColor.setColorStorage(2, newB);
				
				PacketSubColorsTE packet2 = new PacketSubColorsTE(this.tileEntColor.xCoord,
						this.tileEntColor.yCoord, this.tileEntColor.zCoord, costs);
				Capo.packetChannel.sendToServer(packet2);
				Capo.packetChannel.sendToAll(packet2);
				
			}
			else {
				this.invalidColorPrint();
			}
		}
		if (id == 1) {
			this.rVal_Field.setText(this.rSlider.actualValue + "");
		}
		
	}
	
	private void invalidColorPrint() {
		this.thePlayer.addChatComponentMessage(new ChatComponentText(
				"Invalid color code. Must be a valid 6 digit Hex color code."));
	}
	
	@Override
	protected void drawTitle(int titleX, int titleY) {
		super.drawTitle(5, 5);
	}
	
	@Override
	protected void foregroundText() {
		super.foregroundText();
		// draw current item hex value
		if (this.tileEnt.getStackInSlot(1) != null) {
			ItemStack itemStack = this.tileEnt.getStackInSlot(1);
			NBTTagCompound tagCom = new NBTTagCompound();
			if (itemStack.hasTagCompound()) tagCom = itemStack.getTagCompound();
			int color = tagCom.getInteger(ItemMultiDye.colorTagStr);
			// convert int color to a hexadecimal string
			String hexColor = Integer.toHexString(color);
			// get extra zeros on front of hexadecimal
			if (hexColor.length() < 6) {
				int zeros_to_add = 6 - hexColor.length();
				String zeros = "";
				for (int i = 0; i < zeros_to_add; i++) {
					zeros += "0";
				}
				hexColor = zeros + hexColor;
			}
			// show string
			this.string(hexColor, (this.xSize / 2) - 40, (this.ySize / 2) - 12, color);
		}
		
		this.string("R:", (this.xSize / 4) + 10, 11);
		this.string("G:", (this.xSize / 4) + 10, 27);
		this.string("B:", (this.xSize / 4) + 10, 43);
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.bkgdTex);
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		super.backgroundObjects();
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.bkgdTex);
		
		int maxSize = (int) (this.tileEntColor.maxColorStorage * 10);
		int rSize = (int) (this.tileEntColor.getColorStorage()[0] * 10);
		int gSize = (int) (this.tileEntColor.getColorStorage()[1] * 10);
		int bSize = (int) (this.tileEntColor.getColorStorage()[2] * 10);
		
		int left = this.guiLeft + 7;
		int baseY = this.guiTop + (this.ySize / 4) - 3;
		this.rStorageX1 = 8;
		this.rStorageY1 = (this.ySize / 4) - 3;
		
		this.gStorageX1 = 16;
		this.gStorageY1 = (this.ySize / 4) - 3;
		
		this.bStorageX1 = 24;
		this.bStorageY1 = (this.ySize / 4) - 3;
		
		this.setButtonX = (this.xSize / 2) + 7;
		this.setButtonY = (this.ySize / 4) + 19;
		
		// x, y, u, v, width, height
		this.drawTexturedModalRect(this.guiLeft + this.rStorageX1, this.guiTop + this.rStorageY1
				+ (maxSize - rSize), 176 + 00, (maxSize - rSize), 5, maxSize);
		this.drawTexturedModalRect(this.guiLeft + this.gStorageX1, this.guiTop + this.bStorageY1
				+ (maxSize - gSize), 176 + 05, (maxSize - gSize), 5, maxSize);
		this.drawTexturedModalRect(this.guiLeft + this.bStorageX1, this.guiTop + this.bStorageY1
				+ (maxSize - bSize), 176 + 10, (maxSize - bSize), 5, maxSize);
		
	}
	
	public void drawScreen(int mouseX, int mouseY, float par3) {
		super.drawScreen(mouseX, mouseY, par3);
		
		List hoverInfo = new ArrayList();
		
		int width = 6, height = 41;
		if (this.func_146978_c(this.rStorageX1, this.rStorageY1, width, height, mouseX, mouseY)) {
			// type storage
			hoverInfo.add("Red Dye Storage");
			// storage ratio
			hoverInfo.add("Stored: " + this.tileEntColor.getColorStorage()[0] + "/4.0");
			// total storage
			hoverInfo.add("Total Stored:");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[0] + "/4.0 Red");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[1] + "/4.0 Green");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[2] + "/4.0 Blue");
			
			this.renderHoverTip(hoverInfo, mouseX, mouseY);
		}
		
		if (this.func_146978_c(this.gStorageX1, this.gStorageY1, width, height, mouseX, mouseY)) {
			// type storage
			hoverInfo.add("Green Dye Storage");
			// storage ratio
			hoverInfo.add("Stored: " + this.tileEntColor.getColorStorage()[1] + "/4.0");
			// total storage
			hoverInfo.add("Total Stored:");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[0] + "/4.0 Red");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[1] + "/4.0 Green");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[2] + "/4.0 Blue");
			
			this.renderHoverTip(hoverInfo, mouseX, mouseY);
		}
		
		if (this.func_146978_c(this.bStorageX1, this.bStorageY1, width, height, mouseX, mouseY)) {
			// type storage
			hoverInfo.add("Blue Dye Storage");
			// storage ratio
			hoverInfo.add("Stored: " + this.tileEntColor.getColorStorage()[2] + "/4.0");
			// total storage
			hoverInfo.add("Total Stored:");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[0] + "/4.0 Red");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[1] + "/4.0 Green");
			hoverInfo.add("     " + this.tileEntColor.getColorStorage()[2] + "/4.0 Blue");
			
			this.renderHoverTip(hoverInfo, mouseX, mouseY);
		}
		
		if (!this.setColor_Button.enabled) {
			width = 54;
			height = 19;
			if (this.func_146978_c(this.setButtonX, this.setButtonY, width, height, mouseX, mouseY)) {
				hoverInfo.add("Set Color Button");
				
				if (!CoreUtilHex.isValidHexString(this.typeColor_Field.getText()))
					hoverInfo.add("Invalid hex code");
				if (!this.hasColorsForDyeing()) hoverInfo.add("Does not have apt color storage");
				if (this.tileEntColor.getStackInSlot(0) == null)
					hoverInfo.add("Not MultiDye item");
				
				this.renderHoverTip(hoverInfo, mouseX, mouseY);
			}
		}
		
	}
	
	protected void renderHoverTip(List hoverInfo, int mouseX, int mouseY) {
		for (int k = 0; k < hoverInfo.size(); ++k) {
			hoverInfo.set(k, EnumChatFormatting.GRAY + (String) hoverInfo.get(k));
		}
		
		this.func_146283_a(hoverInfo, mouseX, mouseY);
		drawHoveringText(hoverInfo, mouseX, mouseY, this.fontRendererObj);
	}
	
}
