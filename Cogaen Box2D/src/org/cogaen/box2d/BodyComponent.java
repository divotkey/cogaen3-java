package org.cogaen.box2d;

import org.cogaen.entity.ComponentEntity;
import org.cogaen.entity.UpdateableComponent;
import org.cogaen.event.EventService;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class BodyComponent extends UpdateableComponent implements Pose2D {

	public enum Type {DYNAMIC, STATIC, KINEMATIC};
	private Body body;
	private BodyType type;
	private EventService evtSrv;
	private double xPos;
	private double yPos;
	
	public BodyComponent(double x, double y) {
		this(x, y, Type.DYNAMIC);
	}
	
	public BodyComponent(double x, double y, Type type) {
		switch (type) {	
		case DYNAMIC:
			this.type = BodyType.DYNAMIC;
			break;
			
		case STATIC:
			this.type = BodyType.STATIC;
			break;
			
		case KINEMATIC:
			this.type = BodyType.KINEMATIC;
			break;
		}
		this.xPos = x;
		this.yPos = y;
	}
	
	@Override
	public void initialize(ComponentEntity parent) {
		super.initialize(parent);
		getParent().addAttribute(Pose2D.ATTR_ID, this);
		this.evtSrv = EventService.getInstance(getCore());
	}

	@Override
	public void engage() {
		super.engage();
		
		PhysicsService phySrv = PhysicsService.getInstance(getCore());
		World world = phySrv.getWorld();
		BodyDef bodyDef = new BodyDef();
		bodyDef.userData = getParent().getId();
		bodyDef.type = this.type;
		bodyDef.position.x = (float) this.xPos;
		bodyDef.position.y = (float) this.yPos;
		bodyDef.angularVelocity = 1.0f;
		this.body = world.createBody(bodyDef);
	}

	@Override
	public void disengage() {
		World world = PhysicsService.getInstance(getCore()).getWorld();
		world.destroyBody(this.body);
		this.body = null;
		super.disengage();
	}

	@Override
	public final double getPosX() {
		if (this.body != null) {
			return this.body.getPosition().x;
		} else {
			return 0.0;
		}
	}

	@Override
	public final double getPosY() {
		if (this.body != null) {
			return this.body.getPosition().y;
		} else {
			return 0.0;
		}
	}

	@Override
	public final double getAngle() {
		if (this.body != null) {
			return this.body.getAngle();
		} else {
			return 0.0;
		}
	}

	@Override
	public final void setPosition(double x, double y) {
		this.body.setTransform(new Vec2((float) x, (float) y), this.body.getAngle());
	}

	@Override
	public final void setAngle(double angle) {
		this.body.setTransform(this.body.getPosition(), (float) angle);
	}

	@Override
	public final void update() {
		this.evtSrv.dispatchEvent(new PoseUpdateEvent(getParent().getId(), this));
	}
	
	protected final Body getBody() {
		return this.body;
	}
}
