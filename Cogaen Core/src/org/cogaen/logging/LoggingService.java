package org.cogaen.logging;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;

public abstract class LoggingService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.time.LoggingService");
	public static enum Priority {DEBUG, INFO, NOTICE, WARNING, ERROR, CRITICAL, ALERT, EMERGENCY};
	public static LogFilter INFO_LEVEL_FILTER = new LevelFilter(Priority.INFO);
	public static LogFilter NOTICE_LEVEL_FILTER = new LevelFilter(Priority.NOTICE);
	public static LogFilter WARNING_LEVEL_FILTER = new LevelFilter(Priority.WARNING);
	public static LogFilter ERROR_LEVEL_FILTER = new LevelFilter(Priority.ERROR);
	public static LogFilter CRITICAL_LEVEL_FILTER = new LevelFilter(Priority.CRITICAL);
	public static LogFilter ALERT_LEVEL_FILTER = new LevelFilter(Priority.ALERT);
	public static LogFilter EMERGENCY_LEVEL_FILTER = new LevelFilter(Priority.EMERGENCY);
	
	public List<LogFilter> filters = new ArrayList<LogFilter>();
	
	protected abstract void doLog(Priority priority, String source, String message);

	public static LoggingService getInstance(Core core) {
		return (LoggingService) core.getService(ID);
	}
	
	@Override
	public final CogaenId getId() {
		return ID;
	}
	
	public final void addFilter(LogFilter filter) {
		this.filters.add(filter);
	}
	
	public final void removeFilter(LogFilter filter) {
		this.filters.remove(filter);
	}
	
	public final void removeAllFilters() {
		this.filters.clear();
	}
	
	private void filterLog(Priority priority, String source, String message) {
		for (LogFilter filter : this.filters) {
			if (!filter.accept(priority, source, message)) {
				return;
			}
		}
		
		doLog(priority, source, message);
	}

	/**
	 * Logs a message with specified priority.
	 * 
	 * @param priority priority of this logging entry.
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void log(Priority priority, String source, String message) {
		filterLog(priority, source, message);		
	}
	
	/** 
	 * Logs a message with priority 'debug'.
	 * 
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void logDebug(String source, String message) {
		filterLog(Priority.DEBUG, source, message);
	}

	/** 
	 * Logs a message with priority 'info'.
	 * 
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void logInfo(String source, String message) {
		filterLog(Priority.INFO, source, message);		
	}
	
	/** 
	 * Logs a message with priority 'notice'.
	 * 
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void logNotice(String source, String message) {
		filterLog(Priority.NOTICE, source, message);				
	}
	
	/** 
	 * Logs a message with priority 'warning'.
	 * 
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void logWarning(String source, String message) {
		filterLog(Priority.WARNING, source, message);						
	}
	
	/** 
	 * Logs a message with priority 'error'.
	 * 
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void logError(String source, String message) {
		filterLog(Priority.ERROR, source, message);						
	}
	
	/** 
	 * Logs a message with priority 'critical'.
	 * 
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void logCritical(String source, String message) {
		filterLog(Priority.CRITICAL, source, message);						
	}
	
	/** 
	 * Logs a message with priority 'alert'.
	 * 
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void logAlert(String source, String message) {
		filterLog(Priority.ALERT, source, message);						
	}
	
	/** 
	 * Logs a message with priority 'emergency'.
	 * 
	 * @param source logging source of this message.
	 * @param message message text to be logged.
	 */
	public final void logEmergency(String source, String message) {
		filterLog(Priority.EMERGENCY, source, message);						
	}
}
