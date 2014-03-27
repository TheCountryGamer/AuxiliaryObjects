package com.countrygamer.capo.blocks.tiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.core.Base.block.tiles.TileEntityBase;

public class TileEntityPlayerChecker extends TileEntityBase {
	
	public Map<String, EntityPlayerMP> onlinePlayers = new HashMap<String, EntityPlayerMP>();
	public ArrayList<String> activePlayerNames = new ArrayList<String>();
	
	public TileEntityPlayerChecker() {
		super();
		this.refreshPlayers();
		
	}
	
	/**
	 * Used to refresh onlinePlayers HashMap with all active 'usernames' and
	 * their respective EntityPlayerMP objects
	 */
	public void refreshPlayers() {
		this.onlinePlayers.clear();
		for (Object thing : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			if (thing instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) thing;
				this.onlinePlayers.put(player.getGameProfile().getName(), player);
			}
		}
	}
	
	public String getInventoryName() {
		return "Player Checker";
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		NBTTagList activeNames = tagCom.getTagList("activeNames", 10);
		this.activePlayerNames.clear();
		for (int i = 0; i < activeNames.tagCount(); i++) {
			NBTTagCompound tagCom1 = activeNames.getCompoundTagAt(i);
			String str = tagCom1.getString("name");
			this.activePlayerNames.add(str);
		}
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		NBTTagList activeNames = new NBTTagList();
		for (int i = 0; i < this.activePlayerNames.size(); i++) {
			String str = this.activePlayerNames.get(i);
			NBTTagCompound tagCom1 = new NBTTagCompound();
			tagCom1.setString("name", str);
			activeNames.appendTag(tagCom1);
		}
		tagCom.setTag("activeNames", activeNames);
	}
	
	public void updateEntity() {
		this.refreshPlayers();
		// for (String playerName : this.onlinePlayers.keySet()) {
		// boolean isOnline = this.activePlayerNames.contains(playerName);
		// Capo.log.info(playerName + ":" + isOnline);
		// }
		
	}
	
	public void addPlayerToActive(String playerName) {
		if (!this.activePlayerNames.contains(playerName)) {
			this.activePlayerNames.add(playerName);
			this.refreshPlayers();
			this.changedPower();
		}
	}
	
	public void removePlayerFromActive(String playerName) {
		if (this.activePlayerNames.contains(playerName)) {
			this.activePlayerNames.remove(playerName);
			this.refreshPlayers();
			this.changedPower();
		}
	}
	
	private void changedPower() {
		World world = this.getWorldObj();
		int x = this.xCoord, y = this.yCoord, z = this.zCoord;
		Block thisBlock = world.getBlock(x, y, z);
		
		world.notifyBlockOfNeighborChange(x - 1, y + 0, z + 0, thisBlock);
		world.notifyBlockOfNeighborChange(x + 1, y + 0, z + 0, thisBlock);
		world.notifyBlockOfNeighborChange(x + 0, y - 1, z + 0, thisBlock);
		world.notifyBlockOfNeighborChange(x + 0, y + 1, z + 0, thisBlock);
		world.notifyBlockOfNeighborChange(x + 0, y + 0, z - 1, thisBlock);
		world.notifyBlockOfNeighborChange(x + 0, y + 0, z + 1, thisBlock);
	}
	
	public boolean isActivePlayerOnline() {
		boolean hasActiveOnline = false;
		this.refreshPlayers();
		
		// LogBlock log = new LogBlock(Capo.log, "\n");
		for (String playerName : this.onlinePlayers.keySet()) {
			// log.addWithLine(playerName + " is online");
			for (String activeName : this.activePlayerNames) {
				// log.addWithLine(activeName + " is active");
				if (activeName.equals(playerName)) {
					hasActiveOnline = true;
					// log.addWithLine(playerName + " is on both lists");
				}
			}
		}
		// if (!this.getWorldObj().isRemote) log.log();
		return hasActiveOnline;
	}
	
	public int getRedstonePower() {
		if (this.isActivePlayerOnline()) {
			Capo.log.info("Powered");
			return 15;
		}
		Capo.log.info("Not powered");
		return 0;
	}
	
}
