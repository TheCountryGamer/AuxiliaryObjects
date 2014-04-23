package com.countrygamer.capo.common.tileentity;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import com.countrygamer.core.Base.common.tileentity.TileEntityBase;

public class TileEntityExtractor extends TileEntityBase {
	
	public int delayInSeconds = 60;
	private int timer = -1;
	private int safetyYZone = 5;
	private int range = 3;
	private int[] lastMinedCoords = new int[] {
			-1, -1, -1
	};
	private int maxY = -1;
	
	public void setTimer(int time) {
		this.timer = time;
	}
	
	@Override
	public void updateEntity() {
		if (timer == -1) {
			this.timer = this.getRandomTimer();
			return;
		}
		
		if (this.maxY == -1) this.maxY = this.yCoord - safetyYZone;
		
		int minX = this.xCoord - this.range;
		int maxX = this.xCoord + this.range + 1;
		int minZ = this.zCoord - this.range;
		int maxZ = this.zCoord + this.range + 1;
		
		if (this.lastMinedCoords[0] == -1) this.lastMinedCoords[0] = minX;
		if (this.lastMinedCoords[1] == -1) this.lastMinedCoords[1] = 0;
		if (this.lastMinedCoords[2] == -1) this.lastMinedCoords[2] = minZ;
		
		if (this.isPowered()) {
			// if (!this.getWorldObj().isRemote) Capo.log.info("Powered");
			if (this.timer <= 0) {
				ArrayList<int[]> oreCoordsToPull = this.oreCoordsToPull(this.xCoord, this.zCoord,
						this.range, this.yCoord - this.safetyYZone - 1);
				if (!oreCoordsToPull.isEmpty()) {
					int[] oreCoords = oreCoordsToPull.get(0);
					
					Block oreBlock = this.getWorldObj().getBlock(oreCoords[0], oreCoords[1],
							oreCoords[2]);
					int oreMeta = this.getWorldObj().getBlockMetadata(oreCoords[0], oreCoords[1],
							oreCoords[2]);
					
					int[] newCoords = null;
					int loopCount = 0;
					do {
						newCoords = this.getNewOreCoords(minX, maxX, this.maxY, this.yCoord, minZ,
								maxZ);
						loopCount += 1;
					} while (newCoords == null && loopCount <= safetyYZone * range * range);
					loopCount = 0;
					
					Block topBlock = this.getWorldObj().getBlock(newCoords[0], newCoords[1],
							newCoords[2]);
					int topMeta = this.getWorldObj().getBlockMetadata(newCoords[0], newCoords[1],
							newCoords[2]);
					
					if (!this.getWorldObj().isRemote) {
						this.getWorldObj().setBlock(oreCoords[0], oreCoords[1], oreCoords[2],
								topBlock, topMeta, 3);
						this.getWorldObj().setBlock(newCoords[0], newCoords[1], newCoords[2],
								oreBlock, oreMeta, 3);
					}
					
					this.timer = this.getRandomTimer();
				}
				else {
					// if (!this.getWorldObj().isRemote) Capo.log.info("No More Ores");
				}
			}
			else {
				--this.timer;
				// if (!this.getWorldObj().isRemote) Capo.log.info(this.timer + "");
			}
		}
	}
	
	public ArrayList<int[]> oreCoordsToPull(int baseX, int baseZ, int range, int maxY) {
		ArrayList<int[]> oreCoords = new ArrayList<int[]>();
		
		for (int y = 0; y <= maxY; y++) {
			for (int x = baseX - range; x <= baseX + range; x++) {
				for (int z = baseZ - range; z <= baseZ + range; z++) {
					Block block = this.getWorldObj().getBlock(x, y, z);
					int meta = this.getWorldObj().getBlockMetadata(x, y, z);
					if (this.isOre(block, meta)) {
						oreCoords.add(new int[] {
								x, y, z
						});
					}
				}
			}
		}
		
		return oreCoords;
	}
	
	public int getRandomTimer() {
		return ((new Random()).nextInt(this.delayInSeconds) + 1) * 20;
	}
	
	private boolean isOre(Block block, int meta) {
		String[] oreNames = new String[] {
				"Coal", "Iron", "Gold", "Lapis", "Redstone", "Diamond", "Emerald", "Copper",
				"Silver", "Tin", "Bronze"
		};
		
		ArrayList<ItemStack> validOres = new ArrayList<ItemStack>();
		for (String oreName : oreNames) {
			validOres.addAll(OreDictionary.getOres("ore" + oreName));
		}
		
		ItemStack oreStack = new ItemStack(block, 1, meta);
		for (ItemStack validOreStack : validOres) {
			if (validOreStack.getItem() == oreStack.getItem()
					&& validOreStack.getItemDamage() == oreStack.getItemDamage()) {
				return true;
			}
		}
		
		return false;
	}
	
	private int[] getNewOreCoords(int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
		int x = (new Random()).nextInt(maxX - minX) + minX;
		int y = (new Random()).nextInt(maxY - minY) + minY;
		int z = (new Random()).nextInt(maxZ - minZ) + minZ;
		boolean sameCoords = x == this.xCoord && y == this.yCoord && z == this.zCoord;
		
		if (!sameCoords) {
			Block block = this.getWorldObj().getBlock(x, y, z);
			int meta = this.getWorldObj().getBlockMetadata(x, y, z);
			if (block == Blocks.air || !this.isOre(block, meta)) {
				return new int[] {
						x, y, z
				};
			}
		}
		
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setInteger("extractor_timer", this.timer);
		tagCom.setInteger("extractor_range", this.range);
		tagCom.setIntArray("extractor_lastmined", this.lastMinedCoords);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.timer = tagCom.getInteger("extractor_timer");
		this.range = tagCom.getInteger("extractor_range");
		this.lastMinedCoords = tagCom.getIntArray("extractor_lastmined");
		
	}
	
}
