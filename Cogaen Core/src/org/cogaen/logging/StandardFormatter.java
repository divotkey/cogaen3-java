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

import org.cogaen.logging.LoggingService.Priority;

public class StandardFormatter implements LogFormatter {

	private String timeToString(double time) {
		double t = time;
		int hours = (int) (t / 3600);
		t = t - hours * 3600;
		int minutes = (int) (t / 60);
		t = t - minutes * 60;
		int seconds = (int) t;
		t = t - seconds;
		int milliseconds = (int) (t * 1000);
		
		StringBuffer strbuf = new StringBuffer();
		if (hours < 10) {
			strbuf.append('0');
		}
		strbuf.append(Integer.toString(hours));
		strbuf.append(':');
		
		if (minutes < 10) {
			strbuf.append('0');			
		}
		strbuf.append(Integer.toString(minutes));
		strbuf.append(':');
		
		if (seconds < 10) {
			strbuf.append('0');			
		}
		strbuf.append(Integer.toString(seconds));
		
		strbuf.append('.');
		if (milliseconds < 100) {
			strbuf.append('0');			
		}
		if (milliseconds < 10) {
			strbuf.append('0');
		}
		strbuf.append(milliseconds);

		return strbuf.toString();
	}

	@Override
	public void format(PrintStream ps, Priority priority, String src, String msg, double time) {
		ps.print(timeToString(time));
		ps.print(" " + priority);
		if (src != null) {
			ps.print(" [" + src + "]");
		}
		ps.print(": ");
		
		if (msg != null) {
			ps.print(msg);
		}
		ps.println();
	}
}
