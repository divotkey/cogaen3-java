package org.cogaen.box2d;

import org.jbox2d.collision.shapes.CircleShape;

public class CircleShapeComponent extends FixtureComponent {

	public CircleShapeComponent(double radius) {
		this(0, 0, radius);
	}
	
	public CircleShapeComponent(double x, double y, double radius) {
		CircleShape circleShape = new CircleShape();
		circleShape.m_p.set((float) x, (float) y);
		circleShape.m_radius = (float) radius;
		getFixtureDef().shape = circleShape;
	}
	
}
