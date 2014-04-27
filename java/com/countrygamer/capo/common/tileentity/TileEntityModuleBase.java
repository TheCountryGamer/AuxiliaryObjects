package com.countrygamer.capo.common.tileentity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumSkyBlock;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.item.ItemModuleWall;
import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

public class TileEntityModuleBase extends TileEntityInventoryBase {
	
	final double PI = 3.14159265;
	
	public final Item sentry = Items.stick;
	public final Item forceField = Capo.moduleWall;
	
	public EntityLivingBase currentTarget = null;
	public int quad = 0;
	public float horizontalAngle = 0.75F;
	private int maxHurtDelay = 20 * 4, maxWallDelay = 20 * 5, delay, wallDelay;
	private boolean updateWall = false;
	private boolean changeWall = false;
	public boolean hasWall = false;
	
	private int[] offsets = new int[3];
	private int[] bounds = new int[6];
	
	private LinkedHashMap<int[], Integer> blacklistBlocks = new LinkedHashMap<int[], Integer>();
	
	public TileEntityModuleBase() {
		super("Module Base", 2, 1);
		this.delay = 20 * 5;
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		// Capo.log.info("Dirty Slot!");
		ItemStack moduleStack = this.getStackInSlot(0);
		if (moduleStack != null && moduleStack.getItem() == this.forceField) {
			this.updateWall = true;
		}
		
	}
	
	@Override
	public void updateEntity() {
		ItemStack moduleStack = this.getStackInSlot(0);
		if (this.hasWall) {
			if (!this.isPowered() || moduleStack == null
					|| !(moduleStack.getItem() instanceof ItemModuleWall)) {
				//Capo.log.info("Needs to empty field");
				this.emptyField();
			}
		}
		if (moduleStack != null) {
			if (moduleStack.getItem() instanceof ItemModuleWall) {
				this.updateWall();
			}
			else {
				this.wallDelay = this.maxWallDelay;
				if (moduleStack.getItem() == sentry) {
					this.updateLaser();
				}
			}
		}
		
	}
	
	private void updateLaser() {
		if (this.currentTarget == null)
			this.currentTarget = this.getNearestEntity();
		else {
			// Capo.log.info("Calculating entity things");
			double tX = this.xCoord - Math.floor(this.currentTarget.posX);
			// double tY = this.yCoord - Math.floor(this.currentTarget.posY);
			double tZ = this.zCoord - Math.floor(this.currentTarget.posZ);
			
			if (tX == 0 && tZ == 0)
				this.quad = -1;
			else if (tX > 0 && tZ <= 0) {
				this.quad = 1;
			}
			else if (tX <= 0 && tZ < 0) {
				this.quad = 2;
			}
			else if (tX < 0 && tZ >= 0) {
				this.quad = 3;
			}
			else if (tX >= 0 && tZ > 0) {
				this.quad = 4;
			}
			
			this.horizontalAngle = (float) Math.floor( // floor it because PI is not exact
					Math.atan( // used as inverse tanget
					Math.abs(tZ) / Math.abs(tX) // get only a positive value
					) * 180 / PI // convert to degrees
					);
			
			if (this.delay <= 0) {
				this.currentTarget.attackEntityFrom(DamageSource.generic, 1.0F);
				Capo.log.info("Hurt Nearby Entity");
				this.delay = this.maxHurtDelay;
			}
			else {
				this.delay -= 1;
			}
			
			if (this.currentTarget.isDead || !this.isEntityInRange(this.currentTarget)) {
				this.currentTarget = null;
				this.delay = this.maxHurtDelay;
			}
			
		}
	}
	
	@SuppressWarnings({
			"rawtypes", "unchecked"
	})
	private EntityLivingBase getNearestEntity() {
		List ents = this.getWorldObj().getEntitiesWithinAABB(
				EntityLivingBase.class,
				AxisAlignedBB.getBoundingBox(this.xCoord - 10, this.yCoord - 10, this.zCoord - 10,
						this.xCoord + 10, this.yCoord + 10, this.zCoord + 10));
		if (ents.isEmpty()) {
			// Capo.log.info("No Entities");
			return null;
		}
		return this.getValidEnt(ents);
	}
	
