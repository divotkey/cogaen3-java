package org.cogaen.lwjgl.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.core.UpdateableService;
import org.cogaen.name.CogaenId;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class ParticleSystemService extends UpdateableService implements RenderSubsystem {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.scene.ParticleSystemService");
	public static final String NAME = "Cogaen LWJGL Particle System Service";
	
	private List<ParticleSystem> particleSystems = new ArrayList<ParticleSystem>();
	private Map<CogaenId, Pool> pools = new HashMap<CogaenId, Pool>();
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
	protected void doStart() throws ServiceException {
		super.doStart();
		this.timer = TimeService.getInstance(getCore()).getTimer();
		SceneService.getInstance(getCore()).addSubsystem(this);
	}

	@Override
	protected void doStop() {
		SceneService.getInstance(getCore()).removeSubsystem(this);
		super.doStop();
	}

	@Override
	public void update() {
		for (ParticleSystem ps : this.particleSystems) {
			ps.update(this.timer.getDeltaTime());
		}
	}
	
	public void createPool(CogaenId poolId) {
		Pool old = this.pools.put(poolId, new Pool());
		
		if (old != null) {
			this.pools.put(poolId, old);
			throw new RuntimeException("particle system pool already exits id " + poolId);
		}
	}
	
	public void destroyPool(CogaenId poolId) {
		Pool pool = this.pools.remove(poolId);
		
		if (pool == null) {
			throw new RuntimeException("unkonwnn particle system pool  " + poolId);
		}
		
		for (ParticleSystem ps : pool.systems) {
			this.particleSystems.remove(ps);
		}
	}
	
	public void addToPool(CogaenId poolId, ParticleSystem ps, int n) {
		addToPool(poolId, ps);
		for (int i = 0; i < n - 1; ++i) {
			addToPool(poolId, createParticleSystem(ps));
		}
	}
	
	public void addToPool(CogaenId poolId, ParticleSystem ps) {
		Pool pool = this.pools.get(poolId);
		if (pool == null) {
			throw new RuntimeException("unkonwnn particle system pool  " + poolId);
		}
		pool.addParticleSystem(ps);
	}
	
	public ParticleSystem getFromPool(CogaenId poolId) {
		Pool pool = this.pools.get(poolId);
		if (pool == null) {
			throw new RuntimeException("unkonwnn particle system pool  " + poolId);
		}
		
		return pool.getParticleSystem();
	}
	
	public ParticleSystem createParticleSystem() {
		ParticleSystem ps = new ParticleSystem();
		
		this.particleSystems.add(ps);
		return ps;
	}
	
	public ParticleSystem createParticleSystem(ParticleSystem ps) {
		ParticleSystem newInstance = ps.newInstance();
		
		this.particleSystems.add(newInstance);
		return newInstance;
	}
	
	public void destroyParticleSystem(ParticleSystem ps) {
		this.particleSystems.remove(ps);
		for (Pool pool : this.pools.values()) {
			pool.removeParticleSystem(ps);
		}
	}
	
	public void destroyAll() {
		this.particleSystems.clear();
		this.pools.clear();
	}
	
	@Override
	public void render(int mask) {
		for (ParticleSystem ps : this.particleSystems) {
			if ((ps.getVisual().getMask() & mask) != 0) {
				ps.render();				
			}
		}
	}
	
	private static class Pool {
		
		private List<ParticleSystem> systems = new ArrayList<ParticleSystem>();
		private int idx = 0;
		
		public void addParticleSystem(ParticleSystem ps) {
			this.systems.add(ps);
		}
		
		public void removeParticleSystem(ParticleSystem ps) {
			this.systems.remove(ps);
			if (idx >= this.systems.size()) {
				idx = 0;
			}
		}

		public ParticleSystem getParticleSystem() {
			if (systems.isEmpty()) {
				return null;
			}
			
			ParticleSystem ps = this.systems.get(idx++);
			if (idx >= this.systems.size()) {
				idx = 0;
			}
			
			return ps;
		}
	}
}
