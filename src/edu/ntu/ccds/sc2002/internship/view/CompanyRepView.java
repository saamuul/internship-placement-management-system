package edu.ntu.ccds.sc2002.internship.view;

import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * View class for Company Representative interface.
 * Responsible ONLY for displaying information to the user.
 * Does NOT contain business logic - that belongs in CompanyRepController.
 */
public class CompanyRepView {

    public void showDashboard(User user) {
        System.out.println("\n=== Company Representative Dashboard ===");
        System.out.println("Welcome, " + user.getName());
        System.out.println("1) Create Internship Opportunity");
        System.out.println("2) View Applications");
        System.out.println("3) Manage Opportunities");
        System.out.println("4) Logout");
        System.out.print("Choose: ");
    }

    public void displayOpportunities(/* List<InternshipOpportunity> opportunities */) {
        System.out.println("\n=== Your Internship Opportunities ===");
        // TODO: Display opportunities list
    }

    public void displayApplications(/* List<InternshipApplication> applications */) {
        System.out.println("\n=== Applications ===");
        // TODO: Display applications list
    }

    public void showSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public void showError(String message) {
        System.out.println("✗ " + message);
    }
}