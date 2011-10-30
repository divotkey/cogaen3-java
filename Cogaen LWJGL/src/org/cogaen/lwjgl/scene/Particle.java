package org.cogaen.lwjgl.scene;

public class Particle {
	
	private double position[] = new double[3];
	private double velocity[] = new double[3];
	private double acceleration[] = new double[3];
	private double externalAcceleration[] = new double[3];
	private double timeToLive;
	private double lifeTime = 0;
	private double drag = 0;

	public void update(double dt) {
		for (int i = 0; i < 3; ++i) {
			this.acceleration[i] = -this.velocity[i] * this.drag + this.externalAcceleration[i];
			this.velocity[i] += this.acceleration[i] * dt;
			this.position[i] += this.velocity[i] * dt;
		}
		
		this.lifeTime -= dt;
	}

	public void activate() {
		this.lifeTime = this.timeToLive;
	}
	
	public boolean isDead() {
		return this.lifeTime <= 0;
	}
	
	public double getTimeToLive() {
		return this.timeToLive;
	}
	
	public void setTimeToLive(double ttl) {
		this.timeToLive = ttl;
	}
	
	public void setPosition(double x, double y, double phi) {
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = phi;
	}
	
	public void setVelocity(double vx, double vy, double omega) {
		this.velocity[0] = vx;
		this.velocity[1] = vy;
		this.velocity[2] = omega;
	}
	
	public void setAcceleration(double ax, double ay, double alpha) {
		this.externalAcceleration[0] = ax;
		this.externalAcceleration[1] = ay;
		this.externalAcceleration[2] = alpha;
	}

	public double getPosX() {
		return this.position[0];
	}

	public double getPosY() {
		return this.position[1];
	}

	public double getAngle() {
		return this.position[2];
	}
	
	public double getLifeTime() {
		return this.lifeTime;
	}

	public double getDrag() {
		return drag;
	}

	public void setDrag(double drag) {
		this.drag = drag;
	}
}
