package com.bernerbits.utils.lighting;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lwjgl.opengl.GL11;


/**
 * Helper class for OpenGL lights. Since OpenGL only supports 8 lights at a time, this class 
 * provides a mechanism for checking active lights in and out, avoiding some problems related to 
 * hard coding and coordinating light IDs.
 * 
 * If too many lights are checked out at once, an IllegalStateException is thrown.
 * 
 * Intended usage is to acquire a light when rendering nearby objects and then release it.
 * It is also possible to permanently hold a reference to a light, for global lighting effects.
 * 
 * OpenGL's lights are global, so this class uses the singleton pattern to avoid unpredictable
 * effects. While the singleton logic is thread safe, the instance itself is not. All OpenGL
 * calls should be serialized in a dedicated presentation-layer thread.
 * 
 * @author derekberner
 */
public class LightBank {

	/* singleton pattern */
	private static volatile LightBank instance = null;

	private LightBank() {}

	public static LightBank getInstance() {
		if (instance == null) {
			synchronized (LightBank.class){
				if (instance == null) {
					instance = new LightBank();
				}
			}
		}
		return instance;
	}

	/* private members */
	private final Set<Light> allLights = new HashSet<Light>();
	private final Set<Light> lights = new HashSet<Light>();
	{
		for(int i = 0; i < 8; i++) {
			Light light = new Light(this,GL11.GL_LIGHT0 + i);
			lights.add(light);
			allLights.add(light);
		}
	}

	public Light acquire() {
		Iterator<Light> lightIterator = lights.iterator();
		if(!lightIterator.hasNext()) {
			throw new IllegalStateException("No lights left");
		}
		Light light = lightIterator.next();
		lightIterator.remove();
		light.enable();
		return light;
	}

	public void enable() {
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	public void disable() {
		GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	/* package */ void release(Light light) {
		if(!allLights.contains(light)) {
			throw new IllegalArgumentException("Light was not produced by this bank");
		}
		light.disable();
		lights.add(light);
	}

}
