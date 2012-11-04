package com.bernerbits.utils.material;

public abstract class AbstractMaterial implements Material {
	
	public Material scale(double x, double y) {
		return new ScaledMaterial(this,x,y);
	};
	
}
