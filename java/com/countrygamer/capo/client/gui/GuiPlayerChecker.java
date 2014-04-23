package com.countrygamer.capo.client.gui;

import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.packet.PacketStorePlayerNames;
import com.countrygamer.capo.common.tileentity.TileEntityPlayerChecker;
import com.countrygamer.core.Base.client.gui.GuiButtonCheck;
import com.countrygamer.core.Base.client.gui.GuiScreenBlockBase;
import com.countrygamer.core.common.lib.CoreReference;

public class GuiPlayerChecker extends GuiScreenBlockBase {
	
	private int leftOfGui, topOfGui;
	protected int xSize = 176;
	protected int ySize = 166;
	
	public GuiTextField playerField;
	private int playerFieldX, playerFieldY, playerFieldWidth;
	private int playerFieldHeight, playerFieldBoxY, playerFieldBoxYMax;
	private int nameHeight = 10;
	// private String currentPlayerName = "";
	private String[] validNames = new String[] {};
	private HashMap<String, int[]> nameCoors = new HashMap<String, int[]>();
	
	private GuiButtonCheck checkButton, xButton;
	private GuiButton hideActive;
	private boolean hideActiveNames = false;
	
	private ArrayList<String> activeNames = new ArrayList<String>();
	
	public GuiPlayerChecker(TileEntityPlayerChecker tile) {
		super(tile);
		
	}
	
	@SuppressWarnings("unchecked")
	public void initGui() {
		super.initGui();
		this.leftOfGui = (this.width - this.xSize) / 2;
		this.topOfGui = (this.height - this.ySize) / 2;
		
		int buttonID = 0;
		this.buttonList.clear();
		
		this.buttonList.add(this.hideActive = new GuiButton(++buttonID, this.leftOfGui + this.xSize
				- 64, this.topOfGui + 20, 60, 20, "Hide Active"));
		this.buttonList.add(this.checkButton = new GuiButtonCheck(++buttonID, this.leftOfGui
				+ this.xSize - 60, this.topOfGui + 50, true));
		this.buttonList.add(this.xButton = new GuiButtonCheck(++buttonID, this.leftOfGui
				+ this.xSize - 30, this.topOfGui + 50, false));
		
		Keyboard.enableRepeatEvents(true);
		this.playerFieldX = this.leftOfGui + 10;
		this.playerFieldY = this.topOfGui + 20;
		this.playerFieldWidth = 100;
		this.playerFieldHeight = 10;
		this.playerFieldBoxY = this.playerFieldY + this.playerFieldHeight + 1;
		this.playerFieldBoxYMax = (this.ySize + 10) - this.playerFieldY;
		this.playerField = new GuiTextField(this.fontRendererObj, this.playerFieldX,
				this.playerFieldY, this.playerFieldWidth, this.playerFieldHeight);
		this.setupTextField(this.playerField, 16);
		
		this.refreshPlayerList();
	}
	
	@Override
	protected void buttonPress(int id) {
		String playerName = "";
		
		if (id == this.hideActive.id) {
			this.hideActiveNames = !this.hideActiveNames;
		}
		if (id == this.checkButton.id) {
			playerName = this.getActualStringFromTextField(this.playerField.getText());
			PacketStorePlayerNames packet = new PacketStorePlayerNames(tileEnt.xCoord,
					tileEnt.yCoord, tileEnt.zCoord, playerName, true);
			Capo.packetChannel.sendToServer(packet);
			Capo.packetChannel.sendToAll(packet);
		}
		else if (id == this.xButton.id) {
			playerName = this.getActualStringFromTextField(this.playerField.getText());
			PacketStorePlayerNames packet = new PacketStorePlayerNames(tileEnt.xCoord,
					tileEnt.yCoord, tileEnt.zCoord, playerName, false);
			Capo.packetChannel.sendToServer(packet);
			Capo.packetChannel.sendToAll(packet);
		}
		
	}
	
	protected void keyTyped(char letter, int par2) {
		if (this.playerField.textboxKeyTyped(letter, par2)) {
			this.sendKeyPacket(this.playerField);
			this.refreshPlayerList();
		}
		else
			super.keyTyped(letter, par2);
	}
	
	private void refreshPlayerList() {
		String[] playerNames = this.getArrayOfPlayerNames();
		this.validNames = this.getValidNames(this.playerField.getText(), playerNames);
		this.activeNames = ((TileEntityPlayerChecker) tileEnt).activePlayerNames;
	}
	
	private String[] getArrayOfPlayerNames() {
		Map<String, EntityPlayerMP> players = ((TileEntityPlayerChecker) tileEnt).onlinePlayers;
		String[] s = new String[players.keySet().toArray().length];
		int l = players.keySet().toArray().length;
		for (int i = 0; i < l; i++) {
			s[i] = players.keySet().toArray()[i] + " ";
		}
		return s;
	}
	
