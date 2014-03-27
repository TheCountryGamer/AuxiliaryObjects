package com.countrygamer.capo.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;

import com.countrygamer.capo.blocks.tiles.TileEntityIncubator;
import com.countrygamer.capo.inventory.ContainerIncubator;
import com.countrygamer.capo.lib.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIncubator extends GuiContainer {

	private TileEntityIncubator inventory;

	public GuiIncubator(InventoryPlayer invPlayer, TileEntityIncubator tileEnt) {
		super(new ContainerIncubator(invPlayer, tileEnt));
		this.inventory = tileEnt;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = this.inventory.getInventoryName();
		this.fontRendererObj.drawString(s, this.xSize / 2
				- this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString("container.inventory", 8,
				this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(Reference.incubatorBackground);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		int i1;

		i1 = this.inventory.getCookProgressScaled(24);
		this.drawTexturedModalRect(k + 62, l + 35, 176, 14, i1 + 1, 16);
		this.drawTexturedModalRect(k + 104, l + 35, 176, 14, i1 + 1, 16);

	}

}
