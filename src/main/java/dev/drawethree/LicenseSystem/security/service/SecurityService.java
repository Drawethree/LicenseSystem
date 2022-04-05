package dev.drawethree.LicenseSystem.security.service;

import dev.drawethree.LicenseSystem.user.model.User;

public interface SecurityService {

    User getCurrentUser();
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
