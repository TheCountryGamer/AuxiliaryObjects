package com.countrygamer.capo.lib;

import net.minecraft.util.ResourceLocation;

public class Reference {
	
	/* Mod constants */
	public static final String MOD_ID = "capo";
	public static final String BASE_TEX = MOD_ID + ":";
	public static final String MOD_NAME = "CAPO";
	public static final String MOD_NAME_LONG = "Contingently Augmented Preposterous Objects";
	public static final String VERSION = "0.0.1";
	public static final String MC_VERSION = "1.7.2";
	public static final String CHANNEL_NAME = MOD_ID;
	public static final String SERVER_PROXY_CLASS = "com.countrygamer." + MOD_ID
			+ ".proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.countrygamer." + MOD_ID
			+ ".proxy.ClientProxy";
	
	public static int guiInvSack = 0;
	public static int guiInvSackRename = 1;
	public static int guiColorizer = 2;
	public static int guiInflixer = 3;
	public static int guiPlayerChecker = 4;
	
	public static final ResourceLocation endShardTex = new ResourceLocation(MOD_ID,
			"textures/blocks/Ender Shard_model.png");
	public static final ResourceLocation incubatorBackground = new ResourceLocation(MOD_ID,
			"textures/gui/incubator.png");
	
}
