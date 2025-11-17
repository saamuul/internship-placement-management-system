package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.Internship;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
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

    public void showDashboard(User user, List<String> activityLog) {
        System.out.println("\n===== Student Dashboard =====");
        System.out.println("Welcome, " + user.getName());
        System.out.println("\n----- Recent Activities -----");
        if (activityLog.isEmpty()) {
            System.out.println("No recent activities.");
        } else {
            for (String activity : activityLog) {
                System.out.println(activity);
            }
        }
        System.out.println("------------------------------");
        System.out.println("1) View Available Internships");
        System.out.println("2) Filter Internships");
        System.out.println("3) Clear Filters");
        System.out.println("4) Apply for Internship");
        System.out.println("5) View Internship Application(s)");
        System.out.println("6) View Accepted Internship");
        System.out.println("7) Accept Internship");
        System.out.println("8) Withdraw Internship Application(s)");
        System.out.println("9) Change Password");
        System.out.println("10) Logout");
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
            System.out.println("No internships available for your profile (major, year of study, and visibility).");
            return;
        }

        System.out
                .println(
                        "────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-30s %-15s %-35s %-12s %-12s %-6s %-10s%n",
                "ID", "Title", "Company", "Description", "Open Date", "Close Date", "Slots", "Level");
        System.out
                .println(
                        "────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");

        for (Internship internship : internships) {
            System.out.printf("%-5s %-30s %-15s %-35s %-12s %-12s %-6d %-10s%n",
                    internship.getInternshipId(),
                    truncate(internship.getTitle(), 30),
                    truncate(internship.getCompanyRep(), 25),
                    truncate(internship.getDescription(), 35),
                    internship.getOpenDate(),
                    internship.getCloseDate(),
                    internship.getSlots(),
                    internship.getLevel());
        }
        System.out
                .println(
                        "────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
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

        System.out.println(
                "────────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-8s %-30s %-25s %-10s %-12s%n",
                "App ID", "Internship Title", "Company Representative", "Level", "Status");
        System.out.println(
                "────────────────────────────────────────────────────────────────────────────────────────────");

        for (int i = 0; i < appIds.size(); i++) {
            System.out.printf("%-8s %-30s %-25s %-10s %-12s%n",
                    appIds.get(i),
                    truncate(titles.get(i), 30),
                    truncate(companies.get(i), 25),
                    levels.get(i),
                    statuses.get(i));
        }

        System.out.println(
                "────────────────────────────────────────────────────────────────────────────────────────────");
    }

    // Display accepted internship details.
    public void displayAcceptedInternship(InternshipApplication application) {
        System.out.println("\n=== Your Accepted Internship ===");

        if (application == null) {
            System.out.println("You have not accepted any internship yet.");
            return;
        }

        Internship internship = application.getInternship();

        System.out.println("────────────────────────────────────────────────────────────────");
        System.out.println("Application ID: " + application.getApplicationID());
        System.out.println("Status: " + application.getStatus());

        if (internship != null) {
            System.out.println("Internship ID: " + internship.getInternshipId());
            System.out.println("Title: " + internship.getTitle());
            System.out.println("Company Representative: " + internship.getCompanyRep());
            System.out.println("Level: " + internship.getLevel());
        } else {
            System.out.println("Internship details not found.");
        }

        System.out.println("────────────────────────────────────────────────────────────────");
    }

    // Gets application ID from user to accept internship.
    public String getApplicationIdInput() {
        System.out.print("Enter Application ID to accept: ");
        return scanner.nextLine();
    }

    // Gets application ID from user to withdraw application.
    public String getApplicationIdInputForWithdrawal() {
        System.out.print("Enter Application ID for withdrawal: ");
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

    // Get filter parameters from user for internship opportunities
    public Filter getFilterInput() {
        System.out.println("\n=== Filter Internships ===");
        System.out.println("Select filters to apply (type numbers separated by commas):");
        System.out.println("1) Enter Level (BASIC/INTERMEDIATE/ADVANCED)");
        System.out.println("2) Company Representative Name");
        System.out.println("3) Closing Date (YYYY-MM-DD)");
        System.out.println("Or press ENTER for no filters.");

        System.out.print("Your choice: ");
        String input = scanner.nextLine().trim();

        Level level = null;
        String repName = null;
        String closingDate = null;

        if (!input.isEmpty()) {
            String[] choices = input.split(",");

            for (String choice : choices) {
                switch (choice.trim()) {
                    case "1":
                        System.out.print("Enter Level (BASIC/INTERMEDIATE/ADVANCED): ");
                        String levelStr = scanner.nextLine().trim().toUpperCase();
                        try {
                            level = Level.valueOf(levelStr);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid level, skipping...");
                        }
                        break;

                    case "2":
                        System.out.print("Enter Company Representative Name: ");
                        repName = scanner.nextLine().trim();
                        break;

                    case "3":
                        System.out.print("Enter Closing Date (YYYY-MM-DD): ");
                        closingDate = scanner.nextLine().trim();
                        break;

                    default:
                        System.out.println("Invalid filter option '" + choice + "'. Please enter numbers 1-3 only.");
                }
            }
        }

        return new Filter(level, repName, closingDate);
    }

    // Prompts for and gets password confirmation from user.
    public String getConfirmPasswordInput() {
        System.out.print("Confirm new password: ");
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
