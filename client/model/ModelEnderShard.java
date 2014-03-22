package com.countrygamer.capo.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEnderShard extends ModelBase {
	
	ModelRenderer shard1, shard1_top;
	ModelRenderer shard2, shard3, shard4, shard5;
	
	public ModelEnderShard() {
		this.textureWidth = this.textureHeight = 64;
		
		this.shard1 = new ModelRenderer(this, 0, 0);
		//offsetX, offesetY, offestZ, width, height, length
		this.shard1.addBox(-2.0F, -8.0F, -2.0F, 4, 8, 4);
		// poX, posY, posZ
		this.shard1.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.shard1.setTextureSize(this.textureWidth, this.textureHeight);
		this.shard1.mirror = true;
		this.setRotation(this.shard1, 0F, 0F, 0F);
		
		this.shard1_top = new ModelRenderer(this, 0, 0);
		//posX1, posY1, posZ1, width, height, length
		this.shard1_top.addBox(-2.0F, -4.0F, 0.0F, 4, 4, 0);
		this.shard1_top.setRotationPoint(0.0F, 16.0F, 2.0F);
		this.shard1_top.setTextureSize(this.textureWidth, this.textureHeight);
		this.shard1_top.mirror = true;
		this.setRotation(this.shard1_top, 0.0F, 0.0F, 0.0F);
		
		
		this.shard2 = new ModelRenderer(this, 0, 0);
		//offsetX, offesetY, offestZ, width, height, length
		this.shard2.addBox(-2.0F, -8.0F, -2.0F, 4, 8, 4);
		// poX, posY, posZ
		this.shard2.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.shard2.setTextureSize(this.textureWidth, this.textureHeight);
		this.shard2.mirror = true;
		this.setRotation(this.shard2, 0.6F, 0.0F, 0.7F);
		
		this.shard3 = new ModelRenderer(this, 0, 0);
		//offsetX, offesetY, offestZ, width, height, length
		this.shard3.addBox(-2.0F, -8.0F, -2.0F, 4, 8, 4);
		// poX, posY, posZ
		this.shard3.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.shard3.setTextureSize(this.textureWidth, this.textureHeight);
		this.shard3.mirror = true;
		this.setRotation(this.shard3, -0.7F, 0.0F, 0.6F);
		
		this.shard4 = new ModelRenderer(this, 0, 0);
		//offsetX, offesetY, offestZ, width, height, length
		this.shard4.addBox(-2.0F, -8.0F, -2.0F, 4, 8, 4);
		// poX, posY, posZ
		this.shard4.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.shard4.setTextureSize(this.textureWidth, this.textureHeight);
		this.shard4.mirror = true;
		this.setRotation(this.shard4, -0.6F, 0.0F, -0.7F);
		
		this.shard5 = new ModelRenderer(this, 0, 0);
		//offsetX, offesetY, offestZ, width, height, length
		this.shard5.addBox(-2.0F, -8.0F, -2.0F, 4, 8, 4);
		// poX, posY, posZ
		this.shard5.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.shard5.setTextureSize(this.textureWidth, this.textureHeight);
		this.shard5.mirror = true;
		this.setRotation(this.shard5, 0.7F, 0.0F, -0.6F);
		
	}
	
	public void render(Entity entity, float f, float f1, float f2,
			float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		this.shard1.render(f5);
		this.shard1_top.render(f5);
		this.shard2.render(f5);
		this.shard3.render(f5);
		this.shard4.render(f5);
		this.shard5.render(f5);
		
	}
	public void renderModel(float f5) {
		
		this.shard1.render(f5);
		//this.shard1_top.render(f5);
		this.shard2.render(f5);
		this.shard3.render(f5);
		this.shard4.render(f5);
		this.shard5.render(f5);
		
	}
		
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
		//super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
	}
	
	
	
}
