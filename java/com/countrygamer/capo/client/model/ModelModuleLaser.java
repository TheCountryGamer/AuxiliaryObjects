package com.countrygamer.capo.client.model;

import com.countrygamer.core.Base.client.ModelBase;

public class ModelModuleLaser extends ModelBase {

	public ModelModuleLaser() {
		super(64, 64);
		
		this.createModel(this, modelList,
				0.0F, 3.0F, 0.0F,
				-7.0F, -1.0F, -7.0F,
				14, 1, 14,
				0.0F, 0.0F, 0.0F,
				textureWidth, textureHeight, 0, 0);
		
		this.createModel(this, this.modelList,
				0.0F, 4.0F, 0.0F, // origin coords
				-6.0F, -2.0F, -6.0F, // offset coords
				12, 2, 12, // size
				0.0F, 0.0F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 0, 15);
		
		this.createModel(this, modelList,
				0.0F, 6.0F, 0.0F,
				-3.0F, -8.0F, -3.0F,
				6, 8, 6,
				0.0F, 0.8F, 0.0F,
				textureWidth, textureHeight, 0, 29);
		
		
		
	}
	
}
