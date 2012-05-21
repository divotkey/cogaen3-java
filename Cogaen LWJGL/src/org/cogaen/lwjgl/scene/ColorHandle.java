package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;

public class ColorHandle extends ResourceHandle {

	private ReadableColor color;
	private boolean loaded = false;

	public ColorHandle(double r, double g, double b) {
		this.color = new Color(r, g, b);
	}
	
	@Override
	public boolean isLoaded() {
		return this.loaded;
	}

	@Override
	public void load(Core core) throws ResourceException {
		this.loaded = true;
	}

	@Override
	public void unload(Core core) throws ResourceException {
		this.loaded = false;
	}

	@Override
	public ReadableColor getResource() {
		return this.color;
	}
}
