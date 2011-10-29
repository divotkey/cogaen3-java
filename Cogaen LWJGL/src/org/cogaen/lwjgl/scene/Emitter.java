package org.cogaen.lwjgl.scene;


public abstract class Emitter {
	
	protected double posX;
	protected double posY;
	protected double angle;
	
	protected abstract Emitter newInstance();	
	
	public void setPose(double x, double y, double angle) {
		this.posX = x;
		this.posY = y;
		this.angle = angle;
	}
	
	public void setPosition(double x, double y) {
		this.posX = x;
		this.posY = y;		
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public abstract void emit(Particle particle);

	protected void copyFields(Emitter newInstance) {
		newInstance.posX = this.posX;
		newInstance.posY = this.posY;
		newInstance.angle = this.angle;
	}
}
