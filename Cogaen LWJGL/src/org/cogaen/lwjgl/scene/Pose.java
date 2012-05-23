package org.cogaen.lwjgl.scene;

public class Pose {

	public double x;
	public double y;
	public double angle;
	
	public Pose(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
	public Pose() {
		this(0, 0, 0);
	}
	
	public void set(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
	public void set(Pose pose) {
		set(pose.x, pose.y, pose.angle);
	}
	
}
