package org.cogaen.lwjgl.scene;

public abstract class ColorVisual extends Visual {

	private Color color = Color.CYAN;

	public final void setColor(Color color) {
		this.color = color;
	}
	
	public final Color getColor() {
		return this.color;
	}
	
}
