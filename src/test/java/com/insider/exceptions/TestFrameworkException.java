package com.insider.exceptions;

public class TestFrameworkException extends RuntimeException {
    
    public TestFrameworkException(String message) {
        super(message);
    }
    
    public TestFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public TestFrameworkException(Throwable cause) {
        super(cause);
    }
}
