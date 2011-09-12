package org.cogaen.lwjgl.input;

import org.cogaen.name.CogaenId;

public interface ControllerState {

	public static final CogaenId ID = new CogaenId("CONTROLLER_STATE");
	
	public double getVerticalPosition();
	
	public double getHorizontalPosition();
	
	public boolean getButton(int idx);
	
}
