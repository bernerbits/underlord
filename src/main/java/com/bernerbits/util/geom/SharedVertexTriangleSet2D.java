package com.bernerbits.util.geom;

import java.util.Collections;
import java.util.List;

public class SharedVertexTriangleSet2D {

	private final List<SharedVertex2D> vertices;
	private final List<SharedVertexTriangle2D> triangles;
	
	SharedVertexTriangleSet2D(List<SharedVertex2D> vertices, List<SharedVertexTriangle2D> triangles) {
		this.vertices = vertices;
		this.triangles = triangles;
	}

	public Iterable<SharedVertex2D> getVertices() {
		return Collections.unmodifiableCollection(vertices);
	}

	public Iterable<SharedVertexTriangle2D> getTriangles() {
		return Collections.unmodifiableCollection(triangles);
	}

}
