package com.countrygamer.capo.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.capo.blocks.tiles.TileEntityAssembler;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.Base.client.gui.GuiContainerBlockBase;
import com.countrygamer.core.Base.inventory.ContainerBlockBase;

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
