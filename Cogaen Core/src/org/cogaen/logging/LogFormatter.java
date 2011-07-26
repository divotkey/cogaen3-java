package org.cogaen.logging;

import java.io.PrintStream;

import org.cogaen.logging.LoggingService.Priority;

public interface LogFormatter {
	public void format(PrintStream ps, Priority priority, String src, String msg, double time);
}
