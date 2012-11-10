package com.bernerbits.ul.texture;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public class Texturizer {

	private final AssetManager assetManager;

	public Texturizer(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	public void applyWallTexture(Geometry wallMesh) {
		wallMesh.getMesh().scaleTextureCoordinates(new Vector2f(.2f,.2f)); // TODO Don't hardcode this texture scaling!
		Material wallMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
	    Texture wallTexture = assetManager.loadTexture("wall.jpg");
	    wallTexture.setWrap(WrapMode.Repeat);
	    wallMaterial.setTexture("DiffuseMap", wallTexture);
	    wallMesh.setMaterial(wallMaterial);
	}

	public void applyFloorTexture(Geometry floorMesh) {
		floorMesh.getMesh().scaleTextureCoordinates(new Vector2f(.5f,.5f)); // TODO Don't hardcode this texture scaling!
		Material wallMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
	    Texture wallTexture = assetManager.loadTexture("floor.jpg");
	    wallTexture.setWrap(WrapMode.Repeat);
	    wallMaterial.setTexture("DiffuseMap", wallTexture);
	    floorMesh.setMaterial(wallMaterial);
	}

}
