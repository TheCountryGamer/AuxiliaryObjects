package com.countrygamer.capo.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.countrygamer.core.Base.common.item.ItemBase;

public class ItemLuminizer extends ItemBase {
	
	public ItemLuminizer(String modid, String name) {
		super(modid, name);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world,
			int x, int y, int z, int side, float par8, float par9, float par10) {
		int x1 = x, y1 = y, z1 = z;
		String blockText = "";
		if (!player.isSneaking()) {
			if (side == 0) {		// bottom -Y
				y1--;
				blockText = "the space below that block";
			}
			else if (side == 1) {	// top +Y
				y1++;
				blockText = "the space above that block";
			}
			else if (side == 2) {	// north -Z
				z1--;
				blockText = "the space north of that block";
			}
			else if (side == 3) {	// south +Z
				z1++;
				blockText = "the space south of that block";
			}
			else if (side == 4) {	// west -X
				x1--;
				blockText = "the space west of that block";
			}
			else if (side == 5) {	// east +X
				x1++;
				blockText = "the space east of that block";
			}
		}
		else
			blockText = "that block";
		
		int lightValue = world.getBlockLightValue(x1, y1, z1);
		if (!world.isRemote)
			player.addChatMessage(new ChatComponentText("The light level of "
					+ blockText + " is " + lightValue));
		
		return true;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,
			EntityPlayer player) {
		if (player.isSneaking()) {
			int lightValue = world
					.getBlockLightValue((int) Math.floor(player.posX),
							(int) Math.floor(player.posY + 1),
							(int) Math.floor(player.posZ));
			String blockText = "the space above the block you are standing on";
			if (!world.isRemote)
				player.addChatMessage(new ChatComponentText("The light level of "
						+ blockText + " is " + lightValue));
		}
		return itemStack;
	}
	
}
