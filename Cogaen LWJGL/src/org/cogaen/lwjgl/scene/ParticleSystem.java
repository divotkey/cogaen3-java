package org.cogaen.lwjgl.scene;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.Core;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;
import org.lwjgl.opengl.GL11;

public class ParticleSystem extends Visual {

	private List<Particle> particles = new ArrayList<Particle>();
	private int lastDead = 0;
	private double particlesPerSecond;
	private boolean active = false;
	private double particlesToEmit;
	private Emitter emitter;
	
	public ParticleSystem() {
		PointEmitter pe = new PointEmitter();
		pe.setRadius(15);
		this.emitter = pe;
	}
	
	public void setParticlesPerSecond(double pps) {
		this.particlesPerSecond = pps;
		
		int n = (int) (pps * 2.5);
		((ArrayList<Particle>) this.particles).ensureCapacity(n);
		for (int i = 0; i < n; ++i) {
			this.particles.add(new Particle());
		}
	}
	
	public double getParticlesPerSecond() {
		return this.particlesPerSecond;
	}
	
	public void setActive(boolean value) {
		this.active  = value;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	void update(double dt) {
		if (!this.active) {
			return;
		}

		emit(dt);
		
		for (Particle particle : this.particles) {
			if (!particle.isDead()) {
				particle.update(dt);
			}
		}		
	}
	
	public void render() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
    	
    	GL11.glBegin(GL11.GL_POINTS);
//    	GL11.glPointSize(10);
		for (Particle particle : this.particles) {
			if (!particle.isDead()) {
				GL11.glColor4d(1, 1, 1, particle.getLifeTime() /  particle.getTimeToLive());
				GL11.glVertex2d(particle.getPosX(), particle.getPosY());
			}
		}
		GL11.glEnd();
		
		
//		double halfWidth = 0.02;
//		double halfHeight = 0.02;
//		for (Particle particle : this.particles) {
//			if (!particle.isDead()) {
//				GL11.glColor4d(1, 1, 1, particle.getLifeTime() /  particle.getTimeToLive());
//				GL11.glPushMatrix();
//				GL11.glTranslated(particle.getPosX(), particle.getPosY(), 0);
//			    GL11.glBegin(GL11.GL_QUADS);
//		        GL11.glVertex2d(-halfWidth, -halfHeight);
//		        GL11.glVertex2d(halfWidth, -halfHeight);
//		        GL11.glVertex2d(halfWidth,halfHeight);
//				GL11.glVertex2d(-halfWidth, halfHeight);
//			    GL11.glEnd();
//				GL11.glPopMatrix();
//			}
//		}
	}
	
	private void emit(double dt) {
		this.particlesToEmit += this.particlesPerSecond * dt;
		int n = (int) this.particlesToEmit;
		this.particlesToEmit = this.particlesToEmit - n;
		
		for (int i = 0; i < n; ++i) {
			Particle particle = getParticle();
			this.emitter.emit(particle);
		}
	} 

	private Particle getParticle() {
		Particle particle = null;
		int size = this.particles.size();
		
		int i = 0;
		while (i < size && particle == null) {			
			Particle p = this.particles.get((i + this.lastDead) % size);
			if (p.isDead()) {
				particle = p;
				this.lastDead = (i + this.lastDead) % size;
			}
			++i;
		}

		if (particle == null) {
			particle = new Particle();
			this.particles.add(particle);
		}
		
		return particle;
	}

	public Emitter getEmitter() {
		return this.emitter;
	}
}
