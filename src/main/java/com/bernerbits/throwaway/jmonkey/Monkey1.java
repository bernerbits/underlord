package com.bernerbits.throwaway.jmonkey;

import java.util.Arrays;

import com.bernerbits.ul.mesh.DungeonGenerator;
import com.bernerbits.ul.texture.Texturizer;
import com.bernerbits.util.geom.Polygon2D;
import com.bernerbits.util.geom.Polygons;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class Monkey1 extends SimpleApplication {

	public static void main(String[] args) { 
		new Monkey1().start();
	}

	@Override
	public void simpleInitApp() {
		Polygon2D dungeonPoly = Polygons.newRegularPoly(0,0,5,7);

		Polygon2D holePoly = Polygons.newReverseRegularPoly(-1,-1,1,5);
		
		DungeonGenerator generator = new DungeonGenerator("Test Dungeon", Arrays.asList(dungeonPoly, holePoly), new Texturizer(assetManager));
		
		Spatial dungeon = generator.createDungeon();
				
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
