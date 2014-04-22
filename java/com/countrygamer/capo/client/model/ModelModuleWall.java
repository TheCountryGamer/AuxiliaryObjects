package com.countrygamer.capo.client.model;

import com.countrygamer.core.Base.client.ModelBase;

public class ModelModuleWall extends ModelBase {

	public ModelModuleWall() {
		super(64, 64);
		
		this.createModel(this, modelList,
				0.0F, 3.0F, 0.0F,
				-7.0F, -1.0F, -7.0F,
				14, 1, 14,
				0.0F, 0.0F, 0.0F,
				textureWidth, textureHeight, 0, 0);
		
		this.createModel(this, this.modelList,
				0.0F, 4.0F, 0.0F, // origin coords
				-1.0F, -11.0F, -1.0F, // offset coords
				2, 11, 2, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 0, 15);
		
		this.createModel(this, this.modelList,
				0.0F, 4.0F, 0.0F, // origin coords
				-7.0F, -7.0F, -1.0F, // offset coords
				14, 2, 2, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 8, 15);
		
		this.createModel(this, this.modelList,
				0.0F, 4.0F, 0.0F, // origin coords
				-1.0F, -7.0F, -7.0F, // offset coords
				2, 2, 14, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 8, 19);
		
		//
		this.createModel(this, this.modelList,
				-0.5F, 12.0F, 0.0F, // origin coords
				7.5F, -2.0F, -6.0F, // offset coords
				1, 9, 12, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 0, 35);
		
		this.createModel(this, this.modelList,
				-0.5F, 12.0F, 0.0F, // origin coords
				-7.5F, -2.0F, -6.0F, // offset coords
				1, 9, 12, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 0, 35);
		
		//
		
		this.createModel(this, this.modelList,
				0.0F, 12.0F, -0.5F, // origin coords
				-6.0F, -2.0F, -7.5F, // offset coords
				12, 9, 1, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 26, 35);
		
		this.createModel(this, this.modelList,
				0.0F, 12.0F, -0.5F, // origin coords
				-6.0F, -2.0F, 7.5F, // offset coords
				12, 9, 1, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 26, 35);
		
		//
		
		this.createModel(this, this.modelList,
				0.0F, 12.0F, 0.0F, // origin coords
				-6.0F, -4.0F, -6.0F, // offset coords
				12, 1, 12, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 15, 45);
		
	}
	
}
