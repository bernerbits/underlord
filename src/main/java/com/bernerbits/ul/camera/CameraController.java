package com.bernerbits.ul.camera;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
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
				forward = container.getLocalRotation().inverse().multLocal(forward);
				if(name.equals("Up")) {
					model.setLocalTranslation(model.getLocalTranslation().add(forward.mult(-value)));
				} else {
					model.setLocalTranslation(model.getLocalTranslation().add(forward.mult(value)));
				} 
			}
		} else if(name.equals("Left") || name.equals("Right")) {
			if(!rotating) {
				Vector3f left = cam.getLeft().clone();
				left.y = 0;
				left = left.normalizeLocal();
				left = container.getLocalRotation().inverse().multLocal(left);
				if(name.equals("Left")) {
					model.setLocalTranslation(model.getLocalTranslation().add(left.mult(-value)));
				} else {
					model.setLocalTranslation(model.getLocalTranslation().add(left.mult(value)));
				}
			}
		} else if(name.equals("ZoomIn") || name.equals("ZoomOut")) {
			if(!rotating) {
				if(name.equals("ZoomIn")) {
					model.scale((float)Math.pow(2, value));
				} else {
					model.scale((float)Math.pow(2, -value));
				}
			}
		} else if(name.equals("RotLeft") || name.equals("RotRight")) {
			if(rotating) {
				if(name.equals("RotLeft")) {
					container.rotate(0, -value, 0);
				} else {
					container.rotate(0, value, 0);
				}
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
