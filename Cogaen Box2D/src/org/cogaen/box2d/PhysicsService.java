package org.cogaen.box2d;

import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.core.UpdateableService;
import org.cogaen.name.CogaenId;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

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
	}
	
	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		this.world = new World(DEFAULT_GRAVITY, DO_SLEEP);
		this.timer = TimeService.getInstance(getCore()).getTimer();
	}

	@Override
	protected void doStop() {
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

}
