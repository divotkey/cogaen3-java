package org.cogaen.logging;

import org.cogaen.logging.LoggingService.Priority;

public class MessageFilter implements LogFilter {

	private String message;

	public MessageFilter(String message) {
		this.message = message;
	}
	
	@Override
	public boolean accept(Priority priority, String src, String msg) {
		return this.message.equals(msg);
	}

}
