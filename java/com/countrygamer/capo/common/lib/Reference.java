package com.countrygamer.capo.common.lib;

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
	
	public static final int guiInvSack = 0;
	public static final int guiInvSackRename = 1;
	public static final int guiColorizer = 2;
	public static final int guiInflixer = 3;
	public static final int guiPlayerChecker = 4;
	public static final int guiCompressor = 5;
	public static final int guiColorizerII = 6;
	public static final int guiModuleBase = 7;
	public static final int guiAssembler = 8;
	public static final int guiAssemblerSettings = 9;
	
	public static final ResourceLocation endShardTex = new ResourceLocation(MOD_ID,
			"textures/blocks/Ender Shard_model.png");
	public static final ResourceLocation incubatorBackground = new ResourceLocation(MOD_ID,
			"textures/gui/incubator.png");
	
}
