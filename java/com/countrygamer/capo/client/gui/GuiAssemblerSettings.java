package com.countrygamer.capo.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.tileentity.TileEntityAssembler;
import com.countrygamer.core.Base.client.gui.GuiContainerBlockBase;
import com.countrygamer.core.Base.common.inventory.ContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

public class GuiAssemblerSettings extends GuiContainerBlockBase {
	
	public GuiAssemblerSettings(EntityPlayer player, ContainerBlockBase container) {
		super(player, container);
		String moduleName = "Module";
		TileEntityInventoryBase tileEnt = container.tileEnt;
		if (tileEnt != null && tileEnt instanceof TileEntityAssembler) {
			ItemStack moduleStack = ((TileEntityAssembler) tileEnt).getStackInSlot(0);
			if (moduleStack != null)
				moduleName = moduleStack.getDisplayName();
		}
		this.setupGui(moduleName + " Settings", new ResourceLocation(Reference.MOD_ID,
				"textures/gui/Assembler Settings.png"));
	}
	
	protected void foregroundText() {
		// Draw "Offest X"
		
		// Draw "Offest Y"
		
		// Draw "Offset Z"
		
		// Draw "Bounds:"
		
		// Draw "North"
		
		// Draw "South"
		
		// Draw "East"
		
		// Draw "West"
		
		// Draw "Top"
		
		// Draw "Bottom"
		
		
	}
	
	protected void backgroundObjects() {
		
	}
	
}
