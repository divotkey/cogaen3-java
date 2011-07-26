package org.cogaen.core;

import org.cogaen.name.CogaenId;

public interface Updateable {
	
	public CogaenId getId();
	public String getName();
	public void update();
	
}
