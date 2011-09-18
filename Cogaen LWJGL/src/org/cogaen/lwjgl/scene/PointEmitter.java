package org.cogaen.lwjgl.scene;

import java.util.Random;

public class PointEmitter extends Emitter {

	private Random random = new Random();
	private double radius = Math.PI * 2;
	private double minLinearSpeed = 1.0;
	private double maxLinearSpeed = 10.0;
	private double minAngularSpeed = 1.0;
	private double maxAngularSpeed = 10.0;
	private double minTimeToLive = 0.5;
	private double maxTimeToLive = 1.0;
	private double acceleration[] = new double[3];
	
	@Override
	public void emit(Particle particle) {
		double phi = this.angle -this.radius / 2 + this.radius * this.random.nextDouble();
		double speed = this.minLinearSpeed + this.maxLinearSpeed * this.random.nextDouble();
		double vx = -speed * Math.sin(phi);
		double vy = speed * Math.cos(phi);
		
		particle.setPosition(this.posX, this.posY, 0);
		particle.setVelocity(vx, vy, this.minAngularSpeed + this.maxAngularSpeed * this.random.nextDouble());
		particle.setAcceleration(this.acceleration[0], this.acceleration[1], this.acceleration[2]);
		particle.setTimeToLive(this.minTimeToLive + this.maxTimeToLive * this.random.nextDouble());
		particle.activate();
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getMinLinearSpeed() {
		return minLinearSpeed;
	}

	public void setMinLinearSpeed(double minLinearSpeed) {
		this.minLinearSpeed = minLinearSpeed;
	}

	public double getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	public void setMaxLinearSpeed(double maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}
	
	@Override
	protected PointEmitter newInstance() {
		PointEmitter newInstance = new PointEmitter();
		super.copyFields(newInstance);
		newInstance.radius = this.radius;
		newInstance.maxLinearSpeed = this.maxLinearSpeed;
		newInstance.minLinearSpeed = this.minLinearSpeed;
		newInstance.minAngularSpeed = this.minAngularSpeed;
		newInstance.maxAngularSpeed = this.maxAngularSpeed;
		newInstance.minTimeToLive = this.minTimeToLive;
		newInstance.maxTimeToLive = this.maxTimeToLive;
		for (int i = 0; i < 3; ++i) {
			newInstance.acceleration[i] = this.acceleration[i];
		}
		
		return newInstance;
	}

	public double getMinTimeToLive() {
		return minTimeToLive;
	}

	public void setMinTimeToLive(double minTimeToLive) {
		this.minTimeToLive = minTimeToLive;
	}

	public double getMaxTimeToLive() {
		return maxTimeToLive;
	}

	public void setMaxTimeToLive(double maxTimeToLive) {
		this.maxTimeToLive = maxTimeToLive;
	}

	public void setAcceleration(int ax, int ay, int alpha) {
		this.acceleration[0] = ax;
		this.acceleration[1] = ay;
		this.acceleration[2] = alpha;
	}

}
