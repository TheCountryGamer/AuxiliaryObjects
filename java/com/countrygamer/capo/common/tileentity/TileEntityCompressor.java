package com.countrygamer.capo.common.tileentity;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.core.Base.common.tileentity.TileEntityInventoryBase;

public class TileEntityCompressor extends TileEntityInventoryBase {
	
	private final int maxDelay = 20 * 1;
	private int delay = maxDelay;
	
	/**
	 * Uses indicies to decide mode.
	 * 1 = compress ;
	 * 2 = decompress ;
	 * 3 = auto
	 */
	public int mode = 1;
	
	/*
	 * Uses indicies to decide redstone state.
	 * 		1 = disabled
	 * 		2 = low power (redstone == on)
	 * 		3 = high power (redstone == off)
	 */
	public int redstoneMode = 1;
	
	public TileEntityCompressor() {
		super("Compressor", 3, 64);
	}
	
	public void getNextMode() {
		this.mode += 1;
		if (this.mode > 3) this.mode = 1;
	}
	
	public void getPreviousMode() {
		this.mode -= 1;
		if (this.mode < 1) this.mode = 3;
	}
	
	public void getNextRedstoneState() {
		++this.redstoneMode;
		if (this.redstoneMode > 3) this.redstoneMode = 1;
	}
	
	public void getPreviousRedstoneState() {
		--this.redstoneMode;
		if (this.redstoneMode < 1) this.redstoneMode = 3;
	}
	
	@Override
	public void updateEntity() {
		ItemStack input = this.getStackInSlot(0);
		
		if (input != null) {
			// if (!this.getWorldObj().isRemote)
			// (new LogBlock(Capo.log, "\n").add("Has input")).log();
			// if (!this.getWorldObj().isRemote)
			// (new LogBlock(Capo.log, "\n").add("Trying....")).log();
			int sideLength = 0;
			
			if (this.mode == 1)
				sideLength = 3;
			else if (this.mode == 2)
				sideLength = 1;
			else if (this.mode == 3) {
				if (this.canCompress(input, 1)) {
					sideLength = 1;
				}
				else
					sideLength = 3;
			}
			
			if (sideLength == 3) {
				// if (!this.getWorldObj().isRemote)
				// (new LogBlock(Capo.log, "\n").add("Need to calculate for 2x2 first")).log();
				if (this.canCompress(input, 2)) {
					// if (!this.getWorldObj().isRemote)
					// (new LogBlock(Capo.log, "\n").add("Can 2x2")).log();
					this.compress(input, 2);
				}
				else if (this.canCompress(input, 3)) {
					// if (!this.getWorldObj().isRemote)
					// (new LogBlock(Capo.log, "\n").add("Can 3x3")).log();
					this.compress(input, 3);
				}
			}
			else if (this.canCompress(input, sideLength)) {
				/*
					if (!this.getWorldObj().isRemote)
						(new LogBlock(Capo.log, "\n").add("Comressing " + sideLength + "x"
								+ sideLength)).log();
				 */
				this.compress(input, sideLength);
			}
		}
	}
	
	private boolean canCompress(ItemStack input, int crafterSideLength) {
		InventoryCrafting inv = this.getCrafterFromItemStack(input, crafterSideLength);
		return this.getRecipe(input, inv) != null;
	}
	
	private void compress(ItemStack input, int crafterSideLength) {
		ItemStack newInput = input.copy();
		ItemStack addOutput = null;
		
		InventoryCrafting inv = this.getCrafterFromItemStack(input, crafterSideLength);
		if (this.getRecipe(newInput, inv) != null)
			addOutput = this.getRecipe(newInput, inv).getCraftingResult(inv);
		
		if (addOutput != null) {
			newInput.stackSize -= crafterSideLength * crafterSideLength;
			
			if (newInput.stackSize <= 0) {
				if (newInput.stackSize < 0) return;
				newInput = null;
			}
			
			ItemStack output = this.getStackInSlot(1);
			if (output == null) {
				this.runCompressionWithConditionals(newInput, addOutput);
				return;
			}
			
			ItemStack newOutput = output.copy();
			if (output.getItem() == addOutput.getItem()
					&& output.getItemDamage() == addOutput.getItemDamage()) {
				int max = output.getMaxStackSize();
				int total = output.stackSize + addOutput.stackSize;
				if (total <= max) {
					newOutput.stackSize += addOutput.stackSize;
					this.runCompressionWithConditionals(newInput, newOutput);
				}
			}
			
		}
	}
	
	private void runCompressionWithConditionals(ItemStack input, ItemStack output) {
		boolean canFunction = false;
		//canFunction = this.redstoneMode == 1 ? true : (this.redstoneMode == 2 ? this.isPowered()
		//		: (this.redstoneMode == 3 ? !this.isPowered() : false));
		
		if (this.redstoneMode == 1) {
			canFunction = true;
		}
		else if (this.redstoneMode == 2) {
			canFunction = this.isPowered();
		}
		else if (this.redstoneMode == 3) {
			canFunction = !this.isPowered();
		}
		else {
			canFunction = false;
		}
		
		
		if (canFunction) {
			if (this.delay > 0) {
				--this.delay;
				return;
			}

			this.setInventorySlotContents(0, input);
			this.setInventorySlotContents(1, output);
			
			this.delay = this.maxDelay;
		}
		
	}
	
	private IRecipe getRecipe(ItemStack input, InventoryCrafting inv) {
		@SuppressWarnings("unchecked")
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			if (recipe.matches(inv, this.getWorldObj())) {
				if (recipe.getCraftingResult(inv) != null) return recipe;
			}
		}
		
		return null;
	}
	
	private InventoryCrafting getCrafterFromItemStack(ItemStack input, int sideLength) {
		InventoryCrafting invCrafting = new InventoryCrafting(new Container() {
			public boolean canInteractWith(EntityPlayer entityplayer) {
				return false;
			}
			
			public void onCraftMatrixChanged(IInventory iinventory) {
			}
		}, sideLength, sideLength);
		
		ItemStack inputSingle = input.copy();
		inputSingle.stackSize = 1;
		
		for (int i = 0; i < sideLength * sideLength; i++) {
			invCrafting.setInventorySlotContents(i, inputSingle.copy());
		}
		
		return invCrafting;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] {
				0, 1
		};
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i == 0;
	}
	
	@Override
	public boolean canInsertItem(int i, ItemStack itemStack, int side) {
		return this.isItemValidForSlot(i, itemStack);
	}
	
	@Override
	public boolean canExtractItem(int i, ItemStack itemStack, int side) {
		return i == 1;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		tagCom.setInteger("compressor_delay", this.delay);
		tagCom.setInteger("compressor_mode", this.mode);
		tagCom.setInteger("compressor_redstoneState", this.redstoneMode);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		this.delay = tagCom.getInteger("compressor_delay");
		this.mode = tagCom.getInteger("compressor_mode");
		this.redstoneMode = tagCom.getInteger("compressor_redstoneState");
		
	}
	
}
