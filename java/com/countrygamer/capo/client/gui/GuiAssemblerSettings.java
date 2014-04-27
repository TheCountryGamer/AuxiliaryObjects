package com.countrygamer.capo.client.gui;

import java.util.HashMap;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.inventory.container.ContainerAssemblerSettings;
import com.countrygamer.capo.common.item.ItemModuleWall;
import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.packet.PacketSaveModuleSettings;
import com.countrygamer.capo.common.tileentity.TileEntityAssembler;
import com.countrygamer.core.Base.client.gui.GuiContainerBlockBase;
import com.countrygamer.core.Base.common.inventory.ContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

public class GuiAssemblerSettings extends GuiContainerBlockBase {
	
	private HashMap<Integer, String> buttonIDs = new HashMap<Integer, String>();
	private GuiTextField offsetX, offsetY, offsetZ;
	private GuiTextField boundN, boundS, boundE, boundW, boundT, boundB;
	private GuiButton saveBox;
	
	public GuiAssemblerSettings(EntityPlayer player, ContainerBlockBase container) {
		super(player, container);
		this.xSize = 256;
		this.ySize = 200;
		
		String moduleName = "Module";
		TileEntityInventoryBase tileEnt = container.tileEnt;
		if (tileEnt != null && tileEnt instanceof TileEntityAssembler) {
			ItemStack moduleStack = ((TileEntityAssembler) tileEnt).getStackInSlot(0);
			if (moduleStack != null) moduleName = moduleStack.getDisplayName();
		}
		this.setupGui(moduleName + " Settings", new ResourceLocation(Reference.MOD_ID,
				"textures/gui/Assembler Settings.png"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		int bID = -1;
		this.addIncrementButton(bID++, this.guiLeft + 06, (this.guiTop) + 24, "-", "offsetX");
		this.addIncrementButton(bID++, this.guiLeft + 74, (this.guiTop) + 24, "+", "offsetX");
		this.addIncrementButton(bID++, this.guiLeft + 89, (this.guiTop) + 24, "-", "offsetY");
		this.addIncrementButton(bID++, this.guiLeft + 157, (this.guiTop) + 24, "+", "offsetY");
		this.addIncrementButton(bID++, this.guiLeft + 172, (this.guiTop) + 24, "-", "offsetZ");
		this.addIncrementButton(bID++, this.guiLeft + 240, (this.guiTop) + 24, "+", "offsetZ");
		
		this.addIncrementButton(bID++, this.guiLeft + 06, (this.guiTop) + 56, "-", "boundN");
		this.addIncrementButton(bID++, this.guiLeft + 74, (this.guiTop) + 56, "+", "boundN");
		this.addIncrementButton(bID++, this.guiLeft + 89, (this.guiTop) + 56, "-", "boundE");
		this.addIncrementButton(bID++, this.guiLeft + 157, (this.guiTop) + 56, "+", "boundE");
		this.addIncrementButton(bID++, this.guiLeft + 172, (this.guiTop) + 56, "-", "boundT");
		this.addIncrementButton(bID++, this.guiLeft + 240, (this.guiTop) + 56, "+", "boundT");
		
		this.addIncrementButton(bID++, this.guiLeft + 06, (this.guiTop) + 88, "-", "boundS");
		this.addIncrementButton(bID++, this.guiLeft + 74, (this.guiTop) + 88, "+", "boundS");
		this.addIncrementButton(bID++, this.guiLeft + 89, (this.guiTop) + 88, "-", "boundW");
		this.addIncrementButton(bID++, this.guiLeft + 157, (this.guiTop) + 88, "+", "boundW");
		this.addIncrementButton(bID++, this.guiLeft + 172, (this.guiTop) + 88, "-", "boundB");
		this.addIncrementButton(bID++, this.guiLeft + 240, (this.guiTop) + 88, "+", "boundB");
		
		this.buttonList.add(this.saveBox = new GuiButton(bID++, this.guiLeft + 5,
				this.guiTop + 140, 80, 20, "Save Settings"));
		
		ItemStack moduleStack = this.tileEnt.getStackInSlot(0);
		ItemModuleWall module = (ItemModuleWall) moduleStack.getItem();
		// text fields
		FontRenderer fr = this.fontRendererObj;
		this.offsetX = new GuiTextField(fr, this.guiLeft + 21, this.guiTop + 24, 48, 20);
		this.setupTextField(this.offsetX, 3);
		this.offsetX.setText(module.getOffset(moduleStack, 'X') + "");
		this.offsetY = new GuiTextField(fr, this.guiLeft + 104, this.guiTop + 24, 48, 20);
		this.setupTextField(this.offsetY, 3);
		this.offsetY.setText(module.getOffset(moduleStack, 'Y') + "");
		this.offsetZ = new GuiTextField(fr, this.guiLeft + 187, this.guiTop + 24, 48, 20);
		this.setupTextField(this.offsetZ, 3);
		this.offsetZ.setText(module.getOffset(moduleStack, 'Z') + "");
		//
		this.boundN = new GuiTextField(fr, this.guiLeft + 21, this.guiTop + 56, 48, 20);
		this.setupTextField(this.boundN, 3);
		this.boundN.setText(module.getBound(moduleStack, true, 'Z') + "");
		this.boundS = new GuiTextField(fr, this.guiLeft + 21, this.guiTop + 88, 48, 20);
		this.setupTextField(this.boundS, 3);
		this.boundS.setText(module.getBound(moduleStack, false, 'Z') + "");
		
		this.boundE = new GuiTextField(fr, this.guiLeft + 104, this.guiTop + 56, 48, 20);
		this.setupTextField(this.boundE, 3);
		this.boundE.setText(module.getBound(moduleStack, false, 'X') + "");
		this.boundW = new GuiTextField(fr, this.guiLeft + 104, this.guiTop + 88, 48, 20);
		this.setupTextField(this.boundW, 3);
		this.boundW.setText(module.getBound(moduleStack, true, 'X') + "");
		
		this.boundT = new GuiTextField(fr, this.guiLeft + 187, this.guiTop + 56, 48, 20);
		this.setupTextField(this.boundT, 3);
		this.boundT.setText(module.getBound(moduleStack, false, 'Y') + "");
		this.boundB = new GuiTextField(fr, this.guiLeft + 187, this.guiTop + 88, 48, 20);
		this.setupTextField(this.boundB, 3);
		this.boundB.setText(module.getBound(moduleStack, true, 'Y') + "");
		
	}
	
	@SuppressWarnings("unchecked")
	private void addIncrementButton(int id, int x, int y, String text, String key) {
		this.buttonList.add(new GuiButton(id, x, y, 10, 20, text));
		this.buttonIDs.put(id, key + text);
	}
	
	@Override
	protected void buttonPress(int id) {
		if (id == this.saveBox.id) {
			ItemStack moduleStack = this.tileEnt.getStackInSlot(0);
			if (moduleStack != null && moduleStack.getItem() instanceof ItemModuleWall) {				
				ContainerAssemblerSettings container = (ContainerAssemblerSettings) this.inventorySlots;
				ItemStack camoStack = container.getSlotFromInventory(container.fi, 0).getStack();
				
				PacketSaveModuleSettings packet = new PacketSaveModuleSettings(this.tileEnt.xCoord,
						this.tileEnt.yCoord, this.tileEnt.zCoord, this.getOffsets(),
						this.getBounds(), camoStack);
				Capo.packetChannel.sendToServer(packet);
				Capo.packetChannel.sendToAll(packet);
				
			}
		}
		else {
			String key = this.buttonIDs.get(id);
			String fieldName = key.substring(0, key.length() - 1);
			String operator = key.substring(key.length() - 1);
			GuiTextField field = this.getFieldFromKey(fieldName);
			if (field != null) {
				String text = field.getText();
				int value = text != "" ? Integer.parseInt(text) : 0;
				if (operator.equals("+"))
					value += 1;
				else if (operator.equals("-"))
					value -= 1;
				field.setText(value + "");
			}
		}
	}
	
	private int[] getOffsets() {
		return new int[] {
				Integer.parseInt(this.offsetX.getText()), Integer.parseInt(this.offsetY.getText()),
				Integer.parseInt(this.offsetZ.getText())
		};
	}
	
	private int[] getBounds() {
		return new int[] {
				Integer.parseInt(this.boundN.getText()), Integer.parseInt(this.boundS.getText()),
				Integer.parseInt(this.boundE.getText()), Integer.parseInt(this.boundW.getText()),
				Integer.parseInt(this.boundT.getText()), Integer.parseInt(this.boundB.getText())
		};
	}
	
	private GuiTextField getFieldFromKey(String name) {
		if (name.equals("offsetX"))
			return this.offsetX;
		if (name.equals("offsetY"))
			return this.offsetY;
		if (name.equals("offsetZ"))
			return this.offsetZ;
		if (name.equals("boundN"))
			return this.boundN;
		if (name.equals("boundS"))
			return this.boundS;
		if (name.equals("boundE"))
			return this.boundE;
		if (name.equals("boundW"))
			return this.boundW;
		if (name.equals("boundT"))
			return this.boundT;
		if (name.equals("boundB"))
			return this.boundB;
		return null;
	}
	
	protected void foregroundText() {
		// drawRect(0, 10, this.xSize, 11, this.grayTextColor);
		// Draw "Offset X"
		this.string("Offset X", 25, 15);
		// Draw "Offset Y"
		this.string("Offset Y", 110, 15);
		// Draw "Offset Z"
		this.string("Offset Z", 190, 15);
		// Draw "Bounds:"
		// this.string("Bounds:", 0, 0);
		// Draw "North"
		this.string("North", 30, 47);
		// Draw "South"
		this.string("South", 30, 79);
		// Draw "East"
		this.string("East", 115, 47);
		// Draw "West"
		this.string("West", 115, 79);
		// Draw "Top"
		this.string("Top", 200, 47);
		// Draw "Bottom"
		this.string("Bottom", 195, 79);
		
	}
	
	protected void backgroundObjects() {
		
	}
	
}
