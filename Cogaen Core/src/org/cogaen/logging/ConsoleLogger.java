package org.cogaen.logging;

import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

import org.cogaen.core.ServiceException;

public class ConsoleLogger extends LoggingService {

	public static final String NAME = "Cogaen Console Logging Service";
	
	private LogFormatter formatter;
	private PrintStream ps;
	
	public ConsoleLogger() {
		this(new StandardFormatter());
	}
	
	public ConsoleLogger(LogFormatter formatter) {
		this.formatter = formatter;
		this.ps = System.err;
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void doLog(Priority priority, String source, String message) {
		this.formatter.format(this.ps, priority, source, message, getCore().getTime());
	}
	
	private void printHeader(PrintStream ps) {
		ps.println(NAME + " started (" + new Date() + ")");
		Properties properties = System.getProperties();
		ps.println("Cogaen Core V" + getCore().getVersion() + " running on " 
				+ properties.getProperty("os.name") 
				+ ", V" + properties.getProperty("os.version") + 
				" (" + properties.getProperty("os.arch") + ")");
		ps.println("-------------------------------------------------------------------------------");
		ps.println();
	}

	private void printFooter(PrintStream ps) {
		ps.println();
		ps.println(NAME + " stopped (" + new Date() + ")");
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		printHeader(this.ps);
	}

	@Override
	protected void doStop() {
		printFooter(this.ps);
		super.doStop();
	}
	
	
}