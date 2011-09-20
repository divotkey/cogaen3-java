package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class TextureParticleVisual extends ParticleVisual {

	private static final double RAD2DEG = 180.0 / Math.PI;

	private Texture texture;
	private double halfWidth;
	private double halfHeight;
	private double startSize = 1;
	private double endSize = 1;
	private int displayList;
	
	public TextureParticleVisual(Texture texture, double width, double height) {
		this.texture = texture;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		
		this.displayList = createDisplayList();
	}
	
	TextureParticleVisual() {
		// intentionally left empty
	}
	
	@Override
	public TextureParticleVisual newIntance() {
		TextureParticleVisual instance = new TextureParticleVisual();
		super.copyFields(instance);
		instance.texture = texture;
		instance.halfWidth = this.halfWidth;
		instance.halfHeight = this.halfHeight;
		instance.startSize = this.startSize;
		instance.endSize = this.endSize;
		instance.displayList = this.displayList;
		
		return instance;
	}
	
	@Override
	public void applyProlog() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
    	texture.bind();	
//		GL11.glPushMatrix();
	}

	@Override
	public void applyEpilog() {
//	    GL11.glPopMatrix();		
	}

	@Override
	public void render(Particle particle) {
		double p = particle.getLifeTime() /  particle.getTimeToLive();
		double size = this.startSize * p + this.endSize * (1 - p);

//		GL11.glLoadIdentity();
		GL11.glPushMatrix();
		GL11.glTranslated(particle.getPosX(), particle.getPosY(), 0);
		GL11.glRotatef((float) (particle.getAngle() * RAD2DEG), 0, 0, 1);
		GL11.glScaled(size, size, 1);
		
		getColor().setAlpha(p);
		getColor().apply();
		
		GL11.glCallList(this.displayList);
		
//	    GL11.glBegin(GL11.GL_QUADS);
//	    GL11.glTexCoord2f(0.0f, this.texture.getHeight());
//	    GL11.glVertex2d(-this.halfWidth, -this.halfHeight);
//	    
//	    GL11.glTexCoord2f(this.texture.getWidth(), this.texture.getHeight());
//        GL11.glVertex2d(this.halfWidth, -this.halfHeight);
//        
//	    GL11.glTexCoord2f(this.texture.getWidth(), 0);
//        GL11.glVertex2d(this.halfWidth, this.halfHeight);
//        
//	    GL11.glTexCoord2f(0.0f, 0.0f);
//		GL11.glVertex2d(-this.halfWidth, this.halfHeight);
//	    GL11.glEnd();
		
	    GL11.glPopMatrix();		
	}

	private int createDisplayList() {
		int idx = GL11.glGenLists(1);
		
		GL11.glNewList(idx, GL11.GL_COMPILE);
		    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f(0.0f, this.texture.getHeight());
		    GL11.glVertex2d(-this.halfWidth, -this.halfHeight);
		    
		    GL11.glTexCoord2f(this.texture.getWidth(), this.texture.getHeight());
	        GL11.glVertex2d(this.halfWidth, -this.halfHeight);
	        
		    GL11.glTexCoord2f(this.texture.getWidth(), 0);
	        GL11.glVertex2d(this.halfWidth, this.halfHeight);
	        
		    GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex2d(-this.halfWidth, this.halfHeight);
		    GL11.glEnd();		
		GL11.glEndList();
		return idx;
	}
	
	public double getStartSize() {
		return startSize;
	}

	public void setStartSize(double startSize) {
		this.startSize = startSize;
	}

	public double getEndSize() {
		return endSize;
	}

	public void setEndSize(double endSize) {
		this.endSize = endSize;
	}

}
