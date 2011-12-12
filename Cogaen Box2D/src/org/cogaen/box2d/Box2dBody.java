package org.cogaen.box2d;

import org.cogaen.name.CogaenId;
import org.jbox2d.dynamics.Body;

public interface Box2dBody {

	public static final CogaenId BOX2D_BODY_ATTRIB = new CogaenId("Box2dBody");
	
	Body getBody();
}
