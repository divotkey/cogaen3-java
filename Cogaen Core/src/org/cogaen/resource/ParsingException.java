package org.cogaen.resource;

import org.cogaen.core.ServiceException;

public class ParsingException extends ServiceException {

	private static final long serialVersionUID = 3735310309175719834L;

	/**
	 * Creates a new {@code ParsingException} with no detail message.
	 */
	public ParsingException() {
		// intentionally left empty
	}

	/**
	 * Creates a new {@code ParsingException} with specified detail message.
	 * @param message the detail message
	 */
	public ParsingException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@code ParsingException} with specified cause.
	 * @param cause the cause
	 */
	public ParsingException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new {@code ParsingException} with specified detail message
	 * and cause.
	 * @param message the detail message
	 * @param cause the cause
	 */
	public ParsingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}
