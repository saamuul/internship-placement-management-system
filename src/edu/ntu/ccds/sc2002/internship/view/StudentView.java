package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.model.Internship;
import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * View class for Student interface.
 * VIEW LAYER: Responsible ONLY for displaying information and getting user
 * input.
 * - Shows menus and data
 * - Formats output
 * - Gets user input
 * DOES NOT contain business logic.
 */
public class StudentView {
    private final Scanner scanner;

    public StudentView(Scanner scanner) {
        this.scanner = scanner;
    }

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

    // Gets menu choice from user.
    public String getMenuChoice() {
        return scanner.nextLine();
    }

    // Display list of internships in a formatted table.
    public void displayInternships(List<Internship> internships) {
        System.out.println("\n=== Available Internships ===");
        if (internships == null || internships.isEmpty()) {
            System.out.println("No internships available.");
            return;
        }

        System.out.println("─────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-35s %-20s %-10s%n", "ID", "Title", "Company", "Level");
        System.out.println("─────────────────────────────────────────────────────────────────────");

        for (Internship internship : internships) {
            System.out.printf("%-5s %-35s %-20s %-10s%n",
                    internship.getInternshipId(),
                    truncate(internship.getTitle(), 35),
                    truncate(internship.getCompanyName(), 20),
                    internship.getLevel());
        }
        System.out.println("─────────────────────────────────────────────────────────────────────");
    }

    // Prompts for and gets internship ID from user to apply internship.
    public String getInternshipIdInput() {
        System.out.print("Enter Internship ID to apply: ");
        return scanner.nextLine();
    }

    // Display internship applications with full internship details.
    public void displayApplications(List<String> appIds, List<String> titles,
            List<String> companies, List<String> levels,
            List<String> statuses) {
        System.out.println("\n=== Your Internship Applications ===");

        System.out.println("─────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-8s %-30s %-20s %-10s %-12s%n",
                "App ID", "Internship Title", "Company Representative", "Level", "Status");
        System.out.println("─────────────────────────────────────────────────────────────────────────────────────");

        for (int i = 0; i < appIds.size(); i++) {
            System.out.printf("%-8s %-30s %-20s %-10s %-12s%n",
                    appIds.get(i),
                    truncate(titles.get(i), 30),
                    truncate(companies.get(i), 20),
                    levels.get(i),
                    statuses.get(i));
        }

        System.out.println("─────────────────────────────────────────────────────────────────────────────────────");
    }

    // Gets application ID from user to accept internship.
    public String getApplicationIdInput() {
        System.out.print("Enter Application ID to apply: ");
        return scanner.nextLine();
    }

    // Prompts for and gets old password from user.
    public String getOldPasswordInput() {
        System.out.print("Enter old password: ");
        return scanner.nextLine();
    }

    // Prompts for and gets new password from user.
    public String getNewPasswordInput() {
        System.out.print("Enter new password: ");
        return scanner.nextLine();
    }

    private String truncate(String str, int maxLength) {
        if (str == null)
            return "";
        if (str.length() <= maxLength)
            return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    // Display success message.
    public void showSuccess(String message) {
        System.out.println("SUCCESS: " + message);
    }

    // Display error message.
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    // Display invalid choice message.
    public void showInvalidChoice() {
        System.out.println("Invalid option. Please try again.");
    }

    // Display logout message.
    public void showLogout() {
        System.out.println("Logging out...");
    }
}
