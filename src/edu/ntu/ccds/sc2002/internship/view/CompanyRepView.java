package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.model.Company;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

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

    public void displayOpportunities(Company company) {
        System.out.println("\n=== Your Internship Opportunities ===");
        System.out.println(
                "ID | Title | Description | Preferred Major | Open Date | Closing Date | Number Of Slots | Visibility | Level");

        // Read opportunities from CSV and filter by company
        List<String[]> rows = CSVUtil.readCSV("data/IntershipOpp_List.csv");
        for (int i = 1; i < rows.size(); i++) { // Skip header
            String[] row = rows.get(i);
            if (row.length >= 7 && row[6].equals(company.getName())) { // Assuming company name is at index 6
                System.out.println(String.join(" | ", row));
            }
        }
    }

    public static void displayApplications(User user) {
        System.out.println("\n=== Applications ===");
        CompanyRepresentative cuser = (CompanyRepresentative) user;
        String filePath = "data/Internship_Applications_List.csv";
        List<String[]> data = CSVUtil.readCSV(filePath);

        if (data.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

        String[] header = data.get(0);
        int oppIdCol = -1;
        for (int i = 0; i < header.length; i++) {
            if (header[i].equalsIgnoreCase("InternOppID")) {
                oppIdCol = i;
                break;
            }
        }
        if (oppIdCol == -1) {
            System.out.println("internshipOpportunityID column not found in CSV.");
            return;
        }

        // Print header first
        System.out.println(String.join(", ", header));

        // Loop through each row and filter
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            String internshipID = row[oppIdCol];

            // Check if this ID is in the rep's list
            boolean matches = cuser.getCreatedInternshipOpportunities()
                    .stream()
                    .anyMatch(o -> o.getInternshipID().equals(internshipID));

            if (matches) {
                System.out.println(String.join(" | ", row));
            }
        }
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