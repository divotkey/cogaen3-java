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
	
	public ConsoleLogger(LogFilter filter) {
		this(new StandardFormatter(), filter);
	}
	
	public ConsoleLogger(LogFormatter formatter) {
		this(formatter, null);
	}

	public ConsoleLogger(LogFormatter formatter, LogFilter filter) {
		this.formatter = formatter;
		this.ps = System.err;
		if (filter != null) {
			addFilter(filter);
		}
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