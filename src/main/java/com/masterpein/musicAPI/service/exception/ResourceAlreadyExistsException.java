package com.masterpein.musicAPI.service.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
	public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
