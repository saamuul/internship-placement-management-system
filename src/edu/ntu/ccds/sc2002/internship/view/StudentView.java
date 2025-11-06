package edu.ntu.ccds.sc2002.internship.view;

import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * View class for Student interface.
 * Responsible ONLY for displaying information to the user.
 * Does NOT contain business logic - that belongs in StudentController.
 */
public class StudentView {

    public void showDashboard(User user) {
        System.out.println("\n=== Student Dashboard ===");
        System.out.println("Welcome, " + user.getName());
        System.out.println("1) View Available Internships");
        System.out.println("2) Apply for Internship");
        System.out.println("3) View Internship Application(s)");
        System.out.println("4) Accept Internship");
        System.out.println("5) Withdraw Internship Application(s)");
        System.out.println("6) Change Password");
        System.out.println("7) Logout");
        System.out.print("Choose: ");
    }

    public void displayInternships(/* List<InternshipOpportunity> internships */) {
        System.out.println("\n=== Available Internships ===");
        // TODO: Display internship list
    }

    public void showSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public void showError(String message) {
        System.out.println("✗ " + message);
    }
}