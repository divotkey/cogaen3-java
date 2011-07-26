package org.cogaen.logging;

import org.cogaen.logging.LoggingService.Priority;

public class SourceFilter implements LogFilter {

	private String source;

	public SourceFilter(String source) {
		this.source = source;
	}
	
	@Override
	public boolean accept(Priority priority, String src, String msg) {
		return this.source.equals(src);
	}

}
