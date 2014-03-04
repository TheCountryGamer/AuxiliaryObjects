package com.countrygamer.auxiliaryobjects.client.render;

import com.countrygamer.core.client.block.tiles.ModelBase;

public class ModelCore extends ModelBase {

	public ModelCore() {
		super(32, 32);

		this.createModel(this, this.modelList,
				0.0F, 4.0F, 0.0F,	// origin coords
				-2.5F, -2.5F, -2.5F,	// offset coords
				5, 5, 5,			// size
				0.0F, 0.0F, 0.0F,	// rotation
				this.textureWidth, this.textureHeight, 0, 0);

	}
	
	@Override
	public void renderModel(float f5) {
		//this.setRotationAngles(0, 0, 0, 0, 0, f5);
		super.renderModel(f5);	
	}
	
	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4,
			float f5) {
		this.modelList.get(0).rotateAngleY += 0.05F;
		this.modelList.get(0).rotateAngleX += 0.01F;
		this.modelList.get(0).rotateAngleZ += 0.01F;

	}

}
