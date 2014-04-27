package com.countrygamer.capo.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.countrygamer.capo.common.Capo;
import com.countrygamer.capo.common.tileentity.TileEntityWall;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WallBlockRenderer implements ISimpleBlockRenderingHandler {
	
	public WallBlockRenderer() {
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block,
			int modelId, RenderBlocks renderer) {
		if (world != null && block == Capo.wallBlock) {
			TileEntity tEnt = world.getTileEntity(x, y, z);
			if (tEnt != null && tEnt instanceof TileEntityWall) {
				TileEntityWall tileWall = (TileEntityWall) tEnt;
				
				Block camoBlock = tileWall.block;
				// int camoBlockMeta = tileWall.blockMeta;
				// Capo.log.info(tileWall.hasCamo + "");
				if (tileWall.hasCamo) {
					try {
						// renderer.renderBlockByRenderType(camoBlock, x, y, z);
						renderer.renderBlockUsingTexture(camoBlock, x, y, z, renderer
								.getBlockIconFromSideAndMetadata(camoBlock, 0, tileWall.blockMeta));
					} catch (Error e) {
						e.printStackTrace();
						renderer.renderStandardBlock(block, x, y, z);
					}
					return true;
				}
				else {
					// Capo.log.info("Using Texture");
					renderer.renderStandardBlock(block, x, y, z);
					return true;
				}
			}
			// Capo.log.info("Not Wall Ent");
		}
		// Capo.log.info("Not Wall");
		// renderer.renderStandardBlock(block, x, y, z);
		return true;
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}
	
	@Override
	public int getRenderId() {
		return 3000;
	}
	
}
