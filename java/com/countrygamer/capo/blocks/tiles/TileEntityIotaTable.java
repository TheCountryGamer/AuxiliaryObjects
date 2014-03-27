package com.countrygamer.capo.blocks.tiles;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;
import com.countrygamer.core.lib.CoreUtil;

/**
 * Created by Country_Gamer on 3/17/14.
 */
public class TileEntityIotaTable extends TileEntityInventoryBase {

	HashMap<ItemStack, ItemStack> ores = new HashMap<ItemStack, ItemStack>();
	String[] oreNames = new String[] {
			"Coal", "Iron", "Gold", "Lapis", "Redstone",
			"Diamond", "Emerald", "Copper", "Silver",
			"Tin", "Bronze"
	};

	private int hitAmount = 0;

	public TileEntityIotaTable () {
		super("Iotation Table", 1, 1);
		this.ores = CoreUtil.getBasicOreDict();
	}

	@Override
	public boolean isItemValidForSlot (int i, ItemStack itemstack) {
		if (i == 0) {
			ItemStack compareStack = itemstack.copy();
			compareStack.stackSize = 1;
			for (ItemStack oreStack : this.ores.keySet()) {
				//Capo.log.info(oreStack.getDisplayName());
				if (ItemStack.areItemStacksEqual(compareStack, oreStack)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide (int i) {
		return new int[i];
	}

	@Override
	public boolean canInsertItem (int slot, ItemStack itemStack, int side) {
		if (side == 1 || side == 0)
			return false;
		return this.isItemValidForSlot(slot, itemStack);
	}

	public void addHit() {
		if (this.getStackInSlot(0) != null)
			this.hitAmount += 1;
	}

	@Override
	public void updateEntity() {
		ItemStack oreStack = this.getStackInSlot(0);
		if (oreStack != null) {
			oreStack = oreStack.copy();
			if (this.hitAmount >= 40) {
				ItemStack tempStack = new ItemStack(oreStack.getItem(), 1, oreStack.getItemDamage());

				for (ItemStack stack : this.ores.keySet()) {
					if (stack != null && tempStack != null) {
						boolean isSame =
								tempStack.getItem() == stack.getItem() &&
										tempStack.stackSize == stack.stackSize &&
										tempStack.getItemDamage() == stack.getItemDamage();
						if (isSame) {
							ItemStack dropStack = this.ores.get(stack).copy();
							if (dropStack != null) {
								--tempStack.stackSize;

								if (tempStack.stackSize <= 0)
									tempStack = null;

								this.setInventorySlotContents(0, tempStack);
								dropStack.stackSize *= 2;
								CoreUtil.dropItemStack(this.worldObj, dropStack, this.xCoord, this.yCoord + 1, this.zCoord);
								this.hitAmount = 0;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void writeToNBT (NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setInteger("hits", this.hitAmount);
	}

	@Override
	public void readFromNBT (NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.hitAmount = tagCom.getInteger("hits");
	}
}

