package org.cogaen.box2d;

import org.jbox2d.collision.shapes.PolygonShape;

public class BoxShapeComponent extends FixtureComponent {

	public BoxShapeComponent(double width, double height) {
		PolygonShape polyShape = new PolygonShape();
		polyShape.setAsBox((float) (width / 2), (float) (height / 2));
		getFixtureDef().shape = polyShape;
	}
	
}
