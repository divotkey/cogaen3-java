package org.cogaen.box2d;

import org.cogaen.entity.Component;
import org.cogaen.entity.ComponentEntity;
import org.cogaen.event.Event;
import org.cogaen.event.EventService;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.DistanceJointDef;

public class DistanceJointComponent extends Component {

	private double anchorAx;
	private double anchorAy;
	private double anchorBx;
	private double anchorBy;
	private ComponentEntity entityB;
	private boolean collide = false;
	
	public DistanceJointComponent(double anchorAx, double anchorAy,	double anchorBx, double anchorBy, ComponentEntity entityB) {
		this.anchorAx = anchorAx;
		this.anchorAy = anchorAy;
		this.anchorBx = anchorBx;
		this.anchorBy = anchorBy;
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

		DistanceJointDef djd = new DistanceJointDef();
		Body bodyA = getBody(getParent());
		Body bodyB = getBody(this.entityB);
		Vec2 a1 = bodyA.getWorldPoint(new Vec2((float) anchorAx, (float) anchorAy));
		Vec2 a2 = bodyB.getWorldPoint(new Vec2((float) anchorBx, (float) anchorBy));
		djd.collideConnected = this.collide;
		djd.initialize(bodyA, bodyB, a1, a2);
//		djd.dampingRatio = 1.0f;
//		djd.frequencyHz = 30;
		PhysicsService.getInstance(getCore()).getWorld().createJoint(djd);
	}
		
	private Body getBody(ComponentEntity entity) {
		Box2dBody boxBody = (Box2dBody) entity.getAttribute(Box2dBody.BOX2D_BODY_ATTRIB);
		return boxBody.getBody();
	}
	
	public void setCollideConnected(boolean value) {
		this.collide = value;
	}
	
	
}
