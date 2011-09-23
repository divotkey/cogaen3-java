package org.cogaen.lwjgl.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ParticleSystem  {

	private static final double RAD2DEG = 180.0 / Math.PI;
	
	private List<Particle> particles = new ArrayList<Particle>();
	private int lastDead = 0;
	private double particlesPerSecond;
	private boolean active = false;
	private double particlesToEmit;
	private Emitter emitter;
	private Visual visual;
	private double activationTime = 0;
	private boolean autoOff = false;
	private double startSize = 1.0;
	private double endSize = 1.0;
	
	ParticleSystem() {
		// intentionally left empty
	}
	
	ParticleSystem newInstance() {
		ParticleSystem newInstance = new ParticleSystem();
		
		newInstance.particlesPerSecond = this.particlesPerSecond;
		newInstance.emitter = this.emitter.newInstance();
		newInstance.visual = this.visual.newInstance();
		
		return newInstance;
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
		
		if (this.autoOff) {
			this.activationTime -= dt;
			if (this.activationTime <= 0) {
				setActive(false);
				this.autoOff = false;
			}
		}
		
		if (this.active) {
			emit(dt);
		}

		for (Particle particle : this.particles) {
			if (!particle.isDead()) {
				particle.update(dt);
			}
		}		
	}
	
	public void render() {
		this.visual.prolog();

		for (Particle particle : this.particles) {
			if (!particle.isDead()) {
				renderParticle(particle);
			}
		}
		
		this.visual.epilog();
	}
	
	private void renderParticle(Particle particle) {
		double p = particle.getLifeTime() /  particle.getTimeToLive();
		double size = this.startSize * p + this.endSize * (1 - p);

		GL11.glPushMatrix();
		GL11.glTranslated(particle.getPosX(), particle.getPosY(), 0);
		GL11.glRotatef((float) (particle.getAngle() * RAD2DEG), 0, 0, 1);
		//GL11.glScaled(size, size, 1);
		
		this.visual.setScale(size);
		this.visual.getColor().setAlpha(p);
		this.visual.getColor().apply();
		this.visual.render();
		
		GL11.glPopMatrix();
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
	
	public void setEmitter(Emitter emitter) {
		this.emitter = emitter;
	}

	public Visual getVisual() {
		return this.visual;
	}
	
	public void setVisual(Visual visual) {
		this.visual = visual;
	}
	
	public void setActive(double t) {
		setActive(true);
		this.activationTime  = t;
		this.autoOff  = true;
	}

	public void setStartSize(double size) {
		this.startSize = size;
	}
	
	public double getStartSize() {
		return this.startSize;
	}
	
	public void setEndSize(double size) {
		this.endSize = size;
	}
	
	public double getEndSize() {
		return this.endSize;
	}
}
