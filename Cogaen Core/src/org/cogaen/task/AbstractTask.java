package org.cogaen.task;

import org.cogaen.core.Core;

public abstract class AbstractTask implements Task {

	private static final String DEFAULT_NAME = "anonymous task";

	private String name;
	private Core core;
	
	public AbstractTask(Core core) {
		this(core, null);
	}
	
	public AbstractTask(Core core, String name) {
		this.core = core;
		if (name == null) {
			this.name = DEFAULT_NAME;
		} else {
			this.name = name;
		}
	}
	
	public Core getCore() {
		return this.core;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public abstract void update();

	@Override
	public abstract void destroy();
}
