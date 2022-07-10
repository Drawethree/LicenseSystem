package dev.drawethree.LicenseSystem.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiRequestException extends RuntimeException {

    private final HttpStatus status;

    public ApiRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
