
abstract class User {
    private String userID;
    private String name;
    private String password = "password";

    public User(String userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public String getUserID() { return this.userID; }
    public String getName() { return this.name; }
    public void setName(String inputName) { this.name = inputName; }

    public boolean login(String inputID, String inputPassword) {
        return inputID.equals(this.userID) && inputPassword.equals(this.password);
    }

    public void changePassword(String oldPassword, String newPassword) {
        if(this.password.equals(oldPassword)) {
            this.password = newPassword;
        } else {
            System.out.println("Failed, wrong old password");
        }
    }
}

