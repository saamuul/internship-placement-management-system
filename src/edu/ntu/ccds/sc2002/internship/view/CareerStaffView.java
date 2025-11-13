package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.model.Report;

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
        System.out.println("9) Create Filter and Generate Report");
        System.out.println("9) Logout");
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

    public void displayPendingInternshipOpportunities(List<InternshipOpportunity> pendingOpps) {
        if (pendingOpps == null || pendingOpps.isEmpty()) {
            System.out.println("\nNo pending internship opportunities.");
        } else {
            System.out.println("\n=== Pending Internship Opportunities ===");
            for (InternshipOpportunity opp : pendingOpps) {
                System.out.println("ID: " + opp.getInternshipID() + " | Title: " + opp.getTitle() +
                        " | Description: " + opp.getDescription() + " | Major: " + opp.getPrefMajor() +
                        " | Open Date: " + opp.getOpenDate() + "Close Date: " + opp.getCloseDate() +
                        " | Representative: " + opp.getRep() + " | Slots: " + opp.getNumOfSlots() +
                        " | Level: " + opp.getLevel());
            }
        }
    }

    public void displayAllOpportunities(List<InternshipOpportunity> opportunities) {
        System.out.println("\n=== All Internship Opportunities ===");
        for (InternshipOpportunity opp : opportunities) { 
            System.out.printf("%s %s%s", "Title:", opp.getTitle(), "\n");
            System.out.printf("%s %s%s", "Description:", opp.getDescription(), "\n");
            System.out.printf("%s %s%s", "Preferred Major:", opp.getPrefMajor(), "\n");
            System.out.printf("%s %s%s", "Open Date:", opp.getOpenDate(), "\n");
            System.out.printf("%s %s%s", "Close Date:", opp.getCloseDate(), "\n");
            System.out.printf("%s %s%s", "Representative:", opp.getRep().getName(), "\n");
            System.out.printf("%s %d%s", "Number of Slots:", opp.getNumOfSlots(), "\n");
            System.out.printf("%s %s%s", "Level:", opp.getLevel().toString(), "\n");
            System.out.printf("%s %s%s", "Visibility:", opp.getVisibility(), "\n");
            System.out.printf("%s %s%s", "Status:", opp.getStatus().toString(), "\n");
        }
    }

    public void displayAllApplications(List<InternshipApplication> applications) {
        System.out.println("\n=== All Internship Opportunities ===");
        for (InternshipApplication app : applications) { 
            System.out.printf("%s %s%s", "Application ID:", app.getApplicationID(), "\n");
            System.out.printf("%s %s%s", "Student ID:", app.getStudentID(), "\n");
            System.out.printf("%s %s%s", "Internship ID:", app.getInternshipID(), "\n");
            System.out.printf("%s %s%s", "Status:", app.getStatus().toString(), "\n");
        }
    }

    public void displayPendingWithdrawalRequests(List<InternshipApplication> withdrawalRequests) {
        if (withdrawalRequests == null || withdrawalRequests.isEmpty()) {
            System.out.println("\nNo pending withdrawal requests.");
        } else {
            System.out.println("\n=== Pending Withdrawal Requests ===");
            for (InternshipApplication app : withdrawalRequests) {
                System.out.println("Application ID: " + app.getApplicationID() +
                        " | Student ID: " + app.getStudentID() +
                        " | Internship ID: " + app.getInternshipID());
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
}
