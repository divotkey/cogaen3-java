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
