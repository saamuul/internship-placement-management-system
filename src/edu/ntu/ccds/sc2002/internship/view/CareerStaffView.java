package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.Report;
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
        System.out.println("4) Approve Internship Opportunity");
        System.out.println("5) View Withdrawal Requests");
        System.out.println("6) Approve Withdrawal Request");
        System.out.println("7) Create Filter and Generate Report");
        System.out.println("8) Change Password");
        System.out.println("9) Logout");
        System.out.print("Choose: ");
    }

    // Gets menu choice from user.
    public String getMenuChoice() {
        return scanner.nextLine();
    }

    // Gets company representative ID.
    public String getInRepId() {
        System.out.print("Enter Company Representative ID to approve: ");
        return scanner.nextLine();
    }

    // Gets company representative approval status
    public boolean getInRepAppr() {
        System.out.print("Approve Company Representative? (Y/N): ");
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("Y");
    }

    // Gets career opportunity approval status
    public boolean getInCarOppAppr() {
        System.out.print("Approve Career Opportunity? (Y/N): ");
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("Y");
    }

    // Gets application withdrawal approval status
    public boolean getInAppWithAppr() {
        System.out.print("Approve Application Withdrawal? (Y/N): ");
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("Y");
    }

    // Gets internship opportunity ID.
    public String getInCarOppId() {
        System.out.print("Enter the ID of the internship opportunity to approve: ");
        return scanner.nextLine();
    }

    // Gets the application ID to approve withdrawal of.
    public String getInAppWithId() {
        System.out.print("\nEnter the ID of the internship application to approve withdrawal: ");
        return scanner.nextLine();
    }

    // Return the Filter parameters to generate report.
    public Filter getInFilterReport() {
        System.out.println("Select filters to apply (type numbers separated by commas):");
        System.out.println("1) Level");
        System.out.println("2) Preferred Major");
        System.out.println("3) Representative Name");
        System.out.println("4) Status");
        System.out.println("5) Visibility");
        System.out.println("Or press ENTER for no filters.");

        System.out.print("Your choice: ");
        String input = scanner.nextLine().trim();

        Level level = null;
        String prefMajor = null;
        String repName = null;
        Status status = null;
        Boolean visibility = null;

        if (!input.isEmpty()) {
            String[] choices = input.split(",");

            for (String choice : choices) {
                switch (choice.trim()) {

                    case "1":
                        String levelStr = inputLevel();
                        level = Level.valueOf(levelStr);
                        break;

                    case "2":
                        System.out.println("Enter Preferred Major: ");
                        prefMajor = scanner.nextLine().trim();
                        break;

                    case "3":
                        System.out.println("Enter Representative Name: ");
                        repName = scanner.nextLine().trim();
                        break;

                    case "4":
                        String statusStr = inputStatus();
                        status = Status.valueOf(statusStr);
                        break;

                    case "5":
                        System.out.println("Enter Visibility (true/false): ");
                        visibility = Boolean.parseBoolean(scanner.nextLine().trim());
                        break;

                    default:
                        System.out.println("Invalid filter option '" + choice + "'. Please enter numbers 1-6 only.");
                }
            }
        }

        return new Filter(level, prefMajor, repName, status, visibility);
    }

    // Display pending company representatives.
    public void displayPendingCompanyReps(List<CompanyRepresentative> pendingReps) {
        if (pendingReps == null || pendingReps.isEmpty()) {
            System.out.println("No pending company representatives.");
        } else {
            System.out.println("\n=== Pending Company Representatives ===");
            System.out.println(
                    "────────────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-30s %-25s %-30s%n",
                    "ID", "Name", "Company");
            System.out.println(
                    "────────────────────────────────────────────────────────────────────────────────────");

            for (CompanyRepresentative rep : pendingReps) {
                System.out.printf("%-30s %-25s %-30s%n",
                        rep.getUserId(),
                        truncate(rep.getName(), 25),
                        truncate(rep.getCompany().getName(), 30));
            }
        }
    }

    public void displayPendingInternshipOpportunities(List<InternshipOpportunity> pendingOpps) {
        if (pendingOpps == null || pendingOpps.isEmpty()) {
            System.out.println("\nNo pending internship opportunities.");
        } else {
            System.out.println("\n=== Pending Internship Opportunities ===");
            System.out.println(
                    "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-35s %-35s %-25s %-15s %-15s %-20s %-15s %-15s %-10s%n",
                    "ID", "Title", "Description", "Preferred Major", "Open Date", "Closing Date", "Number of Slots",
                    "Visibility", "Status", "Level");
            System.out.println(
                    "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            for (InternshipOpportunity opp : pendingOpps) {
                System.out.printf("%-5s %-35s %-35s %-25s %-15s %-15s %-20s %-15s %-15s %-10s%n",
                        opp.getInternshipID(),
                        truncate(opp.getTitle(), 35),
                        truncate(opp.getDescription(), 35),
                        truncate(opp.getPrefMajor(), 25),
                        opp.getOpenDate(),
                        opp.getCloseDate(),
                        opp.getNumOfSlots(),
                        opp.getVisibility(),
                        opp.getStatus(),
                        opp.getLevel());
            }
        }
    }

    public void displayAllOpportunities(List<InternshipOpportunity> opportunities) {
        System.out.println("\n=== All Internship Opportunities ===");
        System.out.println(
                "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-35s %-35s %-25s %-15s %-15s %-20s %-15s %-15s %-10s%n",
                "ID", "Title", "Description", "Preferred Major", "Open Date", "Closing Date", "Number of Slots",
                "Visibility", "Status", "Level");
        System.out.println(
                "───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        for (InternshipOpportunity opp : opportunities) {
            System.out.printf("%-5s %-35s %-35s %-25s %-15s %-15s %-20s %-15s %-15s %-10s%n",
                    opp.getInternshipID(),
                    truncate(opp.getTitle(), 35),
                    truncate(opp.getDescription(), 35),
                    truncate(opp.getPrefMajor(), 25),
                    opp.getOpenDate(),
                    opp.getCloseDate(),
                    opp.getNumOfSlots(),
                    opp.getVisibility(),
                    opp.getStatus(),
                    opp.getLevel());
        }
    }

    public void displayPendingWithdrawalRequests(List<InternshipApplication> withdrawalRequests) {
        if (withdrawalRequests == null || withdrawalRequests.isEmpty()) {
            System.out.println("\nNo pending withdrawal requests.");
        } else {
            System.out.println("\n=== Pending Withdrawal Requests ===");

            System.out.println(
                    "─────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-20s %-20s %-20s%n", "Application ID", "Student ID", "Internship ID");
            System.out.println(
                    "─────────────────────────────────────────────────────────────────────────");

            for (InternshipApplication app : withdrawalRequests) {
                System.out.printf("%-20s %-20s %-20s%n",
                        app.getApplicationID(),
                        truncate(app.getStudentID(), 20),
                        truncate(app.getInternshipID(), 20));
            }
        }
    }

    public void showReport(Report r) {
        System.out.println(r.generateReport());
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

    public void showLogout() {
        System.out.println("Logging out...");
    }

    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int getIntInput(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    public boolean getBooleanInput(String prompt) {
        System.out.print(prompt);
        return Boolean.parseBoolean(scanner.nextLine().trim());
    }

    private static String truncate(String str, int maxLength) {
        if (str == null)
            return "";
        if (str.length() <= maxLength)
            return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    public String inputLevel() {
        String input;
        System.out.print("Enter Level (BASIC/INTERMEDIATE/ADVANCED): ");

        while (true) {
            input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("BASIC") || input.equals("INTERMEDIATE") || input.equals("ADVANCED")) {
                return input;
            }

            System.out.println("Invalid level! Please enter BASIC, INTERMEDIATE, or ADVANCED.");
            System.out.println("Please try again: ");
        }
    }

    public String inputStatus() {
        String input;
        System.out.print("Enter Status (PENDING/SUCCESSFUL/UNSUCCESSFUL/FILLED): ");

        while (true) {
            input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("PENDING") || input.equals("SUCCESSFUL") || input.equals("UNSUCCESSFUL")
                    || input.equals("FILLED")) {
                return input;
            }

            System.out.println("Invalid status! Please enter SUCCESSFUL, UNSUCCESSFUL, PENDING, or FILLED.");
            System.out.println("Please try again: ");
        }
    }
}
