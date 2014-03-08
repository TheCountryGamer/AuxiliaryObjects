package com.countrygamer.auxiliaryobjects.blocks.tiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;

import com.countrygamer.auxiliaryobjects.Capo;
import com.countrygamer.core.block.tiles.TileEntityBase;

public class TileEntityPlayerChecker extends TileEntityBase {
	
	public Map<String, EntityPlayerMP>	onlinePlayers		= new HashMap<String, EntityPlayerMP>();
	public ArrayList<String>			activePlayerNames	= new ArrayList<String>();
	
	public TileEntityPlayerChecker() {
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
		for (String playerName : this.onlinePlayers.keySet()) {
			boolean isOnline = this.activePlayerNames.contains(playerName);
			Capo.log.info(playerName + ":" + isOnline);
		}
	}
	
	public void addPlayerToActive(String playerName) {
		this.activePlayerNames.add(playerName);
	}
	
	public void removePlayerFromActive(String playerName) {
		this.activePlayerNames.remove(playerName);
	}
	
	public boolean isActivePlayerOnline() {
		for (String playerName : this.activePlayerNames) {
			boolean isOnline = this.onlinePlayers.containsKey(playerName);
			Capo.log.info(playerName + ":" + isOnline);
			if (isOnline) return true;
		}
		return false;
	}
	
}
