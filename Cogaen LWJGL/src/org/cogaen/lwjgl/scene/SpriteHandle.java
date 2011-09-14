package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;
import org.newdawn.slick.opengl.Texture;

public class SpriteHandle extends ResourceHandle {

	private SpriteVisual sprite;
	private double width;
	private double height;
	private CogaenId textureId;
	
	public SpriteHandle(CogaenId textureId) {
		this(textureId, 0, 0);
	}
	
	public SpriteHandle(CogaenId textureId, double width, double height) {
		this.textureId = textureId;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean isLoaded() {
		return this.sprite != null;
	}

	@Override
	public void load(Core core) throws ResourceException {
		Texture texture = (Texture) ResourceService.getInstance(core).getResource(this.textureId);
		
		if (this.width == 0 || this.height == 0) {
			this.sprite = new SpriteVisual(texture, texture.getImageWidth(), texture.getImageHeight());
		} else {
			this.sprite = new SpriteVisual(texture, this.width, this.height);
		}
	}

	@Override
	public void unload(Core core) {
		this.sprite = null;
	}

	@Override
	public Object getResource() {
		return this.sprite;
	}

}
