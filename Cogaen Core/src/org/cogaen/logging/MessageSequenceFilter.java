package org.cogaen.logging;

import org.cogaen.logging.LoggingService.Priority;

public class MessageSequenceFilter implements LogFilter {

	private String msgSequence;

	public MessageSequenceFilter(String messageSequence) {
		this.msgSequence = messageSequence;
	}
	
	@Override
	public boolean accept(Priority priority, String src, String msg) {
		return msg.contains(msgSequence);
	}

}
