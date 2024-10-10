package com.blogit.exceptions;

public class OperationNotAllowedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2560810490805545679L;

	public String resourceName;
	public String fieldName;
	public Object fieldValue;
	
	public OperationNotAllowedException(String resourceName, String fieldName, Object fieldValue, String message) {
		super(String.format("Operation not allowed for %s with %s : '%s'. %s", resourceName, fieldName, fieldValue, message));
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
