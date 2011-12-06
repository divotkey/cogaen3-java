package org.cogaen.box2d;

import org.cogaen.name.CogaenId;

public interface Pose2D {

	public static final CogaenId ATTR_ID = new CogaenId("Pose2D");
	
	public double getPosX();
	public double getPosY();
	public double getAngle();
	public void setPosition(double x, double y);
	public void setAngle(double angle);
}
