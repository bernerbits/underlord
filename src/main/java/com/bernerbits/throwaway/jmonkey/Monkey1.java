package com.bernerbits.throwaway.jmonkey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

import com.bernerbits.util.geom.Polygon2D;
import com.bernerbits.util.geom.Polygon2DNode;
import com.bernerbits.util.geom.Polygons;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;

public class Monkey1 extends SimpleApplication {

	public static void main(String[] args) {
		new Monkey1().start();
	}

	private List<PolygonPoint> getPoints(Polygon2D walls) {
		List<PolygonPoint> points = new ArrayList<>();
		for(Polygon2DNode node : walls.getNodes()) {
			points.add(new PolygonPoint(node.getX(), node.getY()));
		}
		return points;
	}
	
	@Override
	public void simpleInitApp() {
		Polygon2D dungeonPoly = Polygons.newRegularPoly(0,0,5,7);

		Polygon2D holePoly = Polygons.newReverseRegularPoly(-1,-1,1,5);
		
		Node dungeon = new Node();
		
		int wallIndex = 0;
		for(Polygon2D wallPoly : Arrays.asList(dungeonPoly,holePoly)) {
			wallIndex++;
			Mesh wall = new Mesh();
			
			List<Vector3f> vertices = new ArrayList<>();
			List<Vector2f> texcoords = new ArrayList<>();
			List<Vector3f> normals = new ArrayList<>();
			List<Integer> indices = new ArrayList<>();
			
			wall.setMode(Mesh.Mode.TriangleStrip);
			int index = 0;
			for(Polygon2DNode node : wallPoly.getNodes()) {
				indices.add(index);
				vertices.add(new Vector3f((float)node.getX(),0,(float)node.getY()));
				texcoords.add(new Vector2f((float)node.getTextureCoordinate(),0));
				normals.add(new Vector3f((float)node.getNormalX(),0,(float)node.getNormalY()));
				index++;
	
				indices.add(index);
				vertices.add(new Vector3f((float)node.getX(),1,(float)node.getY()));
				texcoords.add(new Vector2f((float)node.getTextureCoordinate(),1));
				normals.add(new Vector3f((float)node.getNormalX(),0,(float)node.getNormalY()));
				index++;
			}
			indices.add(0);
			indices.add(1);
			
			wall.setBuffer(VertexBuffer.Type.Position,3,BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[0])));
			wall.setBuffer(VertexBuffer.Type.TexCoord,2,BufferUtils.createFloatBuffer(texcoords.toArray(new Vector2f[0])));
			wall.setBuffer(VertexBuffer.Type.Normal,3,BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[0])));
			wall.setBuffer(VertexBuffer.Type.Index,1,BufferUtils.createIntBuffer(ArrayUtils.toPrimitive(indices.toArray(new Integer[0]))));
			
			wall.scaleTextureCoordinates(new Vector2f(.2f,.2f));
			wall.updateBound();
			Geometry wallGeom = new Geometry("Wall" + wallIndex,wall);
			
		    Material wallMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		    Texture wallTexture = assetManager.loadTexture("wall.jpg");
		    wallTexture.setWrap(WrapMode.Repeat);
		    wallMaterial.setTexture("DiffuseMap", wallTexture);
		    wallGeom.setMaterial(wallMaterial);
			
			dungeon.attachChild(wallGeom);
		}
		
		
		{
			Mesh floor = new Mesh();
			floor.setMode(Mesh.Mode.Triangles);
			
			List<Vector3f> vertices = new ArrayList<>();
			List<Vector2f> texcoords = new ArrayList<>();
			List<Vector3f> normals = new ArrayList<>();
			List<Integer> indices = new ArrayList<>();
			
			Polygon p = new Polygon(getPoints(dungeonPoly));
			p.addHole(new Polygon(getPoints(holePoly)));
			Poly2Tri.triangulate(p);
			
			int vertexIndex = 0;
			for(DelaunayTriangle tri : p.getTriangles()) {
				for(TriangulationPoint point : new TriangulationPoint[]{tri.points[0],
						tri.pointCW(tri.points[0]),
						tri.pointCCW(tri.points[0])}) {
					Vector2f vertex = new Vector2f(point.getXf(),point.getYf()); 
					vertices.add(new Vector3f(point.getXf(),0,point.getYf()));
					texcoords.add(vertex);
					normals.add(new Vector3f(0,1,0));
					indices.add(vertexIndex++);
				}				
			}
	
			floor.setBuffer(VertexBuffer.Type.Position,3,BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[0])));
			floor.setBuffer(VertexBuffer.Type.TexCoord,2,BufferUtils.createFloatBuffer(texcoords.toArray(new Vector2f[0])));
			floor.setBuffer(VertexBuffer.Type.Normal,3,BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[0])));
			floor.setBuffer(VertexBuffer.Type.Index,1,BufferUtils.createIntBuffer(ArrayUtils.toPrimitive(indices.toArray(new Integer[0]))));
			
			floor.scaleTextureCoordinates(new Vector2f(.5f,.5f));
			floor.updateBound();
			Geometry floorGeom = new Geometry("Floor",floor);
			
		    Material floorMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		    Texture floorTexture = assetManager.loadTexture("floor.jpg");
		    floorTexture.setWrap(WrapMode.Repeat);
		    floorMaterial.setTexture("DiffuseMap", floorTexture);
		    floorGeom.setMaterial(floorMaterial);
		    
		    dungeon.attachChild(floorGeom);
		}
		
		dungeon.setLocalScale(.1f);

		AmbientLight ambient = new AmbientLight();
		ambient.setColor(new ColorRGBA(1.8f,1.8f,1.8f,1));
		rootNode.addLight(ambient);
	    
	    DirectionalLight sun = new DirectionalLight();
	    sun.setDirection(new Vector3f(1,-1,1).normalizeLocal());
	    sun.setColor(ColorRGBA.White);
	    rootNode.addLight(sun);
	    
		rootNode.attachChild(dungeon);
		
		cam.setLocation(new Vector3f(1,1,-1));
		cam.setAxes(new Vector3f(1,0,0), new Vector3f(0,1,0), new Vector3f(0,0,1));
		// Camera rotation obtained from debug. I don't grok Quaternions yet, so I have no idea why they work!!
		cam.setRotation(new Quaternion(0.27080652f, -0.3579396f, 0.08276218f, 0.8897716f));  
		
		cam.setParallelProjection(true);
	}

}
