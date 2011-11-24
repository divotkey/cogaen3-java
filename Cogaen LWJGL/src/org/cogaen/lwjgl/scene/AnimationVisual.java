package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceService;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class AnimationVisual extends Visual {

	public static final double DEFAULT_DISPLAY_TIME = 1.0 / 15.0;
	private Texture texture;
	private double halfWidth;
	private double halfHeight;
	private int blendMode = GL11.GL_ONE_MINUS_SRC_ALPHA;
	private int rows;
	private int columns;
	private float frameWidth;
	private float frameHeight;
	private int frame;
	private double displayTime = DEFAULT_DISPLAY_TIME;
	private double elapsedTime;
	private Timer timer;
	private boolean autoPlay;
	
	public AnimationVisual(Core core, String texRes, int rows, int columns, double width, double height) {
		super(Color.WHITE);
		this.texture = (Texture) ResourceService.getInstance(core).getResource(texRes);
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.rows = rows;
		this.columns = columns;
		this.frameWidth = this.texture.getWidth() / columns;
		this.frameHeight = this.texture.getHeight() / rows;
		this.timer = TimeService.getInstance(core).getTimer();
		this.frame = 0;
		this.elapsedTime = 0;
	}
	
	AnimationVisual() {
		// intentionally left empty
	}

	@Override
	public void prolog() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, this.blendMode);
    	texture.bind();	// or GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}

	@Override
	public void render() {
		getColor().apply();

		float sx = (this.frame % this.columns) * this.frameWidth;
		float sy = (this.frame / this.columns) * this.frameHeight;
		
	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glTexCoord2f(sx, sy + this.frameHeight);
	    GL11.glVertex2d(-this.halfWidth * getScale(), -this.halfHeight * getScale());
	    
	    GL11.glTexCoord2f(sx + this.frameWidth, sy + this.frameHeight);
        GL11.glVertex2d(this.halfWidth * getScale(), -this.halfHeight * getScale());
        
	    GL11.glTexCoord2f(sx + this.frameWidth, sy);
        GL11.glVertex2d(this.halfWidth * getScale(), this.halfHeight * getScale());
        
	    GL11.glTexCoord2f(sx, sy);
		GL11.glVertex2d(-this.halfWidth * getScale(), this.halfHeight * getScale());
	    GL11.glEnd();
	    
	    if (!this.autoPlay) {
	    	return;
	    }
	    this.elapsedTime += this.timer.getDeltaTime();
	    if (this.elapsedTime >= this.displayTime) {
	    	this.elapsedTime = 0.0;
	    	nextFrame();
	    }
	}

	private void nextFrame() {
		this.frame++;
		if (this.frame > this.columns * this.rows - 1) {
			this.frame = 0;
		}
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public Visual newInstance() {
		AnimationVisual instance = new AnimationVisual();
		super.copyFields(instance);
		instance.halfWidth = this.halfWidth;
		instance.halfHeight = this.halfHeight;
		instance.rows = this.rows;
		instance.columns = this.columns;
		instance.frame = this.frame;
		instance.timer = this.timer;
		instance.frameWidth = this.frameWidth;
		instance.frameHeight = this.frameHeight;
		instance.displayTime = this.displayTime;
		instance.autoPlay = this.autoPlay;
		
		return instance;
	}

	public double getWidth() {
		return this.halfWidth * 2;
	}
	
	public double getHeight() {
		return this.halfHeight * 2;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
		this.displayTime = this.timer.getTime() + displayTime;
	}
	
	public void setAdditive(boolean value) {
		this.blendMode = value ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA;
	}
	
	public boolean isAdditive() {
		return this.blendMode == GL11.GL_ONE;
	}

	public double getDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(double displayTime) {
		this.displayTime = displayTime;
	}
	
	public int getFrame() {
		return this.frame;
	}
	
	public void setFrame(int frame) {
		if (frame > numOfFrames() - 1 || frame < 0) {
			throw new IllegalArgumentException("invalid frame number " + frame);
		}
		this.frame = frame;
	}
	
	public int numOfFrames() {
		return this.columns * this.rows;
	}
	
	public void setAutoPlay(boolean b) {
		this.autoPlay = b;
	}
	
	public boolean isAutoPlay() {
		return this.autoPlay;
	}
}
