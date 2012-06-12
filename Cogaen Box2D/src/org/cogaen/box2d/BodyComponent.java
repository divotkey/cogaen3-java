package org.cogaen.box2d;

import org.cogaen.entity.Component;
import org.cogaen.entity.ComponentEntity;
import org.cogaen.event.EventService;
import org.cogaen.math.Vector2;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class BodyComponent extends Component implements Pose2D, Box2dBody, PhysicsBody {

	public enum Type {DYNAMIC, STATIC, KINEMATIC};
	
	private Body body;
	private BodyType type;
	private double xPos;
	private double yPos;
	private double angle;
	private double angularDamping;
	private double linearDamping;
	private Vec2 auxVec1 = new Vec2();
	private Vec2 auxVec2 = new Vec2();
	
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
		getEntity().addAttribute(Pose2D.ATTR_ID, this);
		getEntity().addAttribute(Box2dBody.ATTR_ID, this);
		getEntity().addAttribute(PhysicsBody.ATTR_ID, this);
		EventService.getInstance(getCore());
	}

	@Override
	public void engage() {
		super.engage();
		
		PhysicsService phySrv = PhysicsService.getInstance(getCore());
		World world = phySrv.getWorld();
		BodyDef bodyDef = new BodyDef();
		bodyDef.userData = getEntity();
		bodyDef.type = this.type;
		bodyDef.position.x = (float) this.xPos;
		bodyDef.position.y = (float) this.yPos;
		bodyDef.angle = (float) this.angle;
		bodyDef.angularDamping = (float) this.angularDamping;
		bodyDef.linearDamping = (float) this.linearDamping;
		this.body = world.createBody(bodyDef);
		
		BodyEngagedEvent event = new BodyEngagedEvent(getEntity().getId());
		getEntity().handleEvent(event);
		EventService.getInstance(getCore()).dispatchEvent(event);
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
		this.auxVec1.set((float) x, (float) y);
		this.body.setTransform(auxVec1, this.body.getAngle());
	}

	@Override
	public final void setAngle(double angle) {
		this.body.setTransform(this.body.getPosition(), (float) angle);
	}

	@Override
	public Body getBody() {
		return this.body;
	}

	@Override
	public void applyForce(double fx, double fy, double px, double py) {
		this.auxVec1.set((float) fx, (float) fy);
		this.auxVec2.set((float) px, (float) py);
		this.body.applyForce(this.auxVec1, this.body.getWorldPoint(this.auxVec2));
	}

	@Override
	public void applyRelativeForce(double fx, double fy, double px, double py) {
		this.auxVec1.set((float) fx, (float) fy);
		this.auxVec2.set((float) px, (float) py);
		this.body.applyForce(this.body.getWorldVector(this.auxVec1), this.body.getWorldPoint(this.auxVec2));
	}

	@Override
	public void applyTorque(double torque) {
		this.body.applyTorque((float) torque);
	}
	
	public void setAngularDamping(double angularDamping) {
		if (this.body != null) {
			this.body.setAngularDamping((float) angularDamping);
		}
		this.angularDamping = angularDamping;
	}
	
	public void setLinearDamping(double linearDamping) {
		if (this.body != null) {
			this.body.setLinearDamping((float) linearDamping);
		}
		this.linearDamping = linearDamping;
	}

	@Override
	public void getVelocity(double px, double py, Vector2 result) {
		this.auxVec1.set((float) px, (float) py);
		Vec2 v = this.body.getLinearVelocityFromLocalPoint(this.auxVec1); 
		result.set(v.x, v.y);
	}

	@Override
	public double getVelocityX(double px, double py) {
		this.auxVec1.set((float) px, (float) py);
		return this.body.getLinearVelocityFromLocalPoint(this.auxVec1).x;
	}

	@Override
	public double getVelocityY(double px, double py) {
		this.auxVec1.set((float) px, (float) py);
		return this.body.getLinearVelocityFromLocalPoint(this.auxVec1).y;
	}

	@Override
	public void setVelocity(double vx, double vy) {
		this.auxVec1.set((float) vx, (float) vx);
		this.body.setLinearVelocity(new Vec2((float) vx, (float) vy)); 
	}

	@Override
	public void getWorldPoint(double px, double py, Vector2 result) {
		this.auxVec1.set((float) px, (float) py);
		Vec2 boxResult = this.body.getWorldPoint(this.auxVec1);
		result.set(boxResult.x, boxResult.y);
	}

	@Override
	public void getWorldVector(double vx, double vy, Vector2 result) {
		this.auxVec1.set((float) vx, (float) vy);
		Vec2 boxResult = this.body.getWorldVector(this.auxVec1);
		result.set(boxResult.x, boxResult.y);
	}
}
