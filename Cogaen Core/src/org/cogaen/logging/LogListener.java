package org.cogaen.logging;

public interface LogListener {

	public void handleLogMessage(LoggingService.Priority priority, String source, String message);

}
