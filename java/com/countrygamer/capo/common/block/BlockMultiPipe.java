package com.countrygamer.capo.common.block;

import com.countrygamer.capo.common.tileentity.TileEntityMultiPipe;
import com.countrygamer.core.Base.common.block.BlockContainerBase;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Country_Gamer on 3/16/14.
 */
public class BlockMultiPipe extends BlockContainerBase {

	public static enum EnumPipeType {
		ENERGY, ITEM, LIQUID
	}

	//

	public final EnumPipeType pipeType;

	public BlockMultiPipe (String modid, String name, EnumPipeType type) {
		super(Material.rock, modid, name, TileEntityMultiPipe.class);

		this.pipeType = type;


	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityMultiPipe(this.pipeType);
	}


}
