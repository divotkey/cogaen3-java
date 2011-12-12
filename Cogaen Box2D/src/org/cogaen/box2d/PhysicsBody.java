package org.cogaen.box2d;

import org.cogaen.name.CogaenId;

public interface PhysicsBody {
	
	public static final CogaenId PHYSICS_BODY_ATTRIB = new CogaenId("PhysicsBody");
	
	public void applyForce(double fx, double fy, double px, double py);
	public void applyRelativeForce(double fx, double fy, double px, double py);
	public void applyTorque(double torque);
}
