package com.countrygamer.capo.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;

import com.countrygamer.core.Base.block.tiles.TileEntityInventoryBase;

public class TileEntityColorizerII extends TileEntityInventoryBase {
	
	private int[] hexColors = new int[] {
			-1, -1, -1
	};
	
	public TileEntityColorizerII() {
		super("Colorizer Mark II", 2, 1);
	}
	
	public void setHexColor(int index, int hexValue) {
		if (0 <= index && index < this.hexColors.length) this.hexColors[index] = hexValue;
	}
	
	public int[] getHexColors() {
		return this.hexColors;
	}
	
	public void setHexValuesToTriDye() {
		// TODO
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCom) {
		super.writeToNBT(tagCom);
		
		tagCom.setInteger("colorizerII_colorLength", this.hexColors.length);
		for (int i = 0; i < this.hexColors.length; i++) {
			tagCom.setInteger("colorizerII_color" + i, this.hexColors[i]);
		}
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCom) {
		super.readFromNBT(tagCom);
		
		int length = tagCom.getInteger("colorizerII_colorLength");
		this.hexColors = new int[length];
		for (int i = 0; i < length; i++) {
			this.hexColors[i] = tagCom.getInteger("colorizerII_color" + i);
		}
		
	}
	
	
	
}