	private EntityLivingBase getValidEnt(List<EntityLivingBase> ents) {
		for (int i = 0; i < ents.size(); i++) {
			if (this.isValidEnt(ents.get(i))) return ents.get(i);
		}
		return null;
	}
	
	private boolean isValidEnt(EntityLivingBase ent) {
		if (ent instanceof EntityPlayer) return false;
		return true;
	}
	
	public int getCurrentDelayCount() {
		return this.delay;
	}
	
	public float[] getCoordsForLaserFireFromQuad(int quad) {
		float y = 11.0F;
		float h = 2.0F, l = -2.0F;
		float x, z;
		switch (quad) {
			case 1:
				x = h;
				z = h;
				break;
			case 2:
				x = l;
				z = h;
				break;
			case 3:
				x = l;
				z = l;
				break;
			case 4:
				x = h;
				;
				z = l;
				break;
			default:
				x = 0.0F;
				z = 0.0F;
				break;
		}
		
		return new float[] {
				x, y, z
		};
	}
	
	private boolean isEntityInRange(EntityLivingBase ent) {
		double tX = this.xCoord - Math.floor(this.currentTarget.posX);
		double tY = this.yCoord - Math.floor(this.currentTarget.posY);
		double tZ = this.zCoord - Math.floor(this.currentTarget.posZ);
		return tX <= 10 && tY <= 10 && tZ <= 10;
	}
	
	private void updateWall() {
		if (this.isPowered()) {
			if (!this.hasWall) {
				Capo.log.info("Powered Without Wall");
				this.fillField();
			}
			else {
				int blacklistSize = this.blacklistBlocks.size();
				Iterator<int[]> it = this.blacklistBlocks.keySet().iterator();
				while (it.hasNext()) {
					int[] coord = null;
					try {
						coord = it.next();
					} catch (ConcurrentModificationException er) {
						break;
					}
					
					int tickDelay = this.blacklistBlocks.get(coord);
					if (tickDelay <= 0) {
						//Capo.log.info("Removed Block");
						this.blacklistBlocks.remove(coord);
					}
					else {
						tickDelay -= 1;
						this.blacklistBlocks.put(coord, tickDelay);
					}
				}
				if (blacklistSize > this.blacklistBlocks.size()) {
					Capo.log.info("Update From Blacklist");
					this.fillField();
				}
				//
				if (this.hasWall || this.isPowered()) {
					if (this.wallDelay <= 0) {
						Capo.log.info("Revalidate");
						this.fillField();
						this.wallDelay = this.maxWallDelay;
					}
					else {
						this.wallDelay -= 1;
					}
				}
			}
		}
	}
	
	public void fillField() {
		int[][] fieldCoords = this.getFieldBounds(true);
		if (fieldCoords == null) return;
		
		ItemStack moduleStack = this.getStackInSlot(0);
		ItemStack camoStack = ((ItemModuleWall) moduleStack.getItem()).loadCamoStack(moduleStack);
		Block camoBlock = null;
		int metadata = 0;
		if (camoStack != null) {
			camoBlock = Block.getBlockFromItem(camoStack.getItem());
			metadata = camoStack.getItemDamage();
		}
		
		for (int[] coord : fieldCoords) {
			int x = coord[0];
			int y = coord[1];
			int z = coord[2];
			
			Block currentBlock = this.getWorldObj().getBlock(x, y, z);
			if (currentBlock == Blocks.air) {
				boolean containsKey = this.blacklistBlocks.containsKey(new int[] {
						x, y, z
				});
				for (int[] key : this.blacklistBlocks.keySet()) {
					if (key[0] == x && key[1] == y && key[2] == z) {
						containsKey = true;
						break;
					}
				}
				// Capo.log.info("Contains Key: " + containsKey);
				if (!containsKey) {
					this.getWorldObj().setBlock(x, y, z, Capo.wallBlock);
					TileEntity tileEnt = this.getWorldObj().getTileEntity(x, y, z);
					if (tileEnt != null && tileEnt instanceof TileEntityWall) {
						TileEntityWall wallEnt = (TileEntityWall) tileEnt;
						wallEnt.setBlockWithMeta(camoBlock, metadata);
						wallEnt.moduleBaseX = this.xCoord;
						wallEnt.moduleBaseY = this.yCoord;
						wallEnt.moduleBaseZ = this.zCoord;
						this.worldObj.updateLightByType(EnumSkyBlock.Block, x, y, z);
					}
					this.getWorldObj().scheduleBlockUpdate(x, y, z, Capo.wallBlock, 10);
				}
			}
		}
		
		this.hasWall = true;
	}
	
