package org.cogaen.box2d;

import org.cogaen.entity.ComponentEntity;
import org.cogaen.entity.UpdateableComponent;
import org.cogaen.event.Event;
import org.cogaen.event.EventService;
import org.cogaen.math.Vector2;

public class SpringComponent extends UpdateableComponent {

	private ComponentEntity entityB;
	private double springConstant;
	private PhysicsBody bodyA;
	private PhysicsBody bodyB;
	private Pose2D poseA;
	private Pose2D poseB;
	private double distance;
	
	public SpringComponent(ComponentEntity entityB, double springConstant) {
		this.entityB = entityB;
		this.springConstant = springConstant;
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
	public void update() {
		if (this.bodyA == null || this.bodyB == null) {
			return;
		}
		
		Vector2 d = new Vector2(this.poseA.getPosX() - this.poseB.getPosX(), this.poseA.getPosY() - this.poseB.getPosY());
		double x = this.distance - d.length();
		d.normalize();
		d.scale(this.springConstant * x);
		
		this.bodyA.applyForce(d.x, d.y, 0, 0);
		this.bodyB.applyForce(-d.x, -d.y, 0, 0);
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
		
		this.bodyA = getBody(getParent());
		this.bodyB = getBody(this.entityB);
		this.poseA = getPose(getParent());
		this.poseB = getPose(this.entityB);
		
		Vector2 d = new Vector2(this.poseA.getPosX() - this.poseB.getPosX(), this.poseA.getPosY() - this.poseB.getPosY());
		this.distance = d.length();
	}

	private PhysicsBody getBody(ComponentEntity entity) {
		return (PhysicsBody) entity.getAttribute(PhysicsBody.PHYSICS_BODY_ATTRIB);
	}

	private Pose2D getPose(ComponentEntity entity) {
		return (Pose2D) entity.getAttribute(Pose2D.ATTR_ID);
	}
	
}
