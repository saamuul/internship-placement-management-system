package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.Level;
import edu.ntu.ccds.sc2002.internship.model.Report;
import edu.ntu.ccds.sc2002.internship.model.Status;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.model.Filter;

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
        System.out.println("5) View Applications");
        System.out.println("6) View Withdrawal Requests");
        System.out.println("7) Approve Withdrawal Request");
        System.out.println("8) Create Filter and Generate Report");
        System.out.println("9) Logout");
        System.out.print("Choose: ");
    }

    // Gets menu choice from user.
    public String getMenuChoice() {
        return scanner.nextLine();
    }

    // Gets company representative ID.
    public String getInRepId() {
        System.out.println("Enter Company Representative ID to approve: ");
        return scanner.nextLine();
    }

    // Gets internship opportunity ID.
    public String getInCarOppId() {
        System.out.print("Enter the ID of the internship opportunity to approve: ");
        return scanner.nextLine();
    }

    // Gets the application ID to approve withdrawal of.
    public String getInAppWithId() {
        System.out.print("Enter the ID of the internship application to approve withdrawal: ");
        return scanner.nextLine();
    }

    // Return the Filter parameters to generate report.
    public Filter getInFilterReport() {
        System.out.print("Enter the Level (BASIC/INTERMEDIATE/ADVANCED): ");
        String levelStr = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter the Preferred Major: ");
        String prefMajor = scanner.nextLine().trim();

        System.out.print("Enter the Application Open Date (e.g., 2025-01-15): ");
        String applOpenDate = scanner.nextLine().trim();

        System.out.print("Enter the Application Close Date (e.g., 2025-03-31): ");
        String applCloseDate = scanner.nextLine().trim();

        System.out.print("Enter the Representative Name: ");
        String repName = scanner.nextLine().trim();

        System.out.print("Enter the Number of Slots: ");
        int numOfSlots = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter the Status (PENDING/SUCCESSFUL/UNSUCCESSFUL/FILLED): ");
        String statusStr = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter the Visibility (True/False): ");
        boolean visibility = Boolean.parseBoolean(scanner.nextLine().trim());

        Level level = Level.valueOf(levelStr);
        Status status = Status.valueOf(statusStr);

        Filter f = new Filter(level, prefMajor, applOpenDate, applCloseDate, repName, numOfSlots, status, visibility);
        return f;
    }

    // Display pending company representatives.
    public void displayPendingCompanyReps(List<CompanyRepresentative> pendingReps) {
        if (pendingReps == null || pendingReps.isEmpty()) {
            System.out.println("\nNo pending company representatives.");
        } else {
            System.out.println("\n=== Pending Company Representatives ===");

            System.out.printf("%-5s %-25s %-30s%n",
                    "ID", "Name", "Company");

            for (CompanyRepresentative rep : pendingReps) {
                System.out.printf("%-5s %-25s %-30s%n",
                        rep.getUserId(),
                        rep.getName(),
                        rep.getCompany().getName());
            }
        }
    }

    public void displayPendingInternshipOpportunities(List<InternshipOpportunity> pendingOpps) {
        if (pendingOpps == null || pendingOpps.isEmpty()) {
            System.out.println("\nNo pending internship opportunities.");
        } else {
            System.out.println("\n=== Pending Internship Opportunities ===");
            System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-35s %-30s %-30s %-30s %-30s %-30s %-30s %-30s %-10s%n", "ID", "Title", "Description", "Preferred Major", "Open Date", "Closing Date", "Number of Slots", "Visibility" ,"Status",  "Level");
            System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            for (InternshipOpportunity opp : pendingOpps) {
            System.out.printf("%-5s %-35s %-30s %-30s %-30s %-30s %-30s %-30s %-30s %-10s%n",
                    opp.getInternshipID(),
                    truncate(opp.getTitle(), 35),
                    truncate(opp.getDescription(), 30),
                    truncate(opp.getPrefMajor(), 30),
                    truncate(opp.getOpenDate(), 30),
                    truncate(opp.getCloseDate(), 30),
                    truncate(String.valueOf(opp.getNumOfSlots() + ""), 30),
                    truncate(String.valueOf(opp.getVisibility()), 30),
                    truncate(String.valueOf(opp.getStatus()), 30),
                    opp.getLevel());
            }

        }
    }

    public void displayAllOpportunities(List<InternshipOpportunity> opportunities) {
        System.out.println("\n=== All Internship Opportunities ===");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-35s %-30s %-30s %-30s %-30s %-30s %-30s %-30s %-10s%n", "ID", "Title", "Description", "Preferred Major", "Open Date", "Closing Date", "Number of Slots", "Visibility" ,"Status",  "Level");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
        for (InternshipOpportunity opp : opportunities) {
            System.out.printf("%-5s %-35s %-30s %-30s %-30s %-30s %-30s %-30s %-30s %-10s%n",
                    opp.getInternshipID(),
                    truncate(opp.getTitle(), 35),
                    truncate(opp.getDescription(), 30),
                    truncate(opp.getPrefMajor(), 30),
                    truncate(opp.getOpenDate(), 30),
                    truncate(opp.getCloseDate(), 30),
                    truncate(String.valueOf(opp.getNumOfSlots() + ""), 30),
                    truncate(String.valueOf(opp.getVisibility()), 30),
                    truncate(String.valueOf(opp.getStatus()), 30),
                    opp.getLevel());
        }
    }

    public void displayAllApplications(List<InternshipApplication> applications) {
        System.out.println("\n=== All Internship Applications ===");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-35s %-30s %-10s%n", "ID", "Student ID", "Internship ID", "Status");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────");
        for (InternshipApplication app : applications) {
            System.out.printf("%-5s %-35s %-30s %-10s%n",
                    app.getApplicationID(),
                    truncate(app.getStudentID(), 35),
                    truncate(app.getInternshipID(), 30),
                    app.getStatus());
        }
    }

    public void displayPendingWithdrawalRequests(List<InternshipApplication> withdrawalRequests) {
        if (withdrawalRequests == null || withdrawalRequests.isEmpty()) {
            System.out.println("\nNo pending withdrawal requests.");
        } else {
            System.out.println("\n=== Pending Withdrawal Requests ===");

        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-35s %-30s%n", "Application ID", "Student ID", "Internship ID");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────");

            for (InternshipApplication app : withdrawalRequests) {
                System.out.printf("%-5s %-35s %-30s%n",
                        app.getApplicationID(),
                        truncate(app.getStudentID(), 35),
                        truncate(app.getInternshipID(), 30));
            }
        }
    }

    public void showReport(Report r) {
        System.out.print(r.generateReport());
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
}
