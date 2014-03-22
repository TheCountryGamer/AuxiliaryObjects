package com.countrygamer.capo.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.countrygamer.capo.Capo;
import com.countrygamer.core.Base.item.ItemBase;

public class ItemEnderShard extends ItemBase {

	public ItemEnderShard(int id, String modid, String name) {
		super(modid, name);
	}
	
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player,
			World world, int x, int y, int z, int side,
			float par8, float par9, float par10) {
		boolean canPlace = side == 1 &&
				world.getBlock(x, y+1, z) == null;
		
		if(canPlace) {
			if(!player.capabilities.isCreativeMode)
				itemStack.stackSize--;
			world.setBlock(x, y+1, z, Capo.endShard);
			return true;
		}
		
		return false;
	}
	
}
