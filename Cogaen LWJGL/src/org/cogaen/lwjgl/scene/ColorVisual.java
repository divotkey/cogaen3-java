package org.cogaen.lwjgl.scene;

public abstract class ColorVisual extends Visual {

	private Color color;

	public ColorVisual() {
		this(Color.CYAN);
	}
	
	public ColorVisual(ReadableColor color) {
		this.color = new Color(color);
	}
	
	public final void setColor(Color color) {
		this.color = color;
	}
	
	public final Color getColor() {
		return this.color;
	}
	
}
