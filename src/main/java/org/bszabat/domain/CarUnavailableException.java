package org.bszabat.domain;

public class CarUnavailableException extends RuntimeException {
    public CarUnavailableException(String message) {
        super(message);
    }
}
