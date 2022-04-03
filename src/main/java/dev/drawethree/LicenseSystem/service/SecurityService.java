package dev.drawethree.LicenseSystem.service;

import dev.drawethree.LicenseSystem.model.User;

public interface SecurityService {

    User getCurrentUser();
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
