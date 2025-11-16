package edu.ntu.ccds.sc2002.internship.dto;

import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * Data Transfer Object representing the result of an authentication attempt.
 * DTO LAYER: Transfers authentication outcome data between layers.
 */
public class AuthResult {
    private boolean success;
    private String message;
    private User user;

    public AuthResult(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
