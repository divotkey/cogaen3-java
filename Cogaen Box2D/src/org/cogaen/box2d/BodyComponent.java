package org.cogaen.box2d;

import org.cogaen.entity.ComponentEntity;
import org.cogaen.entity.UpdateableComponent;
import org.cogaen.event.EventService;
import org.cogaen.math.Vector2;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class BodyComponent extends UpdateableComponent implements Pose2D, Box2dBody, PhysicsBody {

	public enum Type {DYNAMIC, STATIC, KINEMATIC};
	
	private Body body;
	private BodyType type;
	private EventService evtSrv;
	private double xPos;
	private double yPos;
	private double angle;
	private double angularDamping;
	private double linearDamping;
	
	public BodyComponent(double x, double y, double angle) {
		this(x, y, angle, Type.DYNAMIC);
	}
	
	public BodyComponent(double x, double y, double angle, Type type) {
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
		this.angle = angle;
	}
	
	@Override
	public void initialize(ComponentEntity parent) {
		super.initialize(parent);
		getParent().addAttribute(Pose2D.ATTR_ID, this);
		getParent().addAttribute(Box2dBody.BOX2D_BODY_ATTRIB, this);
		getParent().addAttribute(PHYSICS_BODY_ATTRIB, this);
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
		bodyDef.angle = (float) this.angle;
		bodyDef.angularDamping = (float) this.angularDamping;
		bodyDef.linearDamping = (float) this.linearDamping;
		this.body = world.createBody(bodyDef);
		
		getParent().handleEvent(new BodyEngagedEvent(this.body));
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

	@Override
	public Body getBody() {
		return this.body;
	}

	@Override
	public void applyForce(double fx, double fy, double px, double py) {
		this.body.applyForce(new Vec2((float) fx, (float) fy), this.body.getWorldPoint(new Vec2((float) px, (float) py)));
	}

	@Override
	public void applyRelativeForce(double fx, double fy, double px, double py) {
		this.body.applyForce(this.body.getWorldVector(new Vec2((float) fx, (float) fy)), this.body.getWorldPoint(new Vec2((float) px, (float) py)));
	}

	@Override
	public void applyTorque(double torque) {
		this.body.applyTorque((float) torque);
	}
	
	public void setAngularDamping(double angularDamping) {
		this.angularDamping = angularDamping;
	}
	
	public void setLinearDamping(double linearDamping) {
		this.linearDamping = linearDamping;
	}

	@Override
	public void getVelocity(double px, double py, Vector2 result) {
		Vec2 v = this.body.getLinearVelocityFromLocalPoint(new Vec2((float) px, (float) py)); 
		result.x = v.x;
		result.y = v.y;
	}

	@Override
	public double getVelocityX(double px, double py) {
		return this.body.getLinearVelocityFromLocalPoint(new Vec2((float) px, (float) py)).x;
	}

	@Override
	public double getVelocityY(double px, double py) {
		return this.body.getLinearVelocityFromLocalPoint(new Vec2((float) px, (float) py)).y;
	}

	@Override
	public void setVelocity(double vx, double vy) {
		this.body.setLinearVelocity(new Vec2((float) vx, (float) vy)); 
	}
}
