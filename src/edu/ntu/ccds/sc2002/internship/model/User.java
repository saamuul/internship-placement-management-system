package edu.ntu.ccds.sc2002.internship.model;

public abstract class User {
    private String userID;
    private String name;
    private String password = "password";
    private UserRole role;

    public User(String userID, String name, String password, UserRole role) {
        this.userID = userID;
        this.name = name;
        this.password = "password";
        this.role = role;
    }

    public String getUserId() { return userID; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }

    public void setName(String inputName) { this.name = inputName; }

    public boolean login(String inputID, String inputPassword) {
        return inputID.equals(this.userID) && inputPassword.equals(this.password);
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
        } else {
            System.out.println("Failed, wrong old password");
        }
    }
}
