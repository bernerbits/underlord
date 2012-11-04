package com.bernerbits.utils.material;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import com.bernerbits.throwaway.lwgl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class DiffuseImageTextureMaterial extends AbstractMaterial {

	private final int textureId;
	
	public DiffuseImageTextureMaterial(String imageResource, boolean alpha) throws IOException, LWJGLException {
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glEnable(GL_COLOR_MATERIAL);
		
		textureId = Texture.loadTexture(imageResource, true);
	}

	@Override
	public void apply() {
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		glBindTexture(GL_TEXTURE_2D, textureId);
	}

}
