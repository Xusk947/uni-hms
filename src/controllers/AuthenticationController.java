package controllers;

import models.Employee;
import services.AuthenticationService;

import java.util.Optional;

public final class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    public boolean login(String staffId, String password) {
        return authService.login(staffId, password);
    }

    public void logout() {
        authService.logout();
    }

    public boolean verifyUser(String staffId) {
        return authService.verifyUser(staffId);
    }

    public Optional<Employee> getCurrentUser() {
        return authService.getCurrentUser();
    }

    public void setCurrentUser(Employee user) {
        authService.setCurrentUser(user);
    }
}
