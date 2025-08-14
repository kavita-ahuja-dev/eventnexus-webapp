package com.cdac.eventnexus.custom_exceptions;

public class ApiException extends RuntimeException {

	public ApiException(String mesg) {
		super(mesg);
	}

}
