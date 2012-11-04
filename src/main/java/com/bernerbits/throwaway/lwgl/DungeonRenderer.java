package com.bernerbits.throwaway.lwgl;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import com.bernerbits.util.geom.Polygon2D;
import com.bernerbits.util.geom.Polygon2DNode;
import com.bernerbits.utils.material.Material;

public class DungeonRenderer {
	
	private final Material wallMat;
	private final Material floorMat;
	private final List<Polygon2D> walls;
	
	private double maxX;
	private double minX;
	private double maxZ;
	private double minZ;
	
	public DungeonRenderer(Material wallMat, Material floorMat, List<Polygon2D> walls) {
		this.wallMat = wallMat;
		this.floorMat = floorMat;
		this.walls = walls;
		calculateExtents();
	}

	private void calculateExtents() {
		maxX = Double.NEGATIVE_INFINITY;
		minX = Double.POSITIVE_INFINITY;
		maxZ = Double.NEGATIVE_INFINITY;
		minZ = Double.POSITIVE_INFINITY;
		
		for(Polygon2D poly : walls) {
			if(poly.getMaxX() > maxX) {
				maxX = poly.getMaxX();
			}
			if(poly.getMinX() < minX) {
				minX = poly.getMinX();
			}
			if(poly.getMaxY() > maxZ) {
				maxZ = poly.getMaxY();
			}
			if(poly.getMinY() < minZ) {
				minZ = poly.getMinY();
			}
		}
	}
	
	public void init() {
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_CULL_FACE);
		glEnable(GL_NORMALIZE);
	}
	
	public void render() {
		// Mask floor
		glEnable(GL_STENCIL_TEST);
		glStencilMask(0xFF);
		
		glDepthMask(false);
		glColorMask(false,false,false,false);
		
		glClearStencil(0);
		glClear(GL_STENCIL_BUFFER_BIT);
		glStencilFunc(GL_ALWAYS, 1, 0xFF);
		glStencilOp(GL_INVERT,GL_INVERT,GL_INVERT);
		
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		for(Polygon2D poly : walls) {
			glBegin(GL_POLYGON);
			for(Polygon2DNode node : poly.getNodes()) {
				glVertex3d(node.getX(),0,node.getY());
			}
			glEnd();
		}
		glEnable(GL_CULL_FACE);
		
		// Draw floor as squares, using calculated extents and stencil buffer as mask
		glDepthMask(true);
		glColorMask(true,true,true,true);
		glStencilMask(0);
		
		glEnable(GL_STENCIL_TEST);
		glStencilFunc(GL_EQUAL, 1, 1);
		glStencilOp(GL_KEEP,GL_KEEP,GL_KEEP);	
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		
		floorMat.apply();
		for(double x = minX; x < maxX+80; x += 80) {
			glBegin(GL_QUAD_STRIP);
			for(double z = minZ; z < maxZ+80; z += 80) {
				glNormal3i(0, 1, 0); 
				glTexCoord2d(x/80+1, z/80); glVertex3d(x+80,0,z);
				glTexCoord2d(x/80, z/80); glVertex3d(x,0,z);
			}	
			glEnd();
		}
		
		glDisable(GL_STENCIL_TEST);
		glDisable(GL_STENCIL);
		
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
