package org.cogaen.lwjgl.scene;

public abstract class Visual {

	private Color color;
	private double scale = 1.0;
	private int mask = 0xFFFF;

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
	
	public void setMask(int mask) {
		this.mask = mask;
	}
	
	public int getMask() {
		return this.mask;
	}
	
	
	protected void copyFields(Visual newInstance) {
		newInstance.color.setColor(this.color);
		newInstance.scale = this.scale;
		newInstance.mask = this.mask;
	}
	
	public abstract void prolog();
	public abstract void render();
	public abstract void epilog();
	public abstract Visual newInstance();
}
