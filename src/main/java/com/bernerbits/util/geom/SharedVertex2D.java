package com.bernerbits.util.geom;

public class SharedVertex2D {

	private final float x;
	private final float y;
	private final int index;
	
	SharedVertex2D(float x, float y, int index) {
		this.x = x;
		this.y = y;
		this.index = index;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int getIndex() {
		return index;
	}

}
