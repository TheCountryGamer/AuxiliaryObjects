package com.countrygamer.auxiliaryobjects.proxy;

import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.countrygamer.auxiliaryobjects.Capo;
import com.countrygamer.auxiliaryobjects.blocks.tiles.TileEntityEnderShard;
import com.countrygamer.auxiliaryobjects.blocks.tiles.TileEntityTele;
import com.countrygamer.auxiliaryobjects.client.render.ItemRenderTeleCore;
import com.countrygamer.auxiliaryobjects.client.render.RenderEnderShard;
import com.countrygamer.auxiliaryobjects.client.render.TileEntityTeleRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends ServerProxy {
	
	@Override
	public void preInit() {
	}
	
	@Override
	public void registerRender() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEnderShard.class,
				new RenderEnderShard());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTele.class,
				new TileEntityTeleRenderer());
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
