package org.cogaen.box2d;

import org.cogaen.entity.Component;
import org.cogaen.entity.ComponentEntity;
import org.cogaen.event.Event;
import org.cogaen.event.EventService;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class RevoluteJointComponent extends Component {

	private double anchorX;
	private double anchorY;
	private ComponentEntity entityB;
	private double lowerAngle;
	private double upperAngle;
	private boolean limit = false;
	private boolean collide = false;
	
	public RevoluteJointComponent(double anchorX, double anchorY, ComponentEntity entityB) {
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		this.entityB = entityB;
	}

	@Override
	public void engage() {
		super.engage();		
		EventService.getInstance(getCore()).addListener(this, BodyEngagedEvent.TYPE_ID);
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this);
		super.disengage();
	}

	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);
		if (event.isOfType(BodyEngagedEvent.TYPE_ID)) {
			handleBodyEngaged((BodyEngagedEvent) event);
		}
	}

	private void handleBodyEngaged(BodyEngagedEvent event) {
		if (!event.getEntityId().equals(this.entityB.getId())) {
			return;
		}

		RevoluteJointDef rjd = new RevoluteJointDef();
		Body bodyA = getBody(getParent());
		Body bodyB = getBody(this.entityB);
		Vec2 a = bodyA.getWorldPoint(new Vec2((float) anchorX, (float) anchorY));
		rjd.initialize(bodyA, bodyB, a);
		rjd.lowerAngle = (float) this.lowerAngle;
		rjd.upperAngle = (float) this.upperAngle;
		rjd.enableLimit = this.limit;
		rjd.collideConnected = this.collide;
		PhysicsService.getInstance(getCore()).getWorld().createJoint(rjd);
	}
		
	private Body getBody(ComponentEntity entity) {
		Box2dBody boxBody = (Box2dBody) entity.getAttribute(Box2dBody.BOX2D_BODY_ATTRIB);
		return boxBody.getBody();
	}

	public void setLimit(double lowerAngle, double upperAngle) {
		this.lowerAngle = lowerAngle;
		this.upperAngle = upperAngle;
		this.limit  = true;
	}
	
	public void setCollideConnected(boolean value) {
		this.collide = value;
	}
}
