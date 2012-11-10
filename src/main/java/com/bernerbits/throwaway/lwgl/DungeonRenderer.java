package com.bernerbits.throwaway.lwgl;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.geometry.polygon.PolygonSet;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

import com.bernerbits.util.geom.Polygon2D;
import com.bernerbits.util.geom.Polygon2DNode;
import com.bernerbits.utils.material.Material;

public class DungeonRenderer {
	
	private final Material wallMat;
	private final Material floorMat;
	private final List<Polygon2D> walls;
	private List<DelaunayTriangle> floor;
	
	public DungeonRenderer(Material wallMat, Material floorMat, List<Polygon2D> walls) {
		this.wallMat = wallMat;
		this.floorMat = floorMat;
		this.walls = walls;
		
		triangulateFloor();
		//calculateExtents();
	}

	private void triangulateFloor() {
		Polygon p = null;
		for(Polygon2D poly : walls) {
			if(p == null) {
				p = new Polygon(getPoints(poly));
			} else {
				p.addHole(new Polygon(getPoints(poly)));
			}
		}
		Poly2Tri.triangulate(p);
		floor = p.getTriangles();
	}

	private List<PolygonPoint> getPoints(Polygon2D walls) {
		List<PolygonPoint> points = new ArrayList<PolygonPoint>();
		for(Polygon2DNode node : walls.getNodes()) {
			points.add(new PolygonPoint(node.getX(), node.getY()));
		}
		return points;
	}

	public void init() {
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_CULL_FACE);
		glEnable(GL_NORMALIZE);
	}
	
	public void render() {
		// Mask floor
		floorMat.apply();
		glBegin(GL_TRIANGLES);
		glNormal3i(0,1,0);
		for(DelaunayTriangle tri : floor) {
			TriangulationPoint point = tri.points[0];
			glTexCoord2d(point.getX() / 80, point.getY() / 80);
			glVertex3d(point.getX(),0,point.getY());
			
			point = tri.pointCW(point);
			glTexCoord2d(point.getX() / 80, point.getY() / 80);
			glVertex3d(point.getX(),0,point.getY());
			
			point = tri.pointCW(point);
			glTexCoord2d(point.getX() / 80, point.getY() / 80);
			glVertex3d(point.getX(),0,point.getY());
		}
		glEnd();
		
		// Draw walls
		wallMat.apply();
		for(Polygon2D poly : walls) {
			glBegin(GL_QUAD_STRIP);
			for(Polygon2DNode node : poly.getClosedNodes()) {
				glNormal3d(node.getNormalX(),0,node.getNormalY());
				glTexCoord2d(node.getTextureCoordinate() / 80, 0); glVertex3d(node.getX(),0,node.getY());
				glTexCoord2d(node.getTextureCoordinate() / 80, 1); glVertex3d(node.getX(),80,node.getY());
			}
			glEnd();
		}
	}
}
