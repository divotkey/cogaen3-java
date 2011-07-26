package org.cogaen.action;

import org.cogaen.core.Core;
import org.cogaen.logging.LoggingService;
import org.cogaen.logging.LoggingService.Priority;

public class LoggingAction implements Action {

	private LoggingService logger;
	private String source;
	private String message;
	private Priority priority;
	
	public LoggingAction(Core core,  Priority priority, String source, String message) {
		this.logger = LoggingService.getInstance(core);
		this.source = source;
		this.message = message;
		this.priority = priority;
	}
	
	@Override
	public void execute() {
		this.logger.log(this.priority, this.source, this.message);
	}

}
