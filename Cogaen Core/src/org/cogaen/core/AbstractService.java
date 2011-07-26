package org.cogaen.core;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.name.CogaenId;

public abstract class AbstractService implements Service {

	private List<CogaenId> dependencies = new ArrayList<CogaenId>();
	private Status status;
	private Core core;
	
	public AbstractService() {
		this.status = Status.STOPPED;
	}
	
	protected final void addDependency(CogaenId serviceId) {
		this.dependencies.add(serviceId);
	}
	
	protected final void removeDependency(CogaenId serviceId) {
		this.dependencies.remove(serviceId);
	}

	public final Core getCore() {
		if (getStatus() == Status.STOPPED) {
			throw new IllegalStateException();
		}
		return this.core;
	}
	
	protected void doPause() {
		// intentionally left empty
	}
	
	protected void doResume() {
		// intentionally left empty		
	}
	
	protected void doStart() throws ServiceException {
		// intentionally left empty
	}
	
	protected void doStop() {
		// intentionally left empty
	}
	
	@Override
	public final void start(Core core) throws ServiceException {
		if (this.status != Status.STOPPED) {
			throw new IllegalStateException();
		}
		this.core = core;
		this.status = Status.STARTED;
		doStart();
	}

	@Override
	public final void stop() {
		if (this.status != Status.STARTED && this.status != Status.PAUSED) {
			throw new IllegalStateException();
		}
		doStop();
		this.status = Status.STOPPED;
	}

	@Override
	public final void pause() {
		if (this.status != Status.STARTED) {
			throw new IllegalStateException();
		}
		doPause();
		this.status = Status.PAUSED;
	}

	@Override
	public final void resume() {
		if (this.status != Status.PAUSED) {
			throw new IllegalStateException();
		}
		doResume();
		this.status = Status.STARTED;
	}
	
	@Override
	public final Status getStatus() {
		return this.status;
	}
	
	@Override
	public final int numOfDependencies() {
		return this.dependencies.size();
	}

	@Override
	public final CogaenId getDependency(int idx) {
		return this.dependencies.get(idx);
	}

}
