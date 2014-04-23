package com.countrygamer.capo.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.inventory.container.ContainerAssembler;
import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.packet.PacketOpenAssemblerSettings;
import com.countrygamer.capo.common.packet.PacketTriggerAssembler;
import com.countrygamer.core.Base.client.gui.GuiContainerBlockBase;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAssembler extends GuiContainerBlockBase {
	
	private GuiButton openSettings;
	
	public GuiAssembler(EntityPlayer player, TileEntityInventoryBase tileEnt) {
		super(player, new ContainerAssembler(player.inventory, tileEnt));
		this.setupGui("Module Assembler", new ResourceLocation(Reference.MOD_ID,
				"textures/gui/Assembler.png"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		
		int x = ((this.width + this.xSize) / 2) - (this.xSize / 2) - 75;
		this.buttonList.add(this.openSettings = new GuiButton(0, x, 80, 75, 20, "Open Settings"));
		
	}
	
	@Override
	protected void buttonPress(int id) {
		if (id == this.openSettings.id) {
			this.saveUpgrades();
			// TODO open settings gui
			Capo.log.info("Sending Packet");
			PacketOpenAssemblerSettings packet = new PacketOpenAssemblerSettings(
					this.tileEnt.xCoord, this.tileEnt.yCoord, this.tileEnt.zCoord, false);
			Capo.packetChannel.sendToServer(packet);
			Capo.packetChannel.sendToAll(packet);
		}
	}
	
	@Override
	public void onGuiClosed() {
		this.saveUpgrades();
	}
	
	private void saveUpgrades() {
		PacketTriggerAssembler packet = new PacketTriggerAssembler(this.tileEnt.xCoord,
				this.tileEnt.yCoord, this.tileEnt.zCoord, "saveUpgrades");
		Capo.packetChannel.sendToServer(packet);
		Capo.packetChannel.sendToAll(packet);
	}
	
}
