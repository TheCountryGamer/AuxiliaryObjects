package com.countrygamer.capo.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.countrygamer.capo.client.model.ModelModuleBase;
import com.countrygamer.capo.client.model.ModelModuleLaser;
import com.countrygamer.capo.client.model.ModelModuleWall;
import com.countrygamer.capo.common.lib.LaserRotation;
import com.countrygamer.capo.common.lib.Reference;
import com.countrygamer.capo.common.tileentity.TileEntityModuleBase;

public class TileEntityModuleBaseRenderer extends TileEntitySpecialRenderer {
	
	private ModelModuleBase model;
	
	public TileEntityModuleBaseRenderer() {
		this.model = new ModelModuleBase();
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tEnt, double dx, double dy, double dz, float f) {
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) dx + 0.5F, (float) dy + 0.5F, (float) dz + 0.5F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		if (tEnt != null) {
			TileEntityModuleBase tileEnt = (TileEntityModuleBase) tEnt;
			
			int i, j, k;
			i = tileEnt.xCoord;
			j = tileEnt.yCoord;
			k = tileEnt.zCoord;
			World world = tileEnt.getWorldObj();
			Block block = world.getBlock(i, j, k);
			
			Tessellator tessellator = Tessellator.instance;
			
			float f1 = block.getMixedBrightnessForBlock(world, i, j, k);
			int l = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
			int l1 = l % 65536;
			int l2 = l / 65536;
			tessellator.setColorOpaque_F(f1, f1, f1);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1,
					(float) l2);
			
			GL11.glPushMatrix();
			
			float f5 = 0.0625F;
			
			this.bindTexture(new ResourceLocation(Reference.MOD_ID,
					"textures/models/blocks/Module Base.png"));
			this.model.renderModel(f5);
			
			ItemStack moduleStack = tileEnt.getStackInSlot(0);
			if (moduleStack != null) {
				// check for sentry
				if (moduleStack.getItem() == tileEnt.sentry) {
					// render laser model
					this.bindTexture(new ResourceLocation(Reference.MOD_ID,
							"textures/models/blocks/Module Laser.png"));
					ModelModuleLaser laserModel = new ModelModuleLaser();
					laserModel.renderModel(f5);
					
					// render laser if applicable
					if (tileEnt.currentTarget != null && tileEnt.getCurrentDelayCount() <= 20) {
						GL11.glPushMatrix();
						
						GL11.glTranslatef(-((float) dx + 0.5F), -((float) dy + 0.5F),
								-((float) dz + 0.5F));
						GL11.glTranslatef((float) dx, (float) dy, (float) dz);
						// GL11.glTranslatef(0.0F, -0.2F, 0.0F);
						
						// Render Laser
						LaserRotation lr = new LaserRotation();
						lr.createPoints(tileEnt.xCoord, tileEnt.yCoord, tileEnt.zCoord,
								Math.floor(tileEnt.currentTarget.posX),
								Math.floor(tileEnt.currentTarget.posY),
								Math.floor(tileEnt.currentTarget.posZ));
						
						this.bindTexture(new ResourceLocation(Reference.MOD_ID,
								"textures/entity/laser_beam.png"));
						GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
						GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
						GL11.glDisable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_CULL_FACE);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glDepthMask(true);
						OpenGlHelper.glBlendFunc(770, 1, 1, 0);
						//float f2 = (float) world.getTotalWorldTime() + f;
						//float f3 = -f2 * 0.2F - (float) MathHelper.floor_float(-f2 * 0.1F);
						//byte b0 = 1;
						//double d03 = (double) f2 * 0.025D * (1.0D - (double) (b0 & 1) * 2.5D);
						tessellator.startDrawingQuads();
						tessellator.setColorRGBA(255, 255, 255, 32);
						
						tessellator = lr.tesselate(tessellator);
						
						tessellator.draw();
						
						GL11.glPopMatrix();
					}
					
				}
				// check for 'waller'
				else if (moduleStack.getItem() == tileEnt.forceField) {
					this.bindTexture(new ResourceLocation(Reference.MOD_ID,
							"textures/models/blocks/Module Wall.png"));
					ModelModuleWall wallModel = new ModelModuleWall();
					wallModel.renderModel(f5);
				}
				
			}
			
		}
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
}
