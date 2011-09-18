package org.cogaen.lwjgl.scene;

public abstract class ParticleVisual {

	private Color color = new Color(Color.WHITE);
	
	
	
	public final void setColor(ReadableColor color) {
		this.color.setColor(color);
	}
	
	public final Color getColor() {
		return this.color;
	}
	
	public abstract ParticleVisual newIntance();
	
	public abstract void applyProlog();
	
	public abstract void applyEpilog();
	
	public abstract void render(Particle particle);

	public void copyFields(ParticleVisual newInstance) {
		newInstance.color.setColor(this.color);
	}
	
	
}
