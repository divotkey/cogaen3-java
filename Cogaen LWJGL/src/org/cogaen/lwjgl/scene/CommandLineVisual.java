package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceService;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

public class CommandLineVisual extends Visual {

	private static final int DEFAULT_NUM_OF_LINES = 8;
	private static final double DEFAULT_WIDTH = 500;
	private static final double LINE_SPACE = 1.0;

	private TrueTypeFont ttf;
	private StringBuffer lines[];
	private int curLine;
	private double width = DEFAULT_WIDTH;
	private double height;
	
	public CommandLineVisual(Core core, String fontRes, int numOfLines) {
		this.ttf = (TrueTypeFont) ResourceService.getInstance(core).getResource(fontRes);
		this.lines = new StringBuffer[numOfLines];
		this.curLine = 0;
		for (int i = 0; i < this.lines.length; ++i) {
			this.lines[i] = new StringBuffer("");
		}
		this.height = this.ttf.getHeight() * this.lines.length * LINE_SPACE;
	}
	
	CommandLineVisual() {
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
		for (int i = 0; i < this.lines.length; ++i) {
			this.ttf.drawString((float) (-this.width / 2), (float) (-this.height / 2 + i * ttf.getHeight() * LINE_SPACE), this.lines[i].toString());		
		}
	}

	@Override
	public void epilog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CommandLineVisual newInstance() {
		CommandLineVisual instance = new CommandLineVisual();
		super.copyFields(instance);
		instance.ttf = ttf;
		instance.lines = new StringBuffer[this.lines.length];
		for (int i = 0; i < this.lines.length; ++i) {
			instance.lines[i] = new StringBuffer(this.lines[i]);
		}
		instance.curLine = this.curLine;
		
		return instance;
	}

	public void addChar(char ch) {
		this.lines[this.curLine].append(ch);
		
		if (this.ttf.getWidth(this.lines[this.curLine].toString()) > this.width) {
			this.lines[this.curLine].deleteCharAt(this.lines[this.curLine].length() - 1);
			newLine();
			addChar(ch);
		}
	}
	
	public void addString(String str) {
		for (int i = 0; i < str.length(); ++i) {
			addChar(str.charAt(i));
		}
	}
		
	public void newLine() {
		if (this.curLine >= this.lines.length - 1) {
			
			StringBuffer first = this.lines[0];
			for (int i = 0; i < this.lines.length; ++i) {
				if (i + 1 >= this.lines.length) {
					this.lines[i] = first;
					first.setLength(0);
				} else {
					this.lines[i] = this.lines[i + 1];					
				}
			}
		} else {
			this.curLine++;
		}
	}
	
	public void deleteLastChar() {
		if (this.lines[this.curLine].length() >0) {
			this.lines[this.curLine].deleteCharAt(this.lines[this.curLine].length() - 1);
		}
	}
	
	public int curLineLengh() {
		return this.lines[this.curLine].length();
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getHeight() {
		return this.height;
	}
}
