package org.cogaen.logging;

import org.cogaen.logging.LoggingService.Priority;

public class NotFilter implements LogFilter {

	private LogFilter filter;
	
	public NotFilter(LogFilter filter) {
		this.filter = filter;
	}
	
	@Override
	public boolean accept(Priority priority, String src, String msg) {
		return !this.filter.accept(priority, src, msg);
	}

}
