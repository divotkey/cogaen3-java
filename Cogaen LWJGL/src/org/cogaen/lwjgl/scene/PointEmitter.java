package org.cogaen.lwjgl.scene;

import java.util.Random;

public class PointEmitter extends Emitter {

	private Random random = new Random();
	private double radius;
	private double minLinearSpeed = 1.0;
	private double maxLinearSpeed = 10.0;
	
	@Override
	public void emit(Particle particle) {
		double phi = this.angle -this.radius / 2 + this.radius * this.random.nextDouble();
		double speed = this.minLinearSpeed + this.maxLinearSpeed * this.random.nextDouble();
		double vx = -speed * Math.sin(phi);
		double vy = speed * Math.cos(phi);
		
		particle.setPosition(this.posX, this.posY, 0);
		particle.setVelocity(vx, vy, 0);
//		particle.setAcceleration(0, -9.81, 0);
		particle.setAcceleration(0, 0, 0);
		particle.setTimeToLive(2.0);
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
	
	public void setPosition(double x, double y) {
		this.posX = x;
		this.posY = y;
	}
	
	public double getPosX() {
		return this.posX;
	}
	
	public double getPosY() {
		return this.posY;
	}

}
