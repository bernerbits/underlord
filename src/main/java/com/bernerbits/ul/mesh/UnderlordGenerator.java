package com.bernerbits.ul.mesh;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class UnderlordGenerator {

	private final AssetManager assetManager;

	public UnderlordGenerator(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public Spatial createUnderlord() {
		Node underlord = new Node("Underlord");
		
		Material flash_mat = new Material(
				assetManager, "Common/MatDefs/Misc/Particle.j3md");
		flash_mat.setTexture("Texture",
				assetManager.loadTexture("Effects/Explosion/flash.png"));

		ParticleEmitter flash1 = new ParticleEmitter("underlord purple", ParticleMesh.Type.Triangle, 7);
		flash1.setParticlesPerSec(7);
		flash1.setRandomAngle(true);
		flash1.setMaterial(flash_mat);
		flash1.setImagesX(2); // columns
		flash1.setImagesY(2); // rows
		flash1.setSelectRandomImage(true);
		flash1.setGravity(0,0,0);
		flash1.setEndSize(0.1f);
		flash1.setStartSize(0.5f);
		flash1.setHighLife(0.7f);
		flash1.setLowLife(0.4f);
		flash1.setStartColor(new ColorRGBA(0.5f,0,1f,0.5f));
		flash1.setEndColor(new ColorRGBA(1,1,1,0.5f));

		ParticleEmitter flash2 = new ParticleEmitter("underlord red", ParticleMesh.Type.Triangle, 7);
		flash2.setParticlesPerSec(6);
		flash2.setRandomAngle(true);
		flash2.setMaterial(flash_mat);
		flash2.setImagesX(2); // columns
		flash2.setImagesY(2); // rows
		flash2.setSelectRandomImage(true);
		flash2.setGravity(0,0,0);
		flash2.setEndSize(0.1f);
		flash2.setStartSize(0.5f);
		flash2.setHighLife(0.7f);
		flash2.setLowLife(0.4f);
		flash2.setStartColor(new ColorRGBA(1f,0.25f,0.25f,0.5f));
		flash2.setEndColor(new ColorRGBA(1,1,1,0.5f));

		underlord.attachChild(flash1);
		underlord.attachChild(flash2);
		underlord.setShadowMode(ShadowMode.Off);
		return underlord;
	}


}
