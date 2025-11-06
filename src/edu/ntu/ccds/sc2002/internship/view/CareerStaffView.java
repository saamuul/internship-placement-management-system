package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * View class for Career Staff interface.
 * VIEW LAYER: Responsible ONLY for displaying information and getting user
 * input.
 * - Shows menus and data
 * - Formats output
 * - Gets user input
 * DOES NOT contain business logic.
 */
public class CareerStaffView {
    private final Scanner scanner;

    public CareerStaffView(Scanner scanner) {
        this.scanner = scanner;
    }

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

    // Gets menu choice from user.
    public String getMenuChoice() {
        return scanner.nextLine();
    }

    // Display pending company representatives.
    public void displayPendingCompanyReps(List<CompanyRepresentative> pendingReps) {
        if (pendingReps == null || pendingReps.isEmpty()) {
            System.out.println("\nNo pending company representatives.");
        } else {
            System.out.println("\n=== Pending Company Representatives ===");
            for (CompanyRepresentative rep : pendingReps) {
                System.out.println("ID: " + rep.getUserId() + " | Name: " + rep.getName() +
                        " | Company: " + rep.getCompany().getName());
            }
        }
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
        System.out.println("SUCCESS: " + message);
    }

    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    public void showInvalidChoice() {
        System.out.println("Invalid option. Please try again.");
    }

    public void showLogout() {
        System.out.println("Logging out...");
    }
}
