package org.cogaen.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.cogaen.core.Core;

public class TextHandle extends ResourceHandle {

	private String text;
	private String filename;
	
	public TextHandle(String filename) {
		this.filename = filename;
	}

	@Override
	public boolean isLoaded() {
		return text != null;
	}

	@Override
	public void load(Core core) throws ResourceException {
		InputStream is = ResourceService.getInstance(core).getStream(this.filename);
		if (is == null) {
			throw new ResourceException("resource not found " + this.filename);
		}
		
		InputStreamReader reader = new InputStreamReader(is);
		
		StringBuffer buf = new StringBuffer();
		int ch;
		try {
			ch = reader.read();
			while (ch != -1) {
				buf.append((char) ch);
				ch = reader.read();
			}
		} catch (IOException e) {
			throw new ResourceException("unable to read file " + this.filename);
		}
		
		this.text = buf.toString();
		try {
			reader.close();
		} catch (IOException e) {
			throw new ResourceException("unable to close file " + this.filename);
		}
	}

	@Override
	public void unload(Core core) {
		this.text = null;
	}

	@Override
	public Object getResource() {
		return this.text;
	}
}
