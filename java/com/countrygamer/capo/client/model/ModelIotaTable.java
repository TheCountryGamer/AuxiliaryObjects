package com.countrygamer.capo.client.model;

import com.countrygamer.core.Base.client.ModelBase;

/**
 * Created by Country_Gamer on 3/17/14.
 */
public class ModelIotaTable extends ModelBase {

	public ModelIotaTable () {
		super(64, 64);

		// Smooth Head
		this.createModel(this, this.modelList,
				-8.0F, -1.0F, -8.0F,    // origin coords
				0.0F, 0.0F, 0.0F,    // offset coords
				16, 2, 16,            // size
				0.0F, 0.0F, 0.0F,    // rotation
				this.textureWidth, this.textureHeight, 0, 0);

		// Rim 1
		this.createModel(this, this.modelList,
				-8.0F, 0.0F, -8.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				16, 1, 1,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 12, 37);

		// Rim 3
		this.createModel(this, this.modelList,
				-8.0F, 0.0F, 7.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				16, 1, 1,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 12, 37);

		// Rim 2
		this.createModel(this, this.modelList,
				-8.0F, 0.0F, -7.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				1, 1, 14,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 12, 39);

		// Rim 4
		this.createModel(this, this.modelList,
				7.0F, 0.0F, -7.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				1, 1, 14,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 12, 39);

		// Cobble Head
		this.createModel(this, this.modelList,
				-8.0F, -3.0F, -8.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				16, 3, 16,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 0, 18);

		// / Leg 1
		this.createModel(this, this.modelList,
				-8.0F, -6.0F, -8.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				3, 10, 3,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 0, 37);
		// Leg 2
		this.createModel(this, this.modelList,
				-8.0F, -6.0F, 5.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				3, 10, 3,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 0, 37);

		// Leg 3
		this.createModel(this, this.modelList,
				5.0F, -6.0F, -8.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				3, 10, 3,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 0, 37);

		// Leg 4
		this.createModel(this, this.modelList,
				5.0F, -6.0F, 5.0F,	// origin coords
				0.0F, 0.0F, 0.0F,	// offset coords
				3, 10, 3,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 0, 37);

	}

}
