package dev.drawethree.LicenseSystem.license.exception;

public class LicenseInvalidStatusException extends Exception {
    public LicenseInvalidStatusException(String errorMessage) {
        super(errorMessage);
    }
}
