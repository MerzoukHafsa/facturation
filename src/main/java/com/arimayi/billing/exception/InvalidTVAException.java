package com.arimayi.billing.exception;

/**
 * Exception levée quand un taux de TVA est invalide
 */
public class InvalidTVAException extends RuntimeException {
    
    public InvalidTVAException(String message) {
        super(message);
    }
    
    public InvalidTVAException(String message, Throwable cause) {
        super(message, cause);
    }
}
