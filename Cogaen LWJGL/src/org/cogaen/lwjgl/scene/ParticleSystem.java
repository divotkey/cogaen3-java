/* 
-----------------------------------------------------------------------------
                   Cogaen - Component-based Game Engine V3
-----------------------------------------------------------------------------
This software is developed by the Cogaen Development Team. Please have a 
look at our project home page for further details: http://www.cogaen.org
   
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Copyright (c) 2010-2011 Roman Divotkey

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
*/

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
	private Color startColor = new Color(Color.WHITE);
	private Color endColor = new Color(Color.WHITE);
	
	ParticleSystem() {
		// intentionally left empty
	}
	
	ParticleSystem newInstance() {
		ParticleSystem newInstance = new ParticleSystem();
		
		newInstance.particlesPerSecond = this.particlesPerSecond;
		newInstance.emitter = this.emitter.newInstance();
		newInstance.visual = this.visual.newInstance();
		newInstance.startSize = this.startSize;
		newInstance.endSize = this.endSize;
		newInstance.startColor.setColor(this.startColor);
		newInstance.endColor.setColor(this.endColor);
		
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
		
		Color c = this.visual.getColor();
		c.setRed(this.startColor.getRed() * p + this.endColor.getRed() * (1 - p));
		c.setGreen(this.startColor.getGreen() * p + this.endColor.getGreen() * (1 - p));
		c.setBlue(this.startColor.getBlue() * p + this.endColor.getBlue() * (1 - p));
		c.setAlpha(this.startColor.getAlpha() * p + this.endColor.getAlpha() * (1 - p));
		this.visual.setScale(size);
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
	
	public void setStartColor(ReadableColor color) {
		this.startColor.setColor(color);
	}
	
	public Color getStartColor() {
		return this.startColor;
	}
	
	public Color getEndColor() {
		return this.endColor;
	}
	
	public void setEndColor(ReadableColor color) {
		this.endColor.setColor(color);
	}
}
