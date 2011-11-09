package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceService;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class TextVisual extends Visual {

	private TrueTypeFont ttf;
	private String text;
	private Alignment alignment = Alignment.LEFT;

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
		GL11.glPushMatrix();
		GL11.glScaled(getScale(), -getScale(), 1);

		org.newdawn.slick.Color utilColor = new org.newdawn.slick.Color((int) (getColor().getRed() * 255), (int) (getColor().getGreen() * 255), (int) (getColor().getBlue() * 255), (int) (getColor().getAlpha() * 255));
		
		switch (this.alignment) {
		case LEFT:
			this.ttf.drawString(0,  -this.ttf.getHeight() / 2.0f, this.text, utilColor);
			break;
			
		case CENTER:
			this.ttf.drawString(-this.ttf.getWidth(this.text) / 2,  -this.ttf.getHeight() / 2.0f, this.text, utilColor);
			break;
			
		case RIGHT:
			this.ttf.drawString(-this.ttf.getWidth(this.text), -this.ttf.getHeight() / 2.0f, this.text, utilColor);
			break;
		}
		GL11.glPopMatrix();
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
		instance.alignment = this.alignment;
		
		return instance;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

	public Alignment getAllignment() {
		return alignment;
	}

	public void setAllignment(Alignment allignment) {
		this.alignment = allignment;
	}

}
