package org.cogaen.box2d;

import org.cogaen.event.Event;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

public class PolygonShapeComponent extends FixtureComponent {

	private Vec2[] vertices = new Vec2[8];
	private int idx;
	
	public PolygonShapeComponent() {
		this.idx = 0;
	}
	
	public void addVertex(double x, double y) {
		this.vertices[idx++] = new Vec2((float) x, (float) y);
	}
	
	@Override
	public void handleEvent(Event event) {
		if (event.isOfType(BodyEngagedEvent.TYPE_ID)) {
			handleBodyEngaged((BodyEngagedEvent) event);
		}
		
		super.handleEvent(event);
	}

	private void handleBodyEngaged(BodyEngagedEvent event) {
		PolygonShape polyShape = new PolygonShape();
		getFixtureDef().shape = polyShape;
		polyShape.set(this.vertices, this.idx);
	}
}
