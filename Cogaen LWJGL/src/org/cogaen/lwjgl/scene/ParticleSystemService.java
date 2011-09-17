package org.cogaen.lwjgl.scene;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.core.Updateable;
import org.cogaen.name.CogaenId;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class ParticleSystemService extends AbstractService implements Updateable, RenderSubsystem {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.scene.ParticleSystemService");
	public static final String NAME = "Cogaen LWJGL Particle System Service";
	
	private List<ParticleSystem> particleSystems = new ArrayList<ParticleSystem>();
	private Timer timer;
	
	public static final ParticleSystemService getInstance(Core core) {
		return (ParticleSystemService) core.getService(ID);
	}
	
	public ParticleSystemService() {
		addDependency(TimeService.ID);
		addDependency(SceneService.ID);
	}
	
	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void doPause() {
		getCore().removeUpdateable(this);
		super.doPause();
	}

	@Override
	protected void doResume() {
		super.doResume();
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		getCore().addUpdateable(this);
		this.timer = TimeService.getInstance(getCore()).getTimer();
		SceneService.getInstance(getCore()).addSubsystem(this);
	}

	@Override
	protected void doStop() {
		if (getStatus() != Status.PAUSED) {
			getCore().removeUpdateable(this);
		}
		SceneService.getInstance(getCore()).removeSubsystem(this);
		super.doStop();
	}

	@Override
	public void update() {
		for (ParticleSystem ps : this.particleSystems) {
			ps.update(this.timer.getDeltaTime());
		}
	}
	
	public void addParticleSystem(ParticleSystem ps) {
		this.particleSystems.add(ps);
	}
	
	public void removeParticleSystem(ParticleSystem ps) {
		this.particleSystems.remove(ps);
	}

	@Override
	public void render() {
		for (ParticleSystem ps : this.particleSystems) {
			ps.render();
		}
	}
}
