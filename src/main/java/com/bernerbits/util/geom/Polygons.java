package com.bernerbits.util.geom;

import java.util.ArrayList;
import java.util.List;

public class Polygons {

	public static Polygon2D newReverseRegularPoly(int x, int y, int radius, int sides) {
		List<Point2D> points = new ArrayList<Point2D>();
		double angleSlice = 2*Math.PI / (sides+1.0);
		double angle = 0;
		for(int i = 0; i <= sides; i++) {
			points.add(new Point2D(
					x+radius*Math.cos(angle),
					x+radius*Math.sin(angle)
			));
			angle += angleSlice;
		}
		return new Polygon2D(points.toArray(new Point2D[0]));
	}

	public static Polygon2D newRegularPoly(int x, int y, int radius, int sides) {
		List<Point2D> points = new ArrayList<Point2D>();
		double angleSlice = 2*Math.PI / (sides+1.0);
		double angle = 0;
		for(int i = 0; i <= sides; i++) {
			points.add(0,new Point2D(
					x+radius*Math.cos(angle),
					x+radius*Math.sin(angle)
			));
			angle += angleSlice;
		}
		return new Polygon2D(points.toArray(new Point2D[0]));
	}

}
