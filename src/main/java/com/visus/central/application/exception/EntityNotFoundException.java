package com.visus.central.application.exception;

public class EntityNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String entityName, Integer id) {
        super(entityName + " no encontrado con ID: " + id);
    }
    
    public EntityNotFoundException(String message) {
        super(message);
    }

}
