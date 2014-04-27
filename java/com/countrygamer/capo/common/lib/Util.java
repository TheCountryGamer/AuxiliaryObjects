package com.countrygamer.capo.common.lib;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemDye;
import net.minecraft.world.World;

import com.countrygamer.capo.client.particle.EntityBlueSparkleFX;

public class Util {
	
	public static int getColorBlockiD(String color) {
		HashMap<String, Integer> coloredBlocks = new HashMap<String, Integer>();
		for (int i = 0; i < 16; i++) {
			coloredBlocks.put(ItemDye.field_150921_b[i],
					Util.getColoredBlockFromItem(i));
		}
		
		return coloredBlocks.get(color.toLowerCase());
		
	}
	
	public static int getColoredBlockFromItem(int itemMeta) {
		return ~itemMeta & 15;
	}
	
	public static void spawnBlueSparkle(World world, int x, int y, int z, float x1,
			float y1, float z1, int side) {
		if (world.isRemote) {
			float pX = x, pY = y, pZ = z;
			
			if (side == 4) {
				pX -= x1 + 0.05F;
			}
			else {
				pX += x1 + 0.05F;
			}
			pY += y1;
			if (side == 0) {
				pY -= 0.05F;
			}
			else if (side == 1) {
				pY += 0.05F;
			}
			if (side == 2) {
				pZ -= z1 + 0.05F;
			}
			else {
				pZ += z1 + 0.05F;
			}
			Minecraft.getMinecraft().effectRenderer
					.addEffect(new EntityBlueSparkleFX(world, pX, pY, pZ));
		}
	}
	
}
