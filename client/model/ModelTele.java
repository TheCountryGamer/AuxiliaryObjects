package com.countrygamer.capo.client.model;

import com.countrygamer.core.Base.client.ModelBase;

public class ModelTele extends ModelBase {

	public ModelTele() {
		super(64, 64);

		// Base
		this.createModel(this, this.modelList,
				0.0F, 0.0F, 0.0F,	// origin coords
				-5.0F, 0.0F, -5.0F,	// offset coords
				10, 16, 10,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 0, 0);

	}

}