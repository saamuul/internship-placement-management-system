package edu.ntu.ccds.sc2002.internship.model;

import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.enums.UserRole;

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
        // Allow login with either userId or email
        boolean idMatch = inputId.equals(this.userId) || inputId.equalsIgnoreCase(this.email);
        return idMatch && inputPassword.equals(this.password);
    }

    /**
     * Template method: Changes the user's password.
     * Subclasses will update their respective csv file through savePasswordChange()
     * MODEL LAYER: Returns success/failure instead of printing.
     */
    public OperationResult changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;

            boolean savedToFile = savePasswordChange();
            if (!savedToFile) {
                return OperationResult.failure("User record not found. Password update failed.");
            }
            return OperationResult.success("Password changed successfully. Please login again.");
        } else {
            return OperationResult.failure("Failed, wrong old password.");
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Each user type should override this function to update to their respective
     * CSV file
     * Protected as this is a internal step, not meant for outer use.
     * Should update their respective csv file
     * Return False if user not found
     */
    protected abstract boolean savePasswordChange();
}
