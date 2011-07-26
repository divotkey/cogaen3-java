package org.cogaen.logging;

import org.cogaen.logging.LoggingService.Priority;

public class PriorityFilter implements LogFilter {

	private LoggingService.Priority priority;

	public PriorityFilter(LoggingService.Priority priority) {
		this.priority = priority;
	}
	
	@Override
	public boolean accept(Priority priority, String src, String msg) {
		return this.priority == priority;
	}

}
