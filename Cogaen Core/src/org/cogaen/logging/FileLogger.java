package org.cogaen.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;

import org.cogaen.core.ServiceException;

public class FileLogger extends LoggingService {

	public static final String NAME = "Cogaen File Logging Service";
	public static final File DEFAULT_LOGFILE = new File("cogaen.log");
	
	private LogFormatter formatter;
	private File logFile;
	private PrintStream ps;
	
	public FileLogger() {
		this(DEFAULT_LOGFILE);
	}
	
	public FileLogger(File logFile) {
		this(logFile, new StandardFormatter());
	}
		
	public FileLogger(File logFile, LogFormatter formatter) {
		this.formatter = formatter;
		this.logFile = logFile;
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void doLog(Priority priority, String source, String message) {
		this.formatter.format(this.ps, priority, source, message, getCore().getTime());
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		
		try {
			this.ps = new PrintStream(this.logFile);
		} catch (FileNotFoundException e) {
			throw new ServiceException("unable to create/open log file " + this.logFile, e);
		}
		
		printHeader();
	}

	private void printHeader() {
		this.ps.println(NAME + " started (" + new Date() + ")");
		Properties properties = System.getProperties();
		this.ps.println("Cogaen Core V" + getCore().getVersion() + " running on " 
				+ properties.getProperty("os.name") 
				+ ", V" + properties.getProperty("os.version") + 
				" (" + properties.getProperty("os.arch") + ")");
		this.ps.println("-------------------------------------------------------------------------------");
		this.ps.println();
	}

	private void printFooter() {
		this.ps.println();
		this.ps.println(NAME + " stopped (" + new Date() + ")");
	}
	
	@Override
	protected void doStop() {
		printFooter();
		this.ps.close();
		super.doStop();
	}
}
