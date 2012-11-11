package com.bernerbits.throwaway.jmonkey;

import java.util.Arrays;

import com.bernerbits.ul.camera.CameraController;
import com.bernerbits.ul.mesh.DungeonGenerator;
import com.bernerbits.ul.mesh.UnderlordGenerator;
import com.bernerbits.ul.texture.Texturizer;
import com.bernerbits.util.geom.Polygon2D;
import com.bernerbits.util.geom.Polygons;
import com.jme3.animation.AnimControl;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.BasicShadowRenderer;

public class Monkey1 extends SimpleApplication {

	public static void main(String[] args) { 
		new Monkey1().start();
	}
	
	private Spatial dungeon;
	
	private Node minion;
	private CharacterControl minionControl;
	private BulletAppState bulletAppState;
	
	private CapsuleCollisionShape minionShape;
	
	private Polygon2D dungeonPoly = Polygons.newRegularPoly(0,0,5,20);
	private Polygon2D holePoly = Polygons.newReverseRegularPoly(-1,-1,1,20);
	
	@Override
	public void simpleInitApp() {
		// World
		Node world = new Node();
		rootNode.attachChild(world);
		
		// Dungeon
		dungeonPoly = Polygons.newRegularPoly(0,0,5,20);
		holePoly = Polygons.newReverseRegularPoly(-1,-1,1,20);
		DungeonGenerator generator = new DungeonGenerator("Test Dungeon", Arrays.asList(dungeonPoly, holePoly), new Texturizer(assetManager));
		dungeon = generator.createDungeon();
		
		CollisionShape dungeonShape = CollisionShapeFactory.createMeshShape((Node) dungeon);
		RigidBodyControl dungeonBody = new RigidBodyControl(dungeonShape,0);
		dungeon.addControl(dungeonBody);
		world.attachChild(dungeon);
		
		// Underlord
		Spatial underlord = new UnderlordGenerator(assetManager).createUnderlord();
		underlord.setLocalTranslation(0,0.5f,0);
		world.attachChild(underlord);
		
		// Minion
		minion = (Node) assetManager.loadModel("Models/Sinbad/Sinbad.mesh.xml");
		minion.setLocalScale(.1f);
		minion.setLocalTranslation(1,4.5f,1);
		
		minion.getControl(SkeletonControl.class).getAttachmentsNode("Handle.R").attachChild((Node)assetManager.loadModel("Models/Sinbad/Sword.mesh.xml"));
		minion.getControl(AnimControl.class).createChannel().setAnim("RunBase");
		minion.getControl(AnimControl.class).createChannel().setAnim("RunTop");
		minion.getControl(AnimControl.class).createChannel().setAnim("HandsClosed");
		
		minion.setShadowMode(ShadowMode.CastAndReceive);
		
		minionShape = new CapsuleCollisionShape(.3f,.5f,1);
		minionControl = new CharacterControl(minionShape, 0.05f);
		minionControl.setGravity(30);
		minionControl.setFallSpeed(30);
		minionControl.setUseViewDirection(true);
		minionControl.setViewDirection(new Vector3f(0,0,1));
		minionControl.setWalkDirection(new Vector3f(0,0,.03f));
		minionControl.setPhysicsLocation(new Vector3f(1,4.5f,1));
		minion.addControl(minionControl);
		
		world.attachChild(minion);
		
		// Lights
		AmbientLight ambient = new AmbientLight();
		ambient.setColor(new ColorRGBA(1.8f,1.35f,.9f,1));
		world.addLight(ambient);
	    
	    DirectionalLight sun = new DirectionalLight();
	    sun.setDirection(new Vector3f(1,-1,1).normalizeLocal());
	    sun.setColor(ColorRGBA.White);
	    world.addLight(sun);
	    
	    BasicShadowRenderer shadow = new BasicShadowRenderer(assetManager,4096);
	    shadow.setDirection(new Vector3f(-1,-1,-1).normalizeLocal());
	    viewPort.addProcessor(shadow);
	    
	    // Camera
	    flyCam.setEnabled(false); // Disable fly-by controls
		cam.setLocation(new Vector3f(1,1,-1));
		cam.setFrustum(-1000f,1000f,-7.5f,7.5f,6.5f,-6.5f);
		cam.setAxes(new Vector3f(1,0,0), new Vector3f(0,1,0), new Vector3f(0,0,1));
		// Camera rotation obtained from debug. I don't grok Quaternions yet, so I have no idea why they work!!
		cam.setRotation(new Quaternion(0.27080652f, -0.3579396f, 0.08276218f, 0.8897716f));  
		cam.setParallelProjection(true);
		new CameraController(cam, world).bind(inputManager);
		
		// Physics
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		//bulletAppState.getPhysicsSpace().enableDebug(assetManager);
		bulletAppState.getPhysicsSpace().add(dungeonBody);
		bulletAppState.getPhysicsSpace().add(minionControl);		
	}

	float lastUpdate = timer.getTimeInSeconds();
	
	@Override
	public void simpleUpdate(float tpf) {
		if(timer.getTimeInSeconds() - lastUpdate > 1) {
			lastUpdate += 1;
			Vector3f walk = minionControl.getWalkDirection();
			minionControl.setWalkDirection(new Vector3f(-walk.z,walk.y,walk.x));
			minionControl.setViewDirection(minionControl.getWalkDirection().normalize());
		}
	}
}
