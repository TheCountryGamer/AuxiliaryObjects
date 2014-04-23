package com.countrygamer.capo.client.gui;

import net.minecraft.entity.player.EntityPlayer;

import com.countrygamer.capo.common.inventory.container.ContainerModuleBase;
import com.countrygamer.core.Base.client.gui.GuiContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiModuleBase extends GuiContainerBlockBase {

	public GuiModuleBase(EntityPlayer player, TileEntityInventoryBase tileEnt) {
		super(player, new ContainerModuleBase(player.inventory, tileEnt));
	}
	
}
