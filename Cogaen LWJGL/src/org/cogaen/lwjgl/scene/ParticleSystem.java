package org.cogaen.lwjgl.scene;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem extends Visual {

	private List<Particle> particles = new ArrayList<Particle>();
	private int lastDead = 0;
	private double particlesPerSecond;
	private boolean active = false;
	private double particlesToEmit;
	private Emitter emitter;
	private ParticleVisual particleVisual;
	private double activationTime = 0;
	private boolean autoOff = false;
	
	ParticleSystem() {
		// intentionally left empty
	}
	
	ParticleSystem newInstance() {
		ParticleSystem newInstance = new ParticleSystem();
		
		newInstance.particlesPerSecond = this.particlesPerSecond;
		newInstance.emitter = this.emitter.newInstance();
		newInstance.particleVisual = this.particleVisual.newIntance();
		
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
		this.particleVisual.applyProlog();
		
		for (Particle particle : this.particles) {
			if (!particle.isDead()) {
				this.particleVisual.render(particle);
			}
		}
		
		this.particleVisual.applyEpilog();
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

	public ParticleVisual getParticleVisual() {
		return this.particleVisual;
	}
	
	public void setParticleVisual(ParticleVisual particleVisual) {
		this.particleVisual = particleVisual;
	}
	
	public void setActive(double t) {
		setActive(true);
		this.activationTime  = t;
		this.autoOff  = true;
	}
}
