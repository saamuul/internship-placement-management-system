package edu.ntu.ccds.sc2002.internship.view;

import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * View class for Company Representative interface.
 * VIEW LAYER: Responsible ONLY for displaying information and getting user
 * input.
 * - Shows menus and data
 * - Formats output
 * - Gets user input
 * DOES NOT contain business logic.
 */
public class CompanyRepView {
    private final Scanner scanner;

    public CompanyRepView(Scanner scanner) {
        this.scanner = scanner;
    }

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
     * Gets menu choice from user.
     * VIEW LAYER: Handles input.
     */
    public String getMenuChoice() {
        return scanner.nextLine();
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
        System.out.println("\n✓ SUCCESS: " + message);
    }

    public void showError(String message) {
        System.out.println("\n✗ ERROR: " + message);
    }

    public void showInvalidChoice() {
        System.out.println("\n✗ Invalid option. Please try again.");
    }

    public void showLogout() {
        System.out.println("Logging out...");
    }
}