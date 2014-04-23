package com.countrygamer.capo.client;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.countrygamer.capo.client.render.ItemRenderTeleCore;
import com.countrygamer.capo.client.render.TileEntityEnderShardRenderer;
import com.countrygamer.capo.client.render.TileEntityIotaRenderer;
import com.countrygamer.capo.client.render.TileEntityModuleBaseRenderer;
import com.countrygamer.capo.client.render.TileEntityTeleRenderer;
import com.countrygamer.capo.client.render.WallBlockRenderer;
import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.ServerProxy;
import com.countrygamer.capo.common.tileentity.TileEntityEnderShard;
import com.countrygamer.capo.common.tileentity.TileEntityIotaTable;
import com.countrygamer.capo.common.tileentity.TileEntityModuleBase;
import com.countrygamer.capo.common.tileentity.TileEntityTele;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ServerProxy {
	
	@Override
	public void preInit() {
	}
	
	@Override
	public void registerRender() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnderShard.class,
				new TileEntityEnderShardRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTele.class,
				new TileEntityTeleRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIotaTable.class,
				new TileEntityIotaRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityModuleBase.class,
				new TileEntityModuleBaseRenderer());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWall.class,
		//		new TileEntityWallRenderer());
		
		RenderingRegistry.registerBlockHandler(new WallBlockRenderer());
		
	}
	
	@Override
	public void registerItemRender() {
		MinecraftForgeClient.registerItemRenderer(Capo.stableCore,
				(IItemRenderer) (new ItemRenderTeleCore()));
		MinecraftForgeClient.registerItemRenderer(Capo.unStableCore,
				(IItemRenderer) (new ItemRenderTeleCore()));
	}
	
	@Override
	public int addArmor(String armor) {
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}
	
}
