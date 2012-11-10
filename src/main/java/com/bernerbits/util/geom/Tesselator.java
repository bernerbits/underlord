package com.bernerbits.util.geom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

public final class Tesselator {

	public static SharedVertexTriangleSet2D tesselate(List<Polygon2D> allPolys) {
		List<Polygon2D> polys = new LinkedList<Polygon2D>();
		List<Polygon2D> holes = new LinkedList<Polygon2D>();
		
		for(Polygon2D poly : allPolys) {
			if(poly.isClockwise()) {
				polys.add(poly);
			} else {
				holes.add(poly);
			}
		}
		
		Map<TriangulationPoint, SharedVertex2D> pointToVertex = new HashMap<TriangulationPoint, SharedVertex2D>();
		List<SharedVertex2D> vertices = new ArrayList<SharedVertex2D>();
		List<SharedVertexTriangle2D> triangles = new ArrayList<SharedVertexTriangle2D>();
		int vertexIndex = 0;
		
		for(Polygon2D poly : polys) {
			Polygon p = new Polygon(getPoints(poly));
			for(Polygon2D hole : holes) {
				p.addHole(new Polygon(getPoints(hole)));
			}
			Poly2Tri.triangulate(p);
			
			for(DelaunayTriangle tri : p.getTriangles()) {
				for(TriangulationPoint point : tri.points) {
					if(!pointToVertex.containsKey(point)) {
						SharedVertex2D vertex = new SharedVertex2D(point.getXf(),point.getYf(),vertexIndex++);
						pointToVertex.put(point, vertex);
						vertices.add(vertex);
					}
				}
				
				TriangulationPoint point = tri.points[0];
				SharedVertex2D v1 = pointToVertex.get(point);
				point = tri.pointCW(point);
				SharedVertex2D v2 = pointToVertex.get(point);
				point = tri.pointCW(point);
				SharedVertex2D v3 = pointToVertex.get(point);
				
				triangles.add(new SharedVertexTriangle2D(v1, v2, v3));
			}
		}
		
		return new SharedVertexTriangleSet2D(vertices, triangles);
	}

	private static List<PolygonPoint> getPoints(Polygon2D walls) {
		List<PolygonPoint> points = new ArrayList<PolygonPoint>();
		for(Polygon2DNode node : walls.getNodes()) {
			points.add(new PolygonPoint(node.getX(), node.getY()));
		}
		return points;
	}
	
}
