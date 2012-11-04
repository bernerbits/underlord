package com.bernerbits.utils.material;

import static org.lwjgl.opengl.GL11.*;

public class ScaledMaterial extends AbstractMaterial {

	private final Material target;
	private final double x;
	private final double y;
	
	public ScaledMaterial(Material target, double x, double y) {
		this.target = target;
		this.x = x;
		this.y = y;
	}

	@Override
	public void apply() {
		glMatrixMode(GL_TEXTURE);
		glLoadIdentity();
		glScaled(1/x, 1/y, 1);
		
		target.apply();
		
		glMatrixMode(GL_MODELVIEW);
	}

	@Override
	public Material scale(double x, double y) {
		return new ScaledMaterial(target,this.x*x,this.y*y);
	}

}
