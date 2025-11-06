package edu.ntu.ccds.sc2002.internship.model;

public abstract class User {
    private String userId;
    private String name;
    private String email;
    private String password = "password";
    private UserRole role;

    public User(String userId, String name, String email, String password, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean login(String inputId, String inputPassword) {
        return inputId.equals(this.userId) && inputPassword.equals(this.password);
    }

    /**
     * Changes the user's password.
     * MODEL LAYER: Returns success/failure instead of printing.
     */
    public OperationResult changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return OperationResult.success("Password changed successfully.");
        } else {
            return OperationResult.failure("Failed, wrong old password.");
        }
    }
}
