package org.cogaen.lwjgl.scene;

public abstract class Visual {

	private Color color;
	private double scale = 1.0;

	public Visual() {
		this(Color.CYAN);
	}
	
	public Visual(ReadableColor color) {
		this.color = new Color(color);
	}
		
	public final void setColor(ReadableColor color) {
		this.color.setColor(color);
	}
	
	public final Color getColor() {
		return this.color;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getScale() {
		return this.scale;
	}
	
	
	protected void copyFields(Visual newInstance) {
		newInstance.color.setColor(this.color);
		newInstance.scale = this.scale;
	}
	
	public abstract void prolog();
	public abstract void render();
	public abstract void epilog();
	public abstract Visual newInstance();
}
