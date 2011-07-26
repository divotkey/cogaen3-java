package org.cogaen.core;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 6195244931574767661L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
