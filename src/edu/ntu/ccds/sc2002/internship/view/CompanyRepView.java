package edu.ntu.ccds.sc2002.internship.view;

import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * View class for Company Representative interface.
 * Responsible ONLY for displaying information to the user.
 * Does NOT contain business logic - that belongs in CompanyRepController.
 */
public class CompanyRepView {

    /**
     * Displays the company representative dashboard menu.
     * 
     * @param user The logged-in company representative user
     */
    public void showDashboard(User user) {
        System.out.println("\n=== Company Representative Dashboard ===");
        System.out.println("Welcome, " + user.getName());
        System.out.println("1) Create Internship Opportunity");
        System.out.println("2) View Applications");
        System.out.println("3) Manage Opportunities");
        System.out.println("4) Logout");
        System.out.print("Choose: ");
    }

    /**
     * Displays internship opportunities created by this company rep.
     * 
     * @param opportunities List of opportunities (to be implemented)
     */
    public void displayOpportunities(/* List<InternshipOpportunity> opportunities */) {
        System.out.println("\n=== Your Internship Opportunities ===");
        // TODO: Display opportunities list
    }

    /**
     * Displays applications for company's internships.
     * 
     * @param applications List of applications (to be implemented)
     */
    public void displayApplications(/* List<InternshipApplication> applications */) {
        System.out.println("\n=== Applications ===");
        // TODO: Display applications list
    }

    /**
     * Displays a success message.
     * 
     * @param message The success message to display
     */
    public void showSuccess(String message) {
        System.out.println("✓ " + message);
    }

    /**
     * Displays an error message.
     * 
     * @param message The error message to display
     */
    public void showError(String message) {
        System.out.println("✗ " + message);
    }
}