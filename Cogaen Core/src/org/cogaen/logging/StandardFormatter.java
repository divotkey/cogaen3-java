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
