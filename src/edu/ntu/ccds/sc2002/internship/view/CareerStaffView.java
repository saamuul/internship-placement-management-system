package edu.ntu.ccds.sc2002.internship.view;

import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * View class for Career Staff interface.
 * Responsible ONLY for displaying information to the user.
 * Does NOT contain business logic - that belongs in CareerStaffController.
 */
public class CareerStaffView {

    public void showDashboard(User user) {
        System.out.println("\n=== Career Staff Dashboard ===");
        System.out.println("Welcome, " + user.getName());
        System.out.println("1) View Pending Company Representatives");
        System.out.println("2) Approve Company Representative");
        System.out.println("3) View All Internship Opportunities");
        System.out.println("4) View Applications");
        System.out.println("5) Logout");
        System.out.print("Choose: ");
    }

    public void displayAllOpportunities(/* List<InternshipOpportunity> opportunities */) {
        System.out.println("\n=== All Internship Opportunities ===");
        // TODO: Display all opportunities
    }

    public void displayAllApplications(/* List<InternshipApplication> applications */) {
        System.out.println("\n=== All Applications ===");
        // TODO: Display all applications
    }

    public void showSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public void showError(String message) {
        System.out.println("✗ " + message);
    }
}
