package com.bernerbits.util.geom;

import java.util.Arrays;

public class SharedVertexTriangle2D {

	private final SharedVertex2D v1;
	private final SharedVertex2D v2;
	private final SharedVertex2D v3;
	
	SharedVertexTriangle2D(SharedVertex2D v1, SharedVertex2D v2, SharedVertex2D v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	public Iterable<SharedVertex2D> getCWVertices() {
		return Arrays.asList(v1,v2,v3);
	}

	public Iterable<SharedVertex2D> getCCWVertices() {
		return Arrays.asList(v1,v3,v2);
	}

}
