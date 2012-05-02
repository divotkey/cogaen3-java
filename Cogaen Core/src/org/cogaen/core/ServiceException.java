/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) 2010-2012 Roman Divotkey

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

package org.cogaen.core;


/**
 * A {@code ServiceException} is thrown to indicate that the attempt 
 * to start a service has failed.
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = 6195244931574767661L;

	/**
	 * Creates a new {@code ServiceException} with no detail message.
	 */
	public ServiceException() {
		// intentionally left empty
	}

	/**
	 * Creates a new {@code ServiceException} with specified detail message.
	 * @param message the detail message
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@code ServiceException} with specified cause.
	 * @param cause the cause
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new {@code ServiceException} with specified detail message
	 * and cause.
	 * @param message the detail message
	 * @param cause the cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
