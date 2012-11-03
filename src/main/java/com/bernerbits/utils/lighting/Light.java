package com.bernerbits.utils.lighting;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


import static org.lwjgl.opengl.GL11.*;

/**
 * Wrapper class for OpenGL lighting calls. Encapsulates light IDs, removing the need for
 * hard-coded light IDs in application code. Management of instances and light IDs is performed by
 * LightBank. This class should never be instantiated directly in application code.
 * 
 * If performing local lighting, you should release() the instance when you are done rendering
 * objects illuminated by this light. After calling release(), any additional calls to this
 * instance will have unpredictable effects, so you should also release any references to it.
 * 
 * The following code will ensure that a light is never held longer than needed:
 * 
 * <code>Light l = null;
 * try {
 *   l = lightBank.acquire(); 
 * } finally {
 *   if(l != null) {
 *     l.release();
 *   }
 * }</code>
 * 
 * @author derekberner
 */
public final class Light {
	
	private final LightBank bank;
	private final int lightId;
	
	/* package */ Light(LightBank bank, int i) {
		this.bank = bank;
		this.lightId = i;
	}
	
	public void enable() {
		GL11.glEnable(lightId);
	}
	
	public void disable() {
		GL11.glDisable(lightId);
	}
	
	public void release() {
		bank.release(this);
	}

	public void setAmbientColor(double r, double g, double b) {
		FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		ambient.put(new float[]{(float)r,(float)g,(float)b,1}).flip(); 
		glLight(lightId, GL_AMBIENT, ambient);
	}

	public void setDiffuseColor(double r, double g, double b) {
		FloatBuffer diffuse = BufferUtils.createFloatBuffer(4);
		diffuse.put(new float[]{(float)r,(float)g,(float)b,1}).flip(); 
		glLight(lightId, GL_DIFFUSE, diffuse);
	}

	// When fourth element of position vector is 0, it acts as a directional light  
	public void setDirection(double x, double y, double z) {
		FloatBuffer direction = BufferUtils.createFloatBuffer(4);
		direction.put(new float[]{(float)x,(float)y,(float)z,0}).flip(); 
		glLight(lightId, GL_POSITION, direction);
	}
	
	// When fourth element of position vector is 1, it acts as a point light  
	public void setPosition(double x, double y, double z) {
		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(new float[]{(float)x,(float)y,(float)z,1}).flip(); 
		glLight(lightId, GL_POSITION, position);
	}
	
}
