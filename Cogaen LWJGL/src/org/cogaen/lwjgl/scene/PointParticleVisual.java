package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class PointParticleVisual extends ParticleVisual {

	@Override
	public void applyProlog() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBegin(GL11.GL_POINTS);
    	GL11.glPointSize(10);
	}

	@Override
	public void applyEpilog() {
		GL11.glEnd();
	}

	@Override
	public void render(Particle particle) {
		getColor().setAlpha(particle.getLifeTime() /  particle.getTimeToLive());
		getColor().apply();
		GL11.glVertex2d(particle.getPosX(), particle.getPosY());
	}

	@Override
	public PointParticleVisual newIntance() {
		PointParticleVisual instance = new PointParticleVisual();
		super.copyFields(instance);
		return instance;
	}

}
