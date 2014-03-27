package com.countrygamer.capo.client.render;

import com.countrygamer.capo.client.model.ModelCore;
import com.countrygamer.capo.client.model.ModelTele;
import com.countrygamer.core.Base.client.Render.TileEntityRendererBase;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.countrygamer.capo.Capo;
import com.countrygamer.capo.blocks.tiles.TileEntityTele;
import com.countrygamer.capo.lib.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityTeleRenderer extends TileEntitySpecialRenderer implements TileEntityRendererBase {

	@SuppressWarnings("unused")
	private ModelCore core;

	public TileEntityTeleRenderer() {
		this.core = new ModelCore();
	}

	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
		GL11.glPushMatrix();
		// This will move our renderer so that it will be on proper place in the
		// world
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

		TileEntityTele tileEnt = (TileEntityTele) tileEntity;
		/*
		 * Note that true tile entity coordinates (tileEntity.xCoord, etc) do
		 * not match to render coordinates (d, etc) that are calculated as [true
		 * coordinates] - [player coordinates (camera coordinates)]
		 */
		renderBlock(tileEnt, tileEnt.getWorldObj(), tileEnt.xCoord, tileEnt.yCoord, tileEnt.zCoord,
				Capo.teleporterBase);
		GL11.glPopMatrix();

	}

	public void renderBlock(TileEntityTele tileEnt, World world, int i, int j, int k, Block block) {
		Tessellator tessellator = Tessellator.instance;
		// This will make your block brightness dependent from surroundings
		// lighting.
		float f = 1.0F;// TODO block.getBlockBrightness(world, i, j, k);
		int l = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f, f, f);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

		/*
		 * This will rotate your model corresponding to player direction that
		 * was when you placed the block. If you want this to work, add these
		 * lines to onBlockPlacedBy method in your block class. int dir =
		 * MathHelper.floor_double((double)((player.rotationYaw * 4F) / 360F) +
		 * 0.5D) & 3; world.setBlockMetadataWithNotify(x, y, z, dir, 0);
		 */

		// int dir = world.getBlockMetadata(i, j, k);

		GL11.glPushMatrix();
		// GL11.glTranslatef(0.5F, 0, 0.5F);
		// This line actually rotates the renderer.
		// GL11.glRotatef(dir * (-90F), 0F, 1F, 0F);
		// GL11.glTranslatef(-0.5F, 0, -0.5F);

		/*
		 * Place your rendering code here.
		 */
		if (tileEnt != null) {
			float f5 = 0.0625F;
			ModelTele model = new ModelTele();

			this.bindTexture(new ResourceLocation(Reference.MOD_ID,
					"textures/models/blocks/Tele_Basic.png"));
			model.renderModel(f5);

			if (tileEnt.getStackInSlot(0) != null) {
				String name = tileEnt.getStackInSlot(0).getItem().getUnlocalizedName().substring(5);

				this.bindTexture(new ResourceLocation(Reference.MOD_ID,
						"textures/models/blocks/Tele_" + name + "_A.png"));
				model.renderModel(f5);

				if (!tileEnt.hasBlockOnTop()) {
					ModelCore core = new ModelCore();
					this.bindTexture(new ResourceLocation(Reference.MOD_ID,
							"textures/models/items/" + name + "_A.png"));
					core.modelList.get(0).rotateAngleY = (tileEnt.coreModelRotations[1] += 0.05F);
					core.modelList.get(0).rotateAngleX = (tileEnt.coreModelRotations[0] += 0.01F);
					core.modelList.get(0).rotateAngleZ = (tileEnt.coreModelRotations[2] += 0.01F);
					core.renderModel(f5);
				}
			}

			if (!tileEnt.isSlotEmpty("redstone")) {
				this.bindTexture(new ResourceLocation(Reference.MOD_ID,
						"textures/models/blocks/Tele_Redstone.png"));
				model.renderModel(f5);
			}
			if (!tileEnt.isSlotEmpty("gold")) {
				this.bindTexture(new ResourceLocation(Reference.MOD_ID,
						"textures/models/blocks/Tele_Gold.png"));
				model.renderModel(f5);
			}
			if (!tileEnt.isSlotEmpty("ender")) {
				this.bindTexture(new ResourceLocation(Reference.MOD_ID,
						"textures/models/blocks/Tele_Ender.png"));
				model.renderModel(f5);
			}

		}
		GL11.glPopMatrix();
	}

	@Override
	public void renderBasicModelForTile (
			World world, int x, int y, int z,
			double d1, double d2, double d3, float f) {
		GL11.glPushMatrix();

		GL11.glTranslatef((float) d1 + 0.5F, (float) d2 + 1.5F, (float) d3 + 0.5F);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

		Tessellator tessellator = Tessellator.instance;
		float f1 = 1.0F;// TODO block.getBlockBrightness(world, i, j, k);
		int l = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		int l1 = l % 65536;
		int l2 = l / 65536;
		tessellator.setColorOpaque_F(f1, f1, f1);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) l1, (float) l2);

		GL11.glPushMatrix();
		float f5 = 0.0625F;
		ModelTele model = new ModelTele();

		this.bindTexture(new ResourceLocation(Reference.MOD_ID,
				"textures/models/blocks/Tele_Basic.png"));
		model.renderModel(f5);
		GL11.glPopMatrix();

		GL11.glPopMatrix();
	}


}
