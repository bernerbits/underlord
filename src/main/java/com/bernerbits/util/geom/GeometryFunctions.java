package com.bernerbits.util.geom;

public final class GeometryFunctions {
	private GeometryFunctions() {}
	
	public static Point2D triangleBarycenter(Point2D p1, Point2D p2, Point2D p3) {
		return new Point2D(
			(p1.getX() + p2.getX() + p3.getX()) / 3,
			(p1.getY() + p2.getY() + p3.getY()) / 3
		);
	}
}
