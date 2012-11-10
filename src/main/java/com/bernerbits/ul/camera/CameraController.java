package com.bernerbits.ul.camera;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class CameraController implements AnalogListener, ActionListener {

	private final Camera cam;
	private final Node container;
	private final Spatial model;

	private boolean rotating = false;
	
	private InputManager inputManager;
	
	public CameraController(Camera cam, Spatial model) {
		this.cam = cam;
		this.model = model;
		container = new Node(model.getName() + " Container");
		model.getParent().attachChild(container);
		container.attachChild(model);
	}
	
	private static final float SPEED = 10;
	
	public void bind(InputManager im) {
		im.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		im.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		im.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		im.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		
		im.addMapping("RotLeft", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		im.addMapping("RotRight", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		
		im.addMapping("RotLeft", new MouseAxisTrigger(MouseInput.AXIS_X, true));
		im.addMapping("RotRight", new MouseAxisTrigger(MouseInput.AXIS_X, false));
		
		im.addMapping("RotToggle", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		
		im.addMapping("ZoomIn", new KeyTrigger(KeyInput.KEY_Q));
		im.addMapping("ZoomOut", new KeyTrigger(KeyInput.KEY_Z));
		
		im.addListener(this, "Up", "Down", "Left", "Right", "RotLeft", "RotRight", "RotToggle", "ZoomIn", "ZoomOut");		
		this.inputManager = im;
	}
	
	@Override
	public void onAnalog(String name, float value, float tpf) {
		if(name.equals("Up") || name.equals("Down")) {
			if(!rotating) {
				Vector3f forward = cam.getDirection().clone();
				forward.y = 0;
				forward = forward.normalizeLocal();
				Vector3f location = cam.getLocation();
				if(name.equals("Up")) {
					cam.setLocation(new Vector3f(
							location.x+forward.x*value*SPEED ,
							location.y,
							location.z+forward.z*value*SPEED));
				} else {
					cam.setLocation(new Vector3f(
							location.x-forward.x*value*SPEED,
							location.y,
							location.z-forward.z*value*SPEED));
				} 
			}
		} else if(name.equals("Left") || name.equals("Right")) {
			if(!rotating) {
				Vector3f left = cam.getLeft().clone();
				left.y = 0;
				left = left.normalizeLocal();
				Vector3f location = cam.getLocation();
				if(name.equals("Left")) {
					cam.setLocation(new Vector3f(
							location.x+left.x*value*SPEED,
							location.y,
							location.z+left.z*value*SPEED));
				} else {
					cam.setLocation(new Vector3f(
							location.x-left.x*value*SPEED,
							location.y,
							location.z-left.z*value*SPEED));
				}
			}
		} else if(name.equals("ZoomIn") || name.equals("ZoomOut")) {
			if(!rotating) {
				double scaleAmount;
				if(name.equals("ZoomOut")) {
					scaleAmount = Math.pow(2, value);
				} else {
					scaleAmount = Math.pow(2, -value);
				}
				cam.setFrustumLeft((float)(cam.getFrustumLeft()*scaleAmount));
				cam.setFrustumRight((float)(cam.getFrustumRight()*scaleAmount));
				cam.setFrustumTop((float)(cam.getFrustumTop()*scaleAmount));
				cam.setFrustumBottom((float)(cam.getFrustumBottom()*scaleAmount));
			}
		} else if(name.equals("RotLeft") || name.equals("RotRight")) {
			if(rotating) {
				if(name.equals("RotRight")) {
					value = -value;
				}
				Matrix3f mat = new Matrix3f();
				mat.fromAngleNormalAxis(value, Vector3f.UNIT_Y);
		        Vector3f up = cam.getUp();
		        Vector3f left = cam.getLeft();
		        Vector3f dir = cam.getDirection();

		        mat.mult(up, up);
		        mat.mult(left, left);
		        mat.mult(dir, dir);

		        Quaternion q = new Quaternion();
		        q.fromAxes(left, up, dir);
		        q.normalizeLocal();

		        cam.setAxes(q);
			}
		}
	}
	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if(name.equals("RotToggle")) {
			if(isPressed) {
				rotating = true;
				inputManager.setCursorVisible(false);
			} else {
				rotating = false;
				inputManager.setCursorVisible(true);
			}
		}
	}
}