	public void emptyField() {
		int[][] fieldCoords = this.getFieldBounds(false);
		if (fieldCoords == null) return;
		
		for (int[] coord : fieldCoords) {
			int x = coord[0];
			int y = coord[1];
			int z = coord[2];
			
			Block currentBlock = this.getWorldObj().getBlock(x, y, z);
			if (currentBlock == Capo.wallBlock) {
				this.getWorldObj().setBlockToAir(x, y, z);
				this.getWorldObj().scheduleBlockUpdate(x, y, z, Blocks.air, 10);
			}
		}
		
		this.hasWall = false;
		this.blacklistBlocks.clear();
	}
	
	public void modifyField(Block newFieldBlock, Block camoBlock, int metadata) {
		int[][] coords = this.getFieldBounds(true);
		if (coords == null) return;
		for (int[] coord : coords) {
			int x1 = this.xCoord + coord[0];
			int y1 = this.yCoord + coord[1];
			int z1 = this.zCoord + coord[2];
			
			Block blockAtCoords = this.getWorldObj().getBlock(x1, y1, z1);
			boolean airFieldBlock = newFieldBlock == Blocks.air && blockAtCoords == Capo.wallBlock;
			boolean wallFieldBlock = newFieldBlock == Capo.wallBlock && blockAtCoords == Blocks.air;
			boolean containsCoord = false;
			if (!containsCoord && (airFieldBlock || wallFieldBlock)) {
				this.getWorldObj().setBlock(x1, y1, z1, newFieldBlock, 0, 3);
				
				if (newFieldBlock == Capo.wallBlock) {
					TileEntity tEnt = this.getWorldObj().getTileEntity(x1, y1, z1);
					if (tEnt != null && tEnt instanceof TileEntityWall) {
						TileEntityWall wallEnt = (TileEntityWall) tEnt;
						wallEnt.setBlockWithMeta(camoBlock, metadata);
						wallEnt.moduleBaseX = this.xCoord;
						wallEnt.moduleBaseY = this.yCoord;
						wallEnt.moduleBaseZ = this.zCoord;
						this.worldObj.updateLightByType(EnumSkyBlock.Block, x1, y1, z1);// .updateAllLightTypes(xCoord,
						// yCoord,
						// zCoord);
					}
				}
			}
			this.getWorldObj().scheduleBlockUpdate(x1, y1, z1, newFieldBlock, 10);
		}
		
	}
	
