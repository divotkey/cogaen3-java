package org.cogaen.lwjgl.scene;

import java.io.IOException;
import java.io.InputStream;

import org.cogaen.core.Core;
import org.cogaen.logging.LoggingService;
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureHandle extends ResourceHandle {

	public static final int LINEAR_FILTER = GL11.GL_LINEAR;
	public static final int NEAREST_FILTER = GL11.GL_NEAREST;
	private String filename;
	private String format;
	private int filter;
	private Texture texture;
	
	public TextureHandle(String format, String filename) {
		this(format, filename, LINEAR_FILTER);
	}
	
	public TextureHandle(String format, String filename, int filter) {
		this.format = format;
		this.filename = filename;
		this.filter = filter;
	}
	
	@Override
	public boolean isLoaded() {
		return this.texture != null;
	}

	@Override
	public void load(Core core) throws ResourceException {
		InputStream in = ResourceService.getInstance(core).getStream(this.filename);
		if (in == null) {
			throw new ResourceException("resource not found " + this.filename);
		}
		try {
			this.texture = TextureLoader.getTexture(this.format, in, this.filter);
			in.close();
		} catch (IOException e) {
			throw new ResourceException("unable to load texture", e);
		}
	}

	@Override
	public void unload(Core core) {
		if (Display.isCreated()) {
			this.texture.release();
			this.texture = null;
		}
	}

	@Override
	public Object getResource() {
		return this.texture;
	}

}
