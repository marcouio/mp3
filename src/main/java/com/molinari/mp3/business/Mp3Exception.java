package com.molinari.mp3.business;

public class Mp3Exception extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public Mp3Exception() {
		super();
	}

	public Mp3Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public Mp3Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public Mp3Exception(String message) {
		super(message);
	}

	public Mp3Exception(Throwable cause) {
		super(cause);
	}


}
