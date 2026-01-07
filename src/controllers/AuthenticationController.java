package controllers;

import services.AuthenticationService;

import java.util.Optional;

public final class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    public boolean login(String staffId) {
        return authService.login(staffId);
    }

    public void logout() {
        authService.logout();
    }

    public Optional<AuthenticationService.UserInfo> getCurrentUser() {
        return authService.getCurrentUser();
    }

    public AuthenticationService getService() {
        return authService;
    }
}
