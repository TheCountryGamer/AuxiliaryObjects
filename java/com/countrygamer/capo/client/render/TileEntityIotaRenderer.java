package com.countrygamer.capo.client.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityIotaTable;
import com.countrygamer.capo.client.model.ModelIotaTable;
import com.countrygamer.capo.lib.Reference;
import com.countrygamer.core.Base.client.ModelBase;
import com.countrygamer.core.Base.client.Render.TileEntityRendererBase;
import com.countrygamer.core.lib.CoreUtilRender;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Country_Gamer on 3/17/14.
 */
@SideOnly(Side.CLIENT)
public class TileEntityIotaRenderer extends TileEntitySpecialRenderer implements TileEntityRendererBase {

	private ModelBase model;

	private final RenderItem oreRender;

	public TileEntityIotaRenderer () {
		this.model = new ModelIotaTable();

		this.oreRender = CoreUtilRender.basicRender;
		this.oreRender.setRenderManager(RenderManager.instance);

	}

	@Override
	public void renderTileEntityAt (TileEntity tileEntity, double d1, double d2, double d3, float f) {
		tileEntity.getWorldObj().scheduleBlockUpdate(
				tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
				Capo.iotationTable,
				10);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d1 + 0.5F, (float) d2 + 1.5F, (float) d3 + 0.5F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

		TileEntityIotaTable tileEnt = (TileEntityIotaTable)tileEntity;

		Tessellator tessellator = Tessellator.instance;
		// This will make your block brightness dependent from surroundings
		// lighting.
		float f1 = 1.0F;// TODO block.getBlockBrightness(world, i, j, k);
		int l = tileEnt.getWorldObj().getLightBrightnessForSkyBlocks(
				tileEnt.xCoord, tileEnt.yCoord, tileEnt.zCoord, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f1, f1, f1);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

		GL11.glPushMatrix();

		/*
		this.renderBasicModelForTile(
				tileEntity.getWorldObj(),
				tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord,
				i, j, k, f);
		*/
		float f5 = 0.0625F;
		this.bindTexture(new ResourceLocation(Reference.MOD_ID,
				"textures/models/blocks/IotaTable.png"));
		this.model.renderModel(f5);
		GL11.glPopMatrix();

		GL11.glPopMatrix();

		GL11.glPushMatrix();

		GL11.glTranslatef((float) d1, (float) d2, (float) d3);
		ItemStack oreStack = tileEnt.getStackInSlot(0);
		if (oreStack != null) {
			CoreUtilRender.renderItem(tileEnt, this.oreRender, oreStack, 0.5F, 1.0F, 0.5F);
		}

		GL11.glPopMatrix();

	}

	@Override
	public void renderBasicModelForTile (
			World world, int x, int y, int z,
			double d1, double d2, double d3, float f) {
		GL11.glPushMatrix();

		// Correct Model Translation
		GL11.glTranslatef((float) d1 + 0.5F, (float) d2 + 1.5F, (float) d3 + 0.5F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

		Tessellator tessellator = Tessellator.instance;
		// This will make your block brightness dependent from surroundings
		// lighting.
		float f1 = world.getLightBrightness(x, y, z);//1.0F;// TODO block.getBlockBrightness(world, i, j, k);
		int l = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f1, f1, f1);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

		GL11.glPushMatrix();
			float f5 = 0.0625F;
			this.bindTexture(new ResourceLocation(Reference.MOD_ID,
					"textures/models/blocks/IotaTable.png"));
			this.model.renderModel(f5);

		GL11.glPopMatrix();

		GL11.glPopMatrix();
	}
}
