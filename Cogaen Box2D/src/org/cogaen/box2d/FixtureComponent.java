package org.cogaen.box2d;

import org.cogaen.entity.Component;
import org.cogaen.event.Event;
import org.jbox2d.dynamics.FixtureDef;

public abstract class FixtureComponent extends Component {

	private static final float DEFAULT_DENSITY = 1.0f;
	private static final float DEFAULT_RESTITUTION = 0.5f;
	private static final float DEFAULT_FRICTION = 0.5f;
	
	private FixtureDef fixtureDef;
	
	public FixtureComponent() {
		this.fixtureDef = new FixtureDef();
		this.fixtureDef.density = DEFAULT_DENSITY;
		this.fixtureDef.restitution = DEFAULT_RESTITUTION;
		this.fixtureDef.friction = DEFAULT_FRICTION;
	}
	
	protected FixtureDef getFixtureDef() {
		return this.fixtureDef;
	}
	
	public void setDensity(double density) {
		this.fixtureDef.density = (float) density;
	}

	public void setRestitution(double restitution) {
		this.fixtureDef.restitution = (float) restitution;
	}
	
	public void setFriction(double friction) {
		this.fixtureDef.friction = (float) friction;
	}
	
	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);
		
		if (event.isOfType(BodyEngagedEvent.TYPE_ID)) {
			handleBodyEngaged((BodyEngagedEvent) event);
		}
	}

	private void handleBodyEngaged(BodyEngagedEvent event) {
		if (!getEntity().getId().equals(event.getEntityId())) {
			return;
		}
		Box2dBody boxBody = (Box2dBody) getEntity().getAttribute(Box2dBody.BOX2D_BODY_ATTRIB);
		boxBody.getBody().createFixture(this.fixtureDef);
	}
	
}
