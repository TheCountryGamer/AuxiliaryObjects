package com.countrygamer.capo.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.countrygamer.capo.common.lib.Reference;

public class EntityBlueSparkleFX extends EntityFX {
	
	private final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID,
			"textures/particle/blueSparkle.png");
	
	public EntityBlueSparkleFX(World world, double x, double y, double z) {
		super(world, x, y, z);
		this.setGravity(0);
	}
	
	@Override
	public void renderParticle(Tessellator tess, float partialTicks, float par3,
			float par4, float par5, float par6, float par7) {
		Minecraft.getMinecraft().renderEngine.bindTexture(this.texture);
		
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		
		tess.startDrawingQuads();
		
		tess.setBrightness(this.getBrightnessForRender(partialTicks));
		
		float scale = 0.1F * this.particleScale;
		float x = (float) (this.prevPosX + (this.posX - this.prevPosX)
				* partialTicks - EntityFX.interpPosX);
		float y = (float) (this.prevPosY + (this.posY - this.prevPosY)
				* partialTicks - EntityFX.interpPosY);
		float z = (float) (this.prevPosZ + (this.posZ - this.prevPosZ)
				* partialTicks - EntityFX.interpPosZ);
		
		tess.addVertexWithUV(x - (par3 * scale) - (par6 * scale),
				y - (par4 * scale), z - (par5 * scale) - (par7 * scale), 0, 0);
		tess.addVertexWithUV(x - (par3 * scale) + (par6 * scale),
				y + (par4 * scale), z - (par5 * scale) + (par7 * scale), 1, 0);
		tess.addVertexWithUV(x + (par3 * scale) + (par6 * scale),
				y + (par4 * scale), z + (par5 * scale) + (par7 * scale), 1, 1);
		tess.addVertexWithUV(x + (par3 * scale) - (par6 * scale),
				y - (par4 * scale), z + (par5 * scale) - (par7 * scale), 0, 1);
		
		tess.draw();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		
	}
	
	@Override
	public int getFXLayer() {
		return 3;
	}
	
	public EntityBlueSparkleFX setMaxAge(int maxAge) {
		this.particleMaxAge = maxAge;
		return this;
	}
	
	public EntityBlueSparkleFX setGravity(float gravity) {
		this.particleGravity = gravity;
		return this;
	}
	
	public EntityBlueSparkleFX setScale(float scale) {
		this.particleScale = scale;
		return this;
	}
}
