package com.countrygamer.capo.client.render;

import com.countrygamer.capo.client.model.ModelEnderShard;
import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.tileentity.TileEntityEnderShard;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityEnderShardRenderer extends TileEntitySpecialRenderer {
	private ModelEnderShard aModel;
	
	public TileEntityEnderShardRenderer() {
		this.aModel = new ModelEnderShard();
		
	}
	@SideOnly(Side.CLIENT)
	public void renderAModelAt(TileEntityEnderShard tileEnt,
			double x, double y, double z, float f) {
		GL11.glPushMatrix();
		int metadata = tileEnt.getBlockMetadata();
		int rotationAngle = 0;
		if (metadata % 4 == 0) {
			rotationAngle = 0;
		}
		if (metadata % 4 == 1) {
			rotationAngle = 270;
		}
		if (metadata % 4 == 2) {
			rotationAngle = 180;
		}
		if (metadata % 4 == 3) {
			rotationAngle = 90;
		}
		GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
		
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);
		
		
		
		this.bindTexture(Reference.endShardTex);
		
		this.aModel.renderModel(0.0625F);
		GL11.glPopMatrix();
		
		
		
		
		
	}
	
	@SideOnly(Side.CLIENT)
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		renderAModelAt((TileEntityEnderShard)tileentity, x, y, z, f);
	}


}