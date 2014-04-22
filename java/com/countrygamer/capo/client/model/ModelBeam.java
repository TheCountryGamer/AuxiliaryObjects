package com.countrygamer.capo.client.model;

import com.countrygamer.core.Base.client.ModelBase;

public class ModelBeam extends ModelBase {
	
	public ModelBeam(float orX, float orY, float orZ, float rotation) {
		super(64, 64);
		
		int size = 3;
		this.createModel(this, this.modelList,
				orX, orY, orZ, // origin coords
				-((float)size / 2.0F), -((float)size / 2.0F), -((float)size / 2.0F), // offset coords
				size, size, size, // size
				0.0F, 0.75F, 0.0F, // rotation
				this.textureWidth, this.textureHeight, 0, 0);
		
	}
	
}
