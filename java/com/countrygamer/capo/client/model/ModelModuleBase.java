package com.countrygamer.capo.client.model;

import com.countrygamer.core.Base.client.ModelBase;

public class ModelModuleBase extends ModelBase {

	public ModelModuleBase() {
		super(64, 64);
		
		this.createModel(this, modelList,
				0.0F, 2.0F, 0.0F,
				-7.0F, 0.0F, -7.0F,
				14, 1, 14,
				0.0F, 0.0F, 0.0F,
				textureWidth, textureHeight, 0, 0);

		this.createModel(this, modelList,
				0.0F, 1.0F, 0.0F,
				-8.0F, 0.0F, -8.0F,
				16, 1, 16,
				0.0F, 0.0F, 0.0F,
				textureWidth, textureHeight, 0, 15);
		
	}
	
}
