package org.cogaen.resource;

import org.cogaen.core.ServiceException;

public class ResourceException extends ServiceException {

	private static final long serialVersionUID = 5479564854008257966L;

	public ResourceException() {
		super();
	}

	public ResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceException(String message) {
		super(message);
	}

	public ResourceException(Throwable cause) {
		super(cause);
	}

}
