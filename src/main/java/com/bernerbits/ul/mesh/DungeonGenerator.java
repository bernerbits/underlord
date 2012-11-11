package com.bernerbits.ul.mesh;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

import com.bernerbits.ul.texture.Texturizer;
import com.bernerbits.util.geom.Polygon2D;
import com.bernerbits.util.geom.Polygon2DNode;
import com.bernerbits.util.geom.SharedVertex2D;
import com.bernerbits.util.geom.SharedVertexTriangle2D;
import com.bernerbits.util.geom.SharedVertexTriangleSet2D;
import com.bernerbits.util.geom.Tesselator;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;

public class DungeonGenerator {
	
	private final Texturizer texturizer;
	private final String name;
	private List<Polygon2D> walls;
	
	public DungeonGenerator(String name, List<Polygon2D> walls, Texturizer texturizer) {
		this.name = name;
		this.texturizer = texturizer;
		this.walls = walls;
	}
	
	public Spatial createDungeon() {
		Node dungeonNode = new Node(name);
		
		Node wallsNode = new Node(name + " Walls");
		
		int wallIndex = 1;
		for(Polygon2D wall : walls) {
			Geometry wallMesh = generateWall(wall,wallIndex++);
			texturizer.applyWallTexture(wallMesh);
			wallsNode.attachChild(wallMesh);
		}
		
		dungeonNode.attachChild(wallsNode);
		
		Geometry floorMesh = generateFloor(walls);
		texturizer.applyFloorTexture(floorMesh);
		dungeonNode.attachChild(floorMesh);
		
		return dungeonNode;
	}

	private Geometry generateFloor(List<Polygon2D> floorPolys) {
		SharedVertexTriangleSet2D triangles = Tesselator.tesselate(floorPolys);
		
		Mesh floor = new Mesh();
		floor.setMode(Mesh.Mode.Triangles);
		//floor.setMode(Mesh.Mode.Lines);
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texcoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		for(SharedVertex2D v : triangles.getVertices()) {
			vertices.add(new Vector3f(v.getX(),0,v.getY()));
			texcoords.add(new Vector2f(v.getX(),v.getY()));
			normals.add(new Vector3f(0,1,0));
		}
		for(SharedVertexTriangle2D tri : triangles.getTriangles()) {
			//SharedVertex2D last = null;
			//SharedVertex2D first = null;
			for(SharedVertex2D v : tri.getCWVertices()) {
				//if(last != null) {
				//	indices.add(last.getIndex());
					indices.add(v.getIndex());
				//} else {
				//	first = v;
				//}
				//last = v;
			}
			//indices.add(last.getIndex());
			//indices.add(first.getIndex());
		}
		
		floor.setBuffer(VertexBuffer.Type.Position,3,BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[0])));
		floor.setBuffer(VertexBuffer.Type.TexCoord,2,BufferUtils.createFloatBuffer(texcoords.toArray(new Vector2f[0])));
		floor.setBuffer(VertexBuffer.Type.Normal,3,BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[0])));
		floor.setBuffer(VertexBuffer.Type.Index,1,BufferUtils.createIntBuffer(ArrayUtils.toPrimitive(indices.toArray(new Integer[0]))));
		
		floor.updateBound();
		Geometry floorGeom = new Geometry(name + " Floor",floor);
		floorGeom.setShadowMode(ShadowMode.Receive);
		return floorGeom;
	}

	private Geometry generateWall(Polygon2D wallPoly, int wallIndex) {
		Mesh wall = new Mesh();
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texcoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		wall.setMode(Mesh.Mode.TriangleStrip);
		//wall.setMode(Mesh.Mode.LineStrip);
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
		
		wall.updateBound();
		Geometry wallGeom = new Geometry(name + " Wall " + wallIndex,wall);
		wallGeom.setShadowMode(ShadowMode.CastAndReceive);
		return wallGeom;
	}
	
	
}
