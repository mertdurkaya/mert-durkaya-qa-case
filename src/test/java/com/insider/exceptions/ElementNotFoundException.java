package com.insider.exceptions;

public class ElementNotFoundException extends TestFrameworkException {
    
    public ElementNotFoundException(String message) {
        super(message);
    }
    
    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
