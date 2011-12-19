package org.cogaen.box2d;

import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.core.UpdateableService;
import org.cogaen.entity.Entity;
import org.cogaen.entity.EntityService;
import org.cogaen.name.CogaenId;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class PhysicsService extends UpdateableService {

	public static final CogaenId ID = new CogaenId("org.cogaen.box2d.PhysicsService");
	private static final String NAME = "Cogaen Box2D Physics Service";
	private static final boolean DO_SLEEP = true;
	private static final Vec2 DEFAULT_GRAVITY = new Vec2(0, 0);
	private static final int VELOCITY_ITERATIONS = 8;
	private static final int POSITION_ITERATIONS = 3;
	
	private World world;
	private Timer timer;

	public static PhysicsService getInstance(Core core) {
		return (PhysicsService) core.getService(ID);
	}
	
	public PhysicsService() {
		addDependency(TimeService.ID);
		addDependency(EntityService.ID);
	}
	
	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		this.world = new World(DEFAULT_GRAVITY, DO_SLEEP);
		this.timer = TimeService.getInstance(getCore()).getTimer();
		this.world.setContactListener(new DirectCollisionReporter(getCore()));
	}

	@Override
	protected void doStop() {
		this.world.setContactListener(null);
		this.world = null;
		this.timer = null;
		super.doStop();
	}

	@Override
	public void update() {
		this.world.step((float) this.timer.getDeltaTime(), VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	}

	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	public World getWorld() {
		return this.world;
	}
	
	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	public void setGravity(double vx, double vy) {
		this.world.setGravity(new Vec2((float) vx, (float) vy));
	}
	
	private static class DirectCollisionReporter implements ContactListener {

		public DirectCollisionReporter(Core core) {
			EntityService.getInstance(core);
		}
		
		@Override
		public void beginContact(Contact contact) {
			Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
			Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();
			
			CollisionEvent collision = new CollisionEvent(entityA.getId(), entityB.getId());
			entityA.handleEvent(collision);
			entityB.handleEvent(collision);
		}

		@Override
		public void endContact(Contact arg0) {
			// intentionally left empty
		}

		@Override
		public void postSolve(Contact arg0, ContactImpulse arg1) {
			// intentionally left empty
		}

		@Override
		public void preSolve(Contact arg0, Manifold arg1) {
			// intentionally left empty
		}
		
	}

}
