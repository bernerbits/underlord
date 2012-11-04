package com.bernerbits.throwaway.lwgl;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.Arrays;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import com.bernerbits.util.geom.Point2D;
import com.bernerbits.util.geom.Polygon2D;
import com.bernerbits.util.geom.Polygons;
import com.bernerbits.utils.lighting.Light;
import com.bernerbits.utils.lighting.LightBank;
import com.bernerbits.utils.material.DiffuseImageTextureMaterial;
import com.bernerbits.utils.material.Material;

public class LWGL1 {

	private static float t = 0;
	private static float angle = 0;
	//private static int x = 400, y = 300;
	
	private static LightBank lightBank = LightBank.getInstance();
	private static Light light;
	private static DungeonRenderer dungeonRenderer;
	
	public static void main(String[] args) throws LWJGLException, IOException {
		Display.setLocation(100, 100);
		Display.setTitle("Hello, World!");
		Display.setDisplayMode(new DisplayMode(800,600));
		Display.create(new PixelFormat(8, 24, 8));
		
		glViewport(0, 0, 800, 600);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glOrtho(0, 800, 0, 600, -100000, 100000);
		glRotated(45, 1, 0, 0);
		glRotated(45, 0, 1, 0);

		glMatrixMode(GL_MODELVIEW); 
		glLoadIdentity();
		
		lightBank.enable();
		light = lightBank.acquire();
		
		light.setDirection(-.2,1,.5);
		light.setDiffuseColor(.3,.3,.3);
		
		Material wallMat = new DiffuseImageTextureMaterial("/wall.jpg", true).scale(2, 2);
		Material floorMat = new DiffuseImageTextureMaterial("/floor.jpg", true).scale(1, 1);
		
		dungeonRenderer = new DungeonRenderer(wallMat, floorMat, Arrays.asList(Polygons.newRegularPoly(200,200,200,20), 
				Polygons.newReverseRegularPoly(100,100,50,15))); // Reverse polygons = cutouts
		dungeonRenderer.init();
		
		while(!Display.isCloseRequested()) {
			pollInput();
			draw();
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}
	
	 private static void draw() {
		glMatrixMode(GL_MODELVIEW); 
		glLoadIdentity();
		glTranslatef(400,0,-200);
		
		glClearColor(0,0,0,1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glTranslatef(200,0,200);
		glRotated(angle,0,1,0);
		glTranslatef(-200,0,-200);
		
		light.setAmbientColor(.15 + .025*t, .1, .05);
		
		/*
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		glBindTexture(GL_TEXTURE_2D, wallTexId);
		
		
		glBegin(GL_QUADS);	
		
		double texX = 0;
		for(int l = 0; l < 400; l+=80) {
			glNormal3i(-1, 0, 0); glTexCoord2d(texX, 0); glVertex3f(0,0,l); 
			glNormal3i(-1, 0, 0); glTexCoord2d(texX, .5); glVertex3f(0,80,l);
			glNormal3i(-1, 0, 0); glTexCoord2d(texX+.5, .5); glVertex3f(0,80,l+80);
			glNormal3i(-1, 0, 0); glTexCoord2d(texX+.5, 0); glVertex3f(0,0,l+80);
			
			glNormal3i(0, 0, 1); glTexCoord2d(texX, 0); glVertex3f(l,0,400);
			glNormal3i(0, 0, 1); glTexCoord2d(texX, .5); glVertex3f(l,80,400);
			glNormal3i(0, 0, 1); glTexCoord2d(texX+.5, .5); glVertex3f(l+80,80,400);
			glNormal3i(0, 0, 1); glTexCoord2d(texX+.5, 0); glVertex3f(l+80,0,400);
	
			glNormal3i(-1, 0, 0); glTexCoord2d(texX, 0); glVertex3f(400,0,l+80);
			glNormal3i(-1, 0, 0); glTexCoord2d(texX, .5); glVertex3f(400,80,l+80);
			glNormal3i(-1, 0, 0); glTexCoord2d(texX+.5, .5); glVertex3f(400,80,l);
			glNormal3i(-1, 0, 0); glTexCoord2d(texX+.5, 0); glVertex3f(400,0,l);
			
			glNormal3i(0, 0, 1); glTexCoord2d(texX, 0); glVertex3f(l+80,0,0);
			glNormal3i(0, 0, 1); glTexCoord2d(texX, .5); glVertex3f(l+80,80,0);
			glNormal3i(0, 0, 1); glTexCoord2d(texX+.5, .5); glVertex3f(l,80,0);
			glNormal3i(0, 0, 1); glTexCoord2d(texX+.5, 0); glVertex3f(l,0,0);
			
			texX += .5;
		}
		glEnd();
		
		glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);
		glBindTexture(GL_TEXTURE_2D, floorTexId);
		
		glBegin(GL_QUADS);	
		
		for(int x = 0; x < 400; x+=80) {
			for(int z = 0; z < 400; z+=80) {
				glNormal3i(0, 1, 0); glTexCoord2d(0, 0); glVertex3f(x,0,z);
				glNormal3i(0, 1, 0); glTexCoord2d(0, 1); glVertex3f(x,0,z+80);
				glNormal3i(0, 1, 0); glTexCoord2d(1, 1); glVertex3f(x+80,0,z+80);
				glNormal3i(0, 1, 0); glTexCoord2d(1, 0); glVertex3f(x+80,0,z);				
			}
		}

		glEnd();
		*/
		
		dungeonRenderer.render();
	}

	public static void pollInput() {
		t += 1/30.0;
		if(Math.abs(t - 1) < .0001 || t > 1) t = 0;
		
		angle += .05f;
		
		//angle += 0.01;
		/*
		if(Mouse.isButtonDown(0)) {
			x = Mouse.getX();
			y = Mouse.getY();
		}
		
		while(Keyboard.next()) {
			String state = "released";
			if(Keyboard.getEventKeyState()) {
				state = "pressed";
			}
			if(Keyboard.getEventKey() == Keyboard.KEY_A) {
				System.out.println("A key " + state);
			} else if(Keyboard.getEventKey() == Keyboard.KEY_S) {
				System.out.println("S key " + state);
			} else if(Keyboard.getEventKey() == Keyboard.KEY_D) {
				System.out.println("D key " + state);
			} 
		}
		*/
	}
}
