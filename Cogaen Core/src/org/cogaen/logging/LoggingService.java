/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) 2010-2011 Roman Divotkey

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */

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