	private String[] getValidNames(String s, String[] playerNames) {
		ArrayList<String> tempNames = new ArrayList<String>();
		for (String str : playerNames) {
			if (s.equals("") || str.startsWith(s)) {
				tempNames.add(str);
			}
		}
		
		String[] strAr = new String[tempNames.size()];
		for (int i = 0; i < tempNames.size(); i++) {
			strAr[i] = tempNames.get(i);
		}
		return strAr;
	}
	
	private void sendKeyPacket(GuiTextField txtField) {
		PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
		try {
			packetbuffer.writeStringToBuffer(txtField.getText());
			this.mc.getNetHandler().addToSendQueue(
					new C17PacketCustomPayload("MC|ItemName", packetbuffer));
		} catch (Exception exception) {
			LogManager.getLogger().error("Couldn\'t send command block info", exception);
		} finally {
			packetbuffer.release();
		}
	}
	
	protected void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
		this.playerField.mouseClicked(x, y, button);
		
		String playerName = this.getActualStringFromTextField(this.playerField.getText());
		for (String str : this.validNames) {
			str = this.getActualStringFromTextField(str);
			if (this.nameCoors.containsKey(str)) {
				int[] minMax = this.nameCoors.get(str);
				if (minMax != null) {
					int xMin = minMax[0];
					int yMin = minMax[1];
					int xMax = minMax[2];
					int yMax = minMax[3];
					if ((x >= xMin && x <= xMax) && (y >= yMin && y <= yMax)) {
						playerName = str;
						continue;
					}
				}
			}
			else
				Capo.log.info("nameCoors doesnt contain \"" + str + "\"");
		}
		if (playerName != this.playerField.getText()) {
			this.playerField.setText(playerName);
		}
		
	}
	
	public void updateScreen() {
		
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public void onGuiClosed() {
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	
	public void drawScreen(int par1, int par2, float par3) {
		this.drawGuiContainerBackgroundLayer(par3, par1, par2);
		this.drawGuiContainerForegroundLayer(par1, par2);
		
		super.drawScreen(par1, par2, par3);
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = ((TileEntityPlayerChecker) tileEnt).getInventoryName();
		this.fontRendererObj.drawString(s,
				(this.width / 2) - (this.fontRendererObj.getStringWidth(s) / 2), this.topOfGui
						+ (int) (0.035 * this.ySize), 4210752);
		
		// int gray = 4210752;
		this.nameCoors.clear();
		ArrayList<String> drawnNames = new ArrayList<String>();
		int x = this.playerFieldX + 2;
		int y = this.playerFieldBoxY + 2;
		if (!this.hideActiveNames) {
			for (int i = 0; i < this.activeNames.size(); i++) {
				String str = this.activeNames.get(i);
				// Capo.log.info("\"" + str + "\"");
				y += i * this.nameHeight;
				if (y + this.nameHeight < this.playerFieldBoxYMax) {
					this.fontRendererObj.drawString(str, x, y, 0x00C8FF);
					// Capo.log.info("1 Adding \"" + str + "\" to nameCoors");
					this.nameCoors.put(str, new int[] {
							x, y, x + this.fontRendererObj.getStringWidth(str), y + this.nameHeight
					});
					drawnNames.add(str);
				}
			}
		}
		for (int i = 0; i < this.validNames.length; i++) {
			String str = this.getActualStringFromTextField(this.validNames[i]);
			// Capo.log.info("\"" + str + "\"");
			y += (i + ((this.hideActiveNames || this.activeNames.isEmpty()) ? 0 : 1))
					* this.nameHeight;
			if (!drawnNames.contains(str) && y + this.nameHeight < this.playerFieldBoxYMax) {
				this.fontRendererObj.drawString(str, x, y, -1);
				// Capo.log.info("2 Adding \"" + str + "\" to nameCoors");
				this.nameCoors.put(str, new int[] {
						x, y, x + this.fontRendererObj.getStringWidth(str), y + this.nameHeight
				});
			}
		}
		drawnNames.clear();
	}
	
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(
				new ResourceLocation(CoreReference.MOD_ID, "textures/gui/blank.png"));
		drawTexturedModalRect(this.leftOfGui, this.topOfGui, 0, 0, this.xSize, this.ySize);
		
		this.mc.getTextureManager().bindTexture(
				new ResourceLocation(CoreReference.MOD_ID, "textures/gui/black.png"));
		drawTexturedModalRect(this.playerFieldX, this.playerFieldBoxY, 0, 0, this.playerFieldWidth,
				this.playerFieldBoxYMax);
		
		this.playerField.drawTextBox();
		
	}
	
	private String getActualStringFromTextField(String str) {
		if (str.length() <= 0 || !str.substring(str.length() - 1, str.length()).equals(" "))
			return str;
		else
			return str.substring(0, str.length() - 1);
	}
	
}
