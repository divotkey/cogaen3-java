package org.cogaen.logging;

import org.cogaen.logging.LoggingService.Priority;

public class LevelFilter implements LogFilter {

	private LoggingService.Priority priority;
	
	public LevelFilter(LoggingService.Priority priority) {
		this.priority = priority;
	}
	
	@Override
	public boolean accept(Priority priority, String src, String msg) {
		return this.priority.compareTo(priority) <= 0;
	}

}
