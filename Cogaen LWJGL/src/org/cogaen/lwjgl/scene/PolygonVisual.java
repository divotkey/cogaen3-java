package org.cogaen.lwjgl.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class PolygonVisual extends Visual {

	private int glMode;
	private List<Vertex> vertices = new ArrayList<Vertex>();
	
	public PolygonVisual() {
		this.glMode = GL11.GL_POLYGON;
	}
	
	@Override
	public void prolog() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void render() {
		getColor().apply();
		
	    GL11.glBegin(this.glMode);
	    for (Vertex vertex : this.vertices) {
	        GL11.glVertex2d(vertex.x, vertex.y);	    	
	    }
	    GL11.glEnd();
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public Visual newInstance() {
		PolygonVisual instance = new PolygonVisual();
		super.copyFields(instance);
		instance.glMode = this.glMode;
		for (Vertex vertex : this.vertices) {
			instance.vertices.add(vertex);
		}
		
		return instance;
	}
	
	public void addVertex(double x, double y) {
		this.vertices.add(new Vertex(x, y));
	}
	
	public boolean isFilled() {
		return this.glMode == GL11.GL_POLYGON;
	}
	
	public void setFilled(boolean filled) {
		this.glMode = filled ? GL11.GL_POLYGON : GL11.GL_LINE_LOOP;
	}
	
	private static class Vertex {
		public float x;
		public float y;
		
		public Vertex(double x, double y) {
			this.x = (float) x;
			this.y = (float) y;
		}
	}

}
