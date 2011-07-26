package org.cogaen.logging;

import org.cogaen.logging.LoggingService.Priority;

public interface LogFilter {

	public boolean accept(Priority priority, String src, String msg);
	
}
