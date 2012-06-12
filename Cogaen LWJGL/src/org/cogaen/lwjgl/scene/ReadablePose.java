package org.cogaen.lwjgl.scene;

import org.cogaen.name.CogaenId;

public interface ReadablePose {
	
	public static final CogaenId ATTR_ID = new CogaenId("ReadablePose");
	
	public double getX();
	public double getY();
	public double getAngle();
}
