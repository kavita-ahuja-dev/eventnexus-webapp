package com.cdac.eventnexus.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

//DTO - to send resp from REST server -> REST clnt
@Getter
@Setter
public class ApiResponse {
	private LocalDateTime timeStamp;
	private String errorMesg;

	public ApiResponse(String mesg) {
		this.errorMesg = mesg;
		this.timeStamp = LocalDateTime.now();
	}

}