	private int[][] getFieldBounds(boolean retrieveModuleCoords) {
		if (retrieveModuleCoords) {
			this.offsets = this.getOffsetsFromModule();
			this.bounds = this.getBoundsFromModule();
		}
		
		if (this.bounds == null || this.offsets == null) {
			return null;
		}
		
		ArrayList<int[]> coordList = new ArrayList<int[]>();
		
		int minX = this.bounds[0] + this.offsets[0] + this.xCoord;
		int maxX = this.bounds[1] + this.offsets[0] + this.xCoord;
		int minY = this.bounds[2] + this.offsets[1] + this.yCoord;
		int maxY = this.bounds[3] + this.offsets[1] + this.yCoord;
		int minZ = this.bounds[4] + this.offsets[2] + this.zCoord;
		int maxZ = this.bounds[5] + this.offsets[2] + this.zCoord;
		
		// (minX -> maxX), minY || maxY, (minZ -> maxZ)
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				coordList.add(new int[] {
						x, minY, z
				});
				coordList.add(new int[] {
						x, maxY, z
				});
			}
		}
		// minX || maxX, (minY -> maxY), (minZ -> maxZ)
		for (int y = minY; y <= maxY; y++) {
			for (int z = minZ; z <= maxZ; z++) {
				coordList.add(new int[] {
						minX, y, z
				});
				coordList.add(new int[] {
						maxX, y, z
				});
			}
		}
		// (minX -> maxX), (minY -> maxY), minZ || maxZ
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				coordList.add(new int[] {
						x, y, minZ
				});
				coordList.add(new int[] {
						x, y, maxZ
				});
			}
		}
		
		int[][] coords = new int[coordList.size()][];
		for (int i = 0; i < coordList.size(); i++)
			coords[i] = coordList.get(i);
		return coords;
	}
	
	private int[] getOffsetsFromModule() {
		ItemStack moduleStack = this.getStackInSlot(0);
		if (moduleStack != null && moduleStack.getItem() instanceof ItemModuleWall) {
			ItemModuleWall module = (ItemModuleWall) moduleStack.getItem();
			int[] offsets = new int[3];
			offsets[0] = module.getOffset(moduleStack, 'X');
			offsets[1] = module.getOffset(moduleStack, 'Y');
			offsets[2] = module.getOffset(moduleStack, 'Z');
			return offsets;
		}
		return null;
	}
	
	private int[] getBoundsFromModule() {
		ItemStack moduleStack = this.getStackInSlot(0);
		if (moduleStack != null && moduleStack.getItem() instanceof ItemModuleWall) {
			ItemModuleWall module = (ItemModuleWall) moduleStack.getItem();
			int[] bounds = new int[6];
			bounds[0] = module.getBound(moduleStack, true, 'X');
			bounds[1] = module.getBound(moduleStack, false, 'X');
			bounds[2] = module.getBound(moduleStack, true, 'Y');
			bounds[3] = module.getBound(moduleStack, false, 'Y');
			bounds[4] = module.getBound(moduleStack, true, 'Z');
			bounds[5] = module.getBound(moduleStack, false, 'Z');
			return bounds;
		}
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setInteger("maxDelayHurt", this.maxHurtDelay);
		tagCom.setInteger("maxDelayWall", this.maxWallDelay);
		tagCom.setInteger("delay", this.delay);
		tagCom.setInteger("wallDelay", this.wallDelay);
		tagCom.setBoolean("needsUpdatedWall", this.updateWall);
		tagCom.setBoolean("changedWall", this.changeWall);
		tagCom.setBoolean("hasWall", this.hasWall);
		tagCom.setIntArray("bounds", this.bounds);
		tagCom.setIntArray("offsets", this.offsets);
		
		NBTTagList blacklistCoords = new NBTTagList();
		ArrayList<int[]> keys = new ArrayList<int[]>(this.blacklistBlocks.keySet());
		ArrayList<Integer> values = new ArrayList<Integer>(this.blacklistBlocks.values());
		for (int i = 0; i < this.blacklistBlocks.size(); i++) {
			NBTTagCompound blockCoordAndDelay = new NBTTagCompound();
			blockCoordAndDelay.setIntArray("coord", keys.get(i));
			blockCoordAndDelay.setInteger("tickDelay", values.get(i));
			blacklistCoords.appendTag(blockCoordAndDelay);
		}
		tagCom.setTag("blacklistBlocks", blacklistCoords);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		tagCom.setInteger("maxDelayHurt", this.maxHurtDelay);
		this.maxWallDelay = tagCom.getInteger("maxDelayWall");
		this.delay = tagCom.getInteger("delay");
		this.wallDelay = tagCom.getInteger("wallDelay");
		this.updateWall = tagCom.getBoolean("needsUpdatedWall");
		this.changeWall = tagCom.getBoolean("changedWall");
		this.hasWall = tagCom.getBoolean("hasWall");
		this.bounds = tagCom.getIntArray("bounds");
		this.offsets = tagCom.getIntArray("offsets");
		
		NBTTagList blackListCoords = tagCom.getTagList("blacklistBlocks", 10);
		this.blacklistBlocks.clear();
		for (int i = 0; i < blackListCoords.tagCount(); i++) {
			NBTTagCompound blockCoordAndDelay = blackListCoords.getCompoundTagAt(i);
			int[] coord = blockCoordAndDelay.getIntArray("coord");
			int tickDelay = blockCoordAndDelay.getInteger("tickDelay");
			this.blacklistBlocks.put(coord, tickDelay);
		}
		
	}
	
	public void setWallToHide(int x, int y, int z) {
		this.blacklistBlocks.put(new int[] {
				x, y, z
		}, 20 * 2);
		this.getWorldObj().setBlockToAir(x, y, z);
	}
	
}
