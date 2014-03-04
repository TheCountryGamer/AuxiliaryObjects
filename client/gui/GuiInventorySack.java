package com.countrygamer.auxiliaryobjects.client.gui;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.auxiliaryobjects.AuxiliaryObjects;
import com.countrygamer.auxiliaryobjects.inventory.ContainerInventorySack;
import com.countrygamer.auxiliaryobjects.items.ItemInventorySack;
import com.countrygamer.auxiliaryobjects.lib.Reference;
import com.countrygamer.auxiliaryobjects.packet.PacketSackName;
import com.countrygamer.core.client.gui.GuiContainerItemBase;
import com.countrygamer.core.inventory.InventoryItemBase;

public class GuiInventorySack extends GuiContainerItemBase {
	
	private GuiTextField	nameTextField;
	
	public GuiInventorySack(EntityPlayer player, InventoryPlayer invPlayer,
			InventoryItemBase inventory) {
		super(new ContainerInventorySack(player, invPlayer, inventory), 169 + 7, 176 + 7);
		NBTTagCompound tagCom = new NBTTagCompound();
		if (player.getHeldItem().hasTagCompound()) tagCom = player.getHeldItem().getTagCompound();
		String name = tagCom.getString(ItemInventorySack.sackName);
		this.title = name != "" ? name : this.inventory.getInventoryName();
		this.bkgdTex = new ResourceLocation(Reference.MOD_ID, "textures/gui/inventory.png");
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int w = 80;
		this.setupTextField(this.nameTextField = new GuiTextField(this.fontRendererObj,
				this.guiLeft + this.xSize - w - 8, this.guiTop + 4, w, 10), 13);
	}
	
	@Override
	protected void renderTitle() {
		this.fontRendererObj.drawString(this.title, 7, 5, this.grayTextColor);
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		if (!this.nameTextField.getText().equals("")) {
			PacketSackName packet = new PacketSackName(this.nameTextField.getText());
			AuxiliaryObjects.packetChannel.sendToServer(packet);
		}
	}
	
}
