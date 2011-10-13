package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceService;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class TextVisual extends Visual {

	private TrueTypeFont ttf;
	private String text;

	public TextVisual(Core core, String fontRes) {
		this(core, fontRes, "");
	}
	
	public TextVisual(Core core, String fontRes, String text) {
		this.ttf = (TrueTypeFont) ResourceService.getInstance(core).getResource(fontRes);
		this.text = text;
	}
	
	TextVisual() {
		// intentionally left empty
	}
	
	@Override
	public void prolog() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void render() {
		GL11.glScaled(getScale(), getScale(), 1);
		getColor().apply();
		this.ttf.drawString(0,  0, this.text);
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public Visual newInstance() {
		TextVisual instance = new TextVisual();
		super.copyFields(instance);
		instance.text = this.text;
		instance.ttf = this.ttf;
		
		return instance;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

}
