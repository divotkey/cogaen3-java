package org.cogaen.lwjgl.scene;

import java.awt.Font;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;
import org.newdawn.slick.TrueTypeFont;

public class FontHandle extends ResourceHandle {

	public static final int PLAIN = Font.PLAIN;
	public static final int ITALIC = Font.ITALIC;
	public static final int BOLD = Font.BOLD;
	
	private int size;
	private int style;
	private String name;
	private TrueTypeFont ttf;
	
	public FontHandle(String name, int size) {
		this(name, PLAIN, size);
	}
	
	public FontHandle(String name, int style, int size) {
		this.name = name;
		this.style = style;
		this.size = size;
	}
	
	@Override
	public boolean isLoaded() {
		return this.ttf != null;
	}

	@Override
	public void load(Core core) throws ResourceException {
		Font awtFont = new Font(this.name, this.style, this.size);
		this.ttf = new TrueTypeFont(awtFont, true);
	}

	@Override
	public void unload(Core core) {
		this.ttf = null;
	}

	@Override
	public Object getResource() {
		return this.ttf;
	}

}
