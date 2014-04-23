package com.countrygamer.capo.common.lib;

import java.util.HashMap;

import net.minecraft.item.ItemDye;

public class Util {
	
	public static int getColorBlockiD(String color) {
		HashMap<String, Integer> coloredBlocks = new HashMap<String, Integer>();
		for(int i = 0; i < 16; i++) {
			coloredBlocks.put(
					ItemDye.field_150921_b[i],
					Util.getColoredBlockFromItem(i));
		}
		
		return coloredBlocks.get(color.toLowerCase());
		
	}
	public static int getColoredBlockFromItem(int itemMeta) {
		return ~itemMeta & 15;
	}
	
	
}
