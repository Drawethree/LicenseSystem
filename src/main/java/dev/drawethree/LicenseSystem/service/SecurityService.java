package dev.drawethree.LicenseSystem.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
