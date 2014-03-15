package com.countrygamer.capo.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.inventory.ContainerInventorySack;
import com.countrygamer.capo.items.ItemInventorySack;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.capo.packet.PacketSackName;
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
			Capo.packetChannel.sendToServer(packet);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(this.bkgdTex);
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		this.nameTextField.drawTextBox();
		
		this.backgroundObjects();
	}
	
}
