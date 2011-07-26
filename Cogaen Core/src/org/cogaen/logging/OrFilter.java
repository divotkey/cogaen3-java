package org.cogaen.logging;

import org.cogaen.logging.LoggingService.Priority;

public class OrFilter implements LogFilter {

	private LogFilter filter1;
	private LogFilter filter2;
	
	public OrFilter(LogFilter filter1, LogFilter filter2) {
		this.filter1 = filter1;
		this.filter2 = filter2;
	}
	
	@Override
	public boolean accept(Priority priority, String src, String msg) {
		return this.filter1.accept(priority, src, msg) || this.filter2.accept(priority, src, msg);
	}

}
