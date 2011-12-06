package org.cogaen.box2d;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.FixtureDef;

public class BoxComponent extends BodyComponent {

	private float width;
	private float height;
	private float density;
	private float friction;
	
	public BoxComponent(BodyComponent.Type type, double x, double y, double width, double height, double density, double friction) {
		super(x, y, type);
		this.width = (float) width;
		this.height = (float) height;
		this.density = (float) density;
		this.friction = (float) friction;
	}
	
	@Override
	public void engage() {
		super.engage();
		FixtureDef fDef = new FixtureDef();
		fDef.density = this.density;
		fDef.friction = this.friction;
		PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(width / 2, height / 2);
		fDef.shape = pShape;
		fDef.restitution = 0.5f;
		
		getBody().createFixture(fDef);
	}

	@Override
	public void disengage() {
		super.disengage();
	}

}
