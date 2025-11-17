package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.dto.ToggleVisHelper;
import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
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
        System.out.println("2) View Created Opportunities");
        System.out.println("3) Filter Opportunities");
        System.out.println("4) Clear Filters");
        System.out.println("5) View Internship Applications");
        System.out.println("6) Filter Applications");
        System.out.println("7) Clear Application Filters");
        System.out.println("8) Review Internship Applications");
        System.out.println("9) Toggle Internship Visibility");
        System.out.println("10) Change Password");
        System.out.println("11) Logout");
        System.out.print("Choose: ");
    }

    /**
     * Gets menu choice from user.
     * VIEW LAYER: Handles input.
     */

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

    // Prompts for and gets password confirmation from user.
    public String getConfirmPasswordInput() {
        System.out.print("Confirm new password: ");
        return scanner.nextLine();
    }

    // Gets menu choice from user.
    public String getMenuChoice() {
        return scanner.nextLine();
    }

    // Input opportunity ID
    public String inputOpportunityID() {
        System.out.print("Enter Internship Opportunity ID: ");
        return scanner.nextLine();
    }

    public String inputApplicationID() {
        System.out.print("Enter Internship Application ID to approve: ");
        return scanner.nextLine();
    }

    public Status inputStatus() {
        while (true) {
            System.out.print("Enter decision (SUCCESSFUL to approve, UNSUCCESSFUL to reject): ");
            String input = scanner.nextLine().trim().toUpperCase();

            // Accept common aliases
            if (input.equals("APPROVE") || input.equals("APPROVED") || input.equals("ACCEPT")
                    || input.equals("ACCEPTED")) {
                return Status.SUCCESSFUL;
            } else if (input.equals("REJECT") || input.equals("REJECTED") || input.equals("DENY")
                    || input.equals("DENIED")) {
                return Status.UNSUCCESSFUL;
            }

            try {
                Status status = Status.valueOf(input);
                // Only allow SUCCESSFUL or UNSUCCESSFUL for application review
                if (status == Status.SUCCESSFUL || status == Status.UNSUCCESSFUL) {
                    return status;
                } else {
                    System.out.println("Invalid status for application review. Enter SUCCESSFUL or UNSUCCESSFUL only.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Enter SUCCESSFUL to approve or UNSUCCESSFUL to reject.");
            }
        }
    }

    public String inputTitle() {
        System.out.print("Enter title: ");
        return scanner.nextLine();
    }

    public String inputDescription() {
        System.out.print("Enter the description: ");
        return scanner.nextLine();
    }

    public String inputPreferredMajor() {
        System.out.print("Enter preferred Major: ");
        return scanner.nextLine();
    }

    public String inputOpenDate() {
        System.out.print("Enter Open Date (YYYY-MM-DD): ");
        return scanner.nextLine();
    }

    public String inputCloseDate() {
        System.out.print("Enter Close Date (YYYY-MM-DD): ");
        return scanner.nextLine();
    }

    public String inputNumOfSlots() {
        System.out.print("Enter Number Of Slots: ");
        return scanner.nextLine();
    }

    public String inputLevel() {
        System.out.print("Enter Level (BASIC/INTERMEDIATE/ADVANCED): ");
        return scanner.nextLine();
    }

    public String inputBoolean() {
        System.out.print("Toggle visibility (true/false): ");
        return scanner.nextLine();
    }

    public void displayOpportunities(List<InternshipOpportunity> opportunities) {
        System.out.println("\n=== Your Created Internship Opportunities ===");
        System.out.println(
                "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-30s %-35s %-20s %-15s %-15s %-20s %-15s %-15s %-15s%n",
                "ID", "Title", "Description", "Preferred Major", "Open Date", "Closing Date", "Number of Slots",
                "Visibility", "Status", "Level");
        System.out.println(
                "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        for (InternshipOpportunity opportunity : opportunities) {
            System.out.printf("%-5s %-30s %-35s %-20s %-15s %-15s %-20s %-15s %-15s %-15s%n",
                    opportunity.getInternshipID(),
                    truncate(opportunity.getTitle(), 30),
                    truncate(opportunity.getDescription(), 30),
                    truncate(opportunity.getPrefMajor(), 20),
                    opportunity.getOpenDate(),
                    opportunity.getCloseDate(),
                    opportunity.getNumOfSlots(),
                    opportunity.getVisibility(),
                    opportunity.getStatus(),
                    opportunity.getLevel());
        }
    }

    public void displayApplications(List<InternshipApplication> applications) {
        System.out.println("\n=== Internship Applications ===");
        System.out
                .println("──────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-25s %-25s %-10s%n", "ID", "Student ID", "Internship ID", "Status");
        System.out
                .println("──────────────────────────────────────────────────────────────────────────────────────────");
        for (InternshipApplication application : applications) {
            System.out.printf("%-5s %-25s %-25s %-10s%n",
                    application.getApplicationID(),
                    truncate(application.getStudentID(), 25),
                    truncate(application.getInternshipID(), 25),
                    application.getStatus());
        }
    }

    public ToggleVisHelper viewToggleVisibility() {
        ToggleVisHelper result = new ToggleVisHelper();
        result.setId(inputOpportunityID());
        result.setState(inputBoolean());
        return result;
    }

    // Get filter parameters for opportunities
    public Filter getOpportunityFilterInput() {
        System.out.println("\n=== Filter Opportunities ===");
        System.out.println("Select filters to apply (type numbers separated by commas):");
        System.out.println("1) Level (BASIC/INTERMEDIATE/ADVANCED)");
        System.out.println("2) Preferred Major");
        System.out.println("3) Status (PENDING/SUCCESSFUL/UNSUCCESSFUL/FILLED)");
        System.out.println("4) Visibility (true/false)");
        System.out.println("5) Closing Date (YYYY-MM-DD)");
        System.out.println("Or press ENTER for no filters.");

        System.out.print("Your choice: ");
        String input = scanner.nextLine().trim();

        Level level = null;
        String prefMajor = null;
        String repName = null;
        Status status = null;
        Boolean visibility = null;
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
                        System.out.print("Enter Preferred Major: ");
                        prefMajor = scanner.nextLine().trim();
                        break;

                    case "3":
                        System.out.print("Enter Status (PENDING/SUCCESSFUL/UNSUCCESSFUL/FILLED): ");
                        String statusStr = scanner.nextLine().trim().toUpperCase();
                        try {
                            status = Status.valueOf(statusStr);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid status, skipping...");
                        }
                        break;

                    case "4":
                        System.out.print("Enter Visibility (true/false): ");
                        visibility = Boolean.parseBoolean(scanner.nextLine().trim());
                        break;

                    case "5":
                        System.out.print("Enter Closing Date (YYYY-MM-DD): ");
                        closingDate = scanner.nextLine().trim();
                        break;

                    default:
                        System.out.println("Invalid filter option '" + choice + "'. Please enter numbers 1-5 only.");
                }
            }
        }

        return new Filter(level, prefMajor, repName, status, visibility, closingDate);
    }

    // Get filter parameters for applications
    public Filter getApplicationFilterInput() {
        System.out.println("\n=== Filter Applications ===");
        System.out.println("Select filters to apply (type numbers separated by commas):");
        System.out.println("1) Status (PENDING/SUCCESSFUL/UNSUCCESSFUL)");
        System.out.println("2) Student ID");
        System.out.println("Or press ENTER for no filters.");

        System.out.print("Your choice: ");
        String input = scanner.nextLine().trim();

        Level level = null;
        String prefMajor = null;
        String repName = null;
        Status status = null;
        Boolean visibility = null;
        String closingDate = null;

        if (!input.isEmpty()) {
            String[] choices = input.split(",");

            for (String choice : choices) {
                switch (choice.trim()) {
                    case "1":
                        System.out.print("Enter Status (PENDING/SUCCESSFUL/UNSUCCESSFUL): ");
                        String statusStr = scanner.nextLine().trim().toUpperCase();
                        try {
                            status = Status.valueOf(statusStr);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid status, skipping...");
                        }
                        break;

                    case "2":
                        System.out.print("Enter Student ID: ");
                        repName = scanner.nextLine().trim(); // Reusing repName for studentID
                        break;

                    default:
                        System.out.println("Invalid filter option '" + choice + "'. Please enter numbers 1-3 only.");
                }
            }
        }

        return new Filter(level, prefMajor, repName, status, visibility, closingDate);
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

    private static String truncate(String str, int maxLength) {
        if (str == null)
            return "";
        if (str.length() <= maxLength)
            return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}