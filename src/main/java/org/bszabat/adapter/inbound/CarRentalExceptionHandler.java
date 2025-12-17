package org.bszabat.adapter.inbound;

import lombok.extern.slf4j.Slf4j;
import org.bszabat.domain.CarUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class CarRentalExceptionHandler {
    @ExceptionHandler(CarUnavailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCarUnavailableException(CarUnavailableException e) {
        String errorId = UUID.randomUUID().toString();
        log.error("Car unavailable error [ID: {}]: {}", errorId, e.getMessage());

        return new ErrorResponse(e.getMessage(), errorId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        String errorId = UUID.randomUUID().toString();
        log.warn("Validation error [ID: {}]: {}", errorId, e.getMessage());
        return new ErrorResponse("Invalid request parameter", errorId);
    }
}
