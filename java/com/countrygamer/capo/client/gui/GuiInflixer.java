package com.countrygamer.capo.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.inventory.container.ContainerInflixer;
import com.countrygamer.capo.common.item.ItemMultiItem;
import com.countrygamer.capo.common.lib.EnumPartition;
import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.packet.PacketTriggerInflixer;
import com.countrygamer.capo.common.tileentity.TileEntityInflixer;
import com.countrygamer.core.Base.client.gui.GuiButtonArrow;
import com.countrygamer.core.Base.client.gui.GuiContainerBlockBase;

public class GuiInflixer extends GuiContainerBlockBase {
	
	private TileEntityInflixer	tileEntInflix;
	
	private GuiButtonArrow		partitionUp, partitionDown;
	private GuiButton			inflix, partition;
	
	private int					currentPartitionID	= 0;
	
	public GuiInflixer(EntityPlayer player, TileEntityInflixer tileEnt) {
		super(player, new ContainerInflixer(player.inventory, tileEnt));
		this.tileEntInflix = tileEnt;
		this.setupGui(tileEnt.getInventoryName(), new ResourceLocation(Reference.MOD_ID,
				"textures/gui/inflixer.png"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		int buttonID = 0;
		this.buttonList.clear();
		
		this.buttonList.add(this.partitionUp = new GuiButtonArrow(buttonID++, this.guiLeft + 8,
				this.guiTop + 36, GuiButtonArrow.ButtonType.UP));
		this.buttonList.add(this.partitionDown = new GuiButtonArrow(buttonID++, this.guiLeft + 8,
				this.guiTop + 70, GuiButtonArrow.ButtonType.DOWN));
		
		this.buttonList.add(this.inflix = new GuiButton(buttonID++,
				this.guiLeft + (this.xSize / 4), this.guiTop + 15, 50, 20, "Inflix"));
		this.buttonList.add(this.partition = new GuiButton(buttonID++,
				this.guiLeft + (this.xSize / 4), this.guiTop + (this.ySize / 3), 50, 20,
				"Partition"));
		
	}
	
	@Override
	protected void buttonPress(int id) {
		if (id == this.partitionUp.id) {
			int newID = EnumPartition.getEnumForID(this.currentPartitionID + 1).getID();
			this.currentPartitionID = newID;
		}
		if (id == this.partitionDown.id) {
			int newID = EnumPartition.getEnumForID(this.currentPartitionID - 1).getID();
			this.currentPartitionID = newID;
		}
		if (id == this.inflix.id) {
			PacketTriggerInflixer packet = new PacketTriggerInflixer(this.tileEntInflix.xCoord,
					this.tileEntInflix.yCoord, this.tileEntInflix.zCoord);
			Capo.packetChannel.sendToServer(packet);
		}
		if (id == this.partition.id) {
			PacketTriggerInflixer packet = new PacketTriggerInflixer(this.tileEntInflix.xCoord,
					this.tileEntInflix.yCoord, this.tileEntInflix.zCoord, 1, this.currentPartitionID);
			Capo.packetChannel.sendToServer(packet);
		}
	}
	
	@Override
	protected void foregroundText() {
		ItemStack multiStack = this.tileEnt.getStackInSlot(1);
		if (multiStack != null) {
			//ItemMultiItem multiItem = (ItemMultiItem) multiStack.getItem();
			
			int numberOfItems = ItemMultiItem.getCurrentNumberOfItems(multiStack);
			int slotsAvailible = ItemMultiItem.maxItemNum - numberOfItems;
			//
			String str = "Slots: " + slotsAvailible;
			int x = this.xSize - this.fontRendererObj.getStringWidth(str) - 10;
			int y = 20;
			this.string(str, x, y);
			//
			x -= 10;
			for (int i = 1; i <= ItemMultiItem.maxItemNum; i++) {
				ItemStack itemStack = ItemMultiItem.getStackInSlot(multiStack, i);
				str = "- ";
				if (itemStack != null) {
					str += itemStack.getDisplayName();
				}
				else {
					str += "------";
				}
				this.string(str, x, y + (i * 10));
			}
			
		}
		
	}
	
	protected void backgroundObjects() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.currentPartitionID != 0) {
			this.mc.getTextureManager().bindTexture(
					new ResourceLocation(Reference.MOD_ID, "textures/gui/Partition.png"));
			this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 50,
					(this.currentPartitionID - 1) * 16, 0, 16, 16);
		}
	}
	
}
