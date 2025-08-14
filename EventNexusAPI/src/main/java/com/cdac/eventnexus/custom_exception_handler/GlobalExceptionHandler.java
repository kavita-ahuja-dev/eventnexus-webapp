package com.cdac.eventnexus.custom_exception_handler;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cdac.eventnexus.custom_exceptions.DuplicateProfileException;
import com.cdac.eventnexus.custom_exceptions.DuplicateResourceException;
import com.cdac.eventnexus.custom_exceptions.ResourceNotFoundException;
import com.cdac.eventnexus.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice // =@ControllerAdvice => global exc handler class
//--common interceptor to intercept ALL excs in all contoller + @ResponseBody added impl. 
//on ret types of all req handling methods 
public class GlobalExceptionHandler {
	// method level anno to tell SC , following is an exc handling method : to
	// handle MethodArgumentNotValidException
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		System.out.println("in method arg invalid " + e);
		List<FieldError> fieldErrors = e.getFieldErrors();// list of fiels having validation errs
		Map<String, String> map = fieldErrors.stream()
				.collect(Collectors.toMap
						(FieldError::getField, FieldError::getDefaultMessage));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(map);
	}

	// method level anno to tell SC , following is an exc handling method : to
	// handle : ResourceNotFoundException
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ApiResponse handleResourceNotFoundException(
			ResourceNotFoundException e) {
		System.out.println("in res not found " + e);
		return new ApiResponse(e.getMessage());
	}

	// method level anno to tell SC , following is an exc handling method : to
	// handle any other remaining exc => catch all
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse handleAnyException(RuntimeException e) {
		System.out.println("in catch-all " + e);
		return new ApiResponse(e.getMessage());
	}
	
	@ExceptionHandler(DuplicateResourceException.class)
	//public ResponseEntity<String> handleDuplicateResource(DuplicateResourceException ex) {
	//	        return ResponseEntity
	//	                .status(HttpStatus.CONFLICT)
	//	                .body(ex.getMessage());
		
	public ResponseEntity<Map<String, Object>> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest req){ 

		  return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
			        "status", 409,
			        "message", ex.getMessage(),
			        "timestamp", Instant.now().toString(),
			        "path", req.getRequestURI()
			    ));
	    }
	
	@ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
	  @ResponseStatus(HttpStatus.CONFLICT)
	  public Map<String, Object> handleDataIntegrity(org.springframework.dao.DataIntegrityViolationException ex, HttpServletRequest req) {
	    String message = "Duplicate or constraint violation.";
	    String txt = String.valueOf(ex.getMessage());
	    
	    //Adding for feedback
	    
	    if (txt.contains("UK_feedbacks_user_event") || txt.contains("feedbacks.UK_feedbacks_user_event")) {
	        message = "You already submitted feedback for this event.";
	    }
	    if (txt.contains("uk_users_email") || txt.contains("users.UK6dotkott2kjsp8vw4d0m25fb7")) {
	      message = "Email is already registered.";
	    } else if (txt.contains("uk_users_username")) {
	      message = "Username is already taken.";
	    }
	    return Map.of(
	      "status", 409,
	      "message", message,
	      "timestamp", Instant.now().toString(),
	      "path", req.getRequestURI()
	    );
	  }
	
	//Added 10-08-2025
	@ExceptionHandler(DuplicateProfileException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateProfile(DuplicateProfileException ex,
                                                                      HttpServletRequest req) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "Duplicate or invalid operation.";
        HttpStatus status;

        // Decide status by message (minimal change to your service code)
        if (msg.toLowerCase().contains("already submitted feedback")) {
            status = HttpStatus.CONFLICT; // 409
        } else if (msg.toLowerCase().contains("must be registered for this event")) {
            status = HttpStatus.FORBIDDEN; // 403
        } else {
            status = HttpStatus.CONFLICT; // default
        }

        return ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "message", msg,
                "timestamp", Instant.now().toString(),
                "path", req.getRequestURI()
        ));
    }
	
	
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String, Object> handleIllegalState(IllegalStateException ex) {
	    return Map.of(
	        "status", 403,
	        "message", ex.getMessage() != null ? ex.getMessage() : "Account inactive",
	        "timestamp", java.time.Instant.now().toString()
	    );
	}

	
}

