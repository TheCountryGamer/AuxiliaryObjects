package com.countrygamer.capo.blocks.tiles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumSkyBlock;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.items.ItemModuleWall;
import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;

public class TileEntityModuleBase extends TileEntityInventoryBase {
	
	final double PI = 3.14159265;
	
	public final Item sentry = Items.stick;
	public final Item forceField = Capo.moduleWall;
	
	public EntityLivingBase currentTarget = null;
	public int quad = 0;
	public float horizontalAngle = 0.75F;
	private int maxHurtDelay = 20 * 4, maxWallDelay = 20 * 2, delay;
	private boolean updateWall = false;
	private boolean changeWall = false;
	public boolean hasWall = false;
	
	private int[] offsets = new int[3];
	private int[] bounds = new int[6];
	
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
		if (moduleStack != null) {
			if (moduleStack.getItem() == sentry) {
				// Capo.log.info("Trigger Laser Update");
				this.updateLaser();
			}
			if (moduleStack.getItem() == forceField) {
				this.updateWall();
			}
		}
		
		if (this.hasWall && (moduleStack == null || moduleStack.getItem() != forceField)) {
			this.emptyField();
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
		// isPowered == should place wall
		// not isPowered == should remove wall
		if (this.isPowered() && !this.hasWall)
			this.changeWall = true;
		else if (!this.isPowered() && this.hasWall) this.changeWall = true;
		
		if (this.changeWall) {
			ItemStack moduleStack = this.getStackInSlot(0);
			ItemModuleWall module = (ItemModuleWall) moduleStack.getItem();
			char[] axiss = new char[] {
					'X', 'Y', 'Z'
			};
			for (int i = 0; i < axiss.length; i++) {
				char axis = axiss[i];
				
				this.bounds[2 * i + 0] = module.getBound(moduleStack, true, axis);
				this.bounds[2 * i + 1] = module.getBound(moduleStack, false, axis);
				
				this.offsets[i] = module.getOffset(moduleStack, axis);
				
			}
			
			if (this.hasWall)
				this.emptyField();
			else
				this.fillField();
			
			this.hasWall = !this.hasWall;
			this.changeWall = false;
		}
		if (this.updateWall) {
			// Capo.log.info(this.hasWall + "");
			if (this.hasWall) {
				this.emptyField();
				this.fillField();
			}
			this.updateWall = false;
		}
		
	}
	
	public void fillField() {
		ItemStack moduleStack = this.getStackInSlot(0);
		if (moduleStack != null && moduleStack.getItem() instanceof ItemModuleWall) {
			ItemStack camoStack = ((ItemModuleWall) moduleStack.getItem())
					.loadCamoStack(moduleStack);
			
			Block camoBlock = camoStack != null ? Block.getBlockFromItem(camoStack.getItem())
					: null;
			int meta = camoStack != null ? camoStack.getItemDamage() : 0;
			
			this.modifyField(Capo.wallBlock, camoBlock != Blocks.air ? camoBlock : null, meta);
		}
	}
	
	public void emptyField() {
		this.modifyField(Blocks.air, null, 0);
	}
	
	public void modifyField(Block newFieldBlock, Block camoBlock, int metadata) {
		int[][] coords = this.getFieldBounds();
		for (int[] coord : coords) {
			int x1 = this.xCoord + coord[0];
			int y1 = this.yCoord + coord[1];
			int z1 = this.zCoord + coord[2];
			
			this.getWorldObj().setBlock(x1, y1, z1, newFieldBlock, 0, 3);
			
			if (newFieldBlock == Capo.wallBlock) {
				TileEntity tEnt = this.getWorldObj().getTileEntity(x1, y1, z1);
				if (tEnt != null && tEnt instanceof TileEntityWall) {
					TileEntityWall wallEnt = (TileEntityWall) tEnt;
					wallEnt.setBlockWithMeta(camoBlock, metadata);
					this.worldObj.updateLightByType(EnumSkyBlock.Block, x1, y1, z1);//.updateAllLightTypes(xCoord, yCoord, zCoord);
				}
			}
			
			this.getWorldObj().scheduleBlockUpdate(x1, y1, z1, newFieldBlock, 10);
		}
		
	}
	
	private int[][] getFieldBounds() {
		ArrayList<int[]> coordList = new ArrayList<int[]>();
		
		int minX = this.bounds[0] + this.offsets[0];
		int maxX = this.bounds[1] + this.offsets[0];
		int minY = this.bounds[2] + this.offsets[1];
		int maxY = this.bounds[3] + this.offsets[1];
		int minZ = this.bounds[4] + this.offsets[2];
		int maxZ = this.bounds[5] + this.offsets[2];
		
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
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setInteger("maxDelayHurt", this.maxHurtDelay);
		tagCom.setInteger("maxDelayWall", this.maxWallDelay);
		tagCom.setInteger("delay", this.delay);
		tagCom.setBoolean("needsUpdatedWall", this.updateWall);
		tagCom.setBoolean("changedWall", this.changeWall);
		tagCom.setBoolean("hasWall", this.hasWall);
		tagCom.setIntArray("bounds", this.bounds);
		tagCom.setIntArray("offsets", this.offsets);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		tagCom.setInteger("maxDelayHurt", this.maxHurtDelay);
		this.maxWallDelay = tagCom.getInteger("maxDelayWall");
		this.delay = tagCom.getInteger("delay");
		this.updateWall = tagCom.getBoolean("needsUpdatedWall");
		this.changeWall = tagCom.getBoolean("changedWall");
		this.hasWall = tagCom.getBoolean("hasWall");
		this.bounds = tagCom.getIntArray("bounds");
		this.offsets = tagCom.getIntArray("offsets");
		
	}
	
}
