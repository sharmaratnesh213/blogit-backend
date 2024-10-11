package com.blogit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3542333258003596485L;
	
	public String resourceName;
	public String fieldName;
	public Object fieldValue;
	
	public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
		super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public String getResourceName() {
		return resourceName;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public Object getFieldValue() {
		return fieldValue;
	}

}
