package edu.ntu.ccds.sc2002.internship.view;

import java.util.List;
import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.model.Company;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.Level;
import edu.ntu.ccds.sc2002.internship.util.InternshipInputData;
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
        System.out.println("3) View Created Opportunities");
        System.out.println("4) Manage Opportunities");
        System.out.println("5) Logout");
        System.out.print("Choose: ");
    }

    /**
     * Gets menu choice from user.
     * VIEW LAYER: Handles input.
     */
    public String getMenuChoice() {
        return scanner.nextLine();
    }
    public String inputOpportunityID(){
        System.out.println("Enter Internship Opportunity ID: ");
        return scanner.nextLine();
    }

    public String inputTitle(){
        System.out.println("Enter Title: ");
        return scanner.nextLine();
    }

    public String inputDescription(){
        System.out.println("Enter Description: ");
        return scanner.nextLine();
    }

    public String inputPreferredMajor(){
        System.out.println("Enter Preferred Major: ");
        return scanner.nextLine();
    }

    public String inputOpenDate(){
        System.out.println("Enter Open Date (YYYY-MM-DD): ");
        return scanner.nextLine();
    }

    public String inputCloseDate(){
        System.out.println("Enter Closing Date (YYYY-MM-DD): ");
        return scanner.nextLine();
    }

    public String inputNumOfSlots(){
        System.out.print("Enter Number Of Slots: ");
        return scanner.nextLine();
    }

    public String inputLevel(){
        System.out.print("Enter Level (BASIC/INTERMEDIATE/ADVANCED): ");
        return scanner.nextLine();
    }

    public InternshipInputData createOpportunityInput(){
        InternshipInputData data = new InternshipInputData();

        System.out.println("\n=== Create Internship Opportunity ===");
       // System.out.print("Enter Title: ");
        data.title = inputTitle();
        //System.out.print("Enter Description: ");
        data.description = inputDescription();
        //System.out.print("Enter Preferred Major: ");
        data.prefMajor = inputPreferredMajor();
        //System.out.print("Enter Open Date (YYYY-MM-DD): ");
        data.openDate = inputOpenDate();
        //System.out.print("Enter Closing Date (YYYY-MM-DD): ");
        data.closeDate = inputCloseDate();
        //System.out.print("Enter Number Of Slots: ");
        data.numOfSlots = Integer.parseInt(inputNumOfSlots());
        //System.out.print("Enter Level (BASIC/INTERMEDIATE/ADVANCED): ");
        data.level = inputLevel();

        return data;
        
        // Return or process the collected data as needed
    }

    public void displayOpportunities(List<InternshipOpportunity> opportunities) {
        System.out.println("\n=== Your Created Internship Opportunities ===");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-35s %-30s %-30s %-30s %-30s %-30s %-30s %-10s%n", "ID", "Title", "Description", "Preferred Major", "Open Date", "Closing Date", "Number of Slots", "Visibility" , "Level");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────");
        for (InternshipOpportunity opportunity : opportunities){
            System.out.printf("%-5s %-35s %-30s %-10s%n",
                    opportunity.getInternshipID(),
                    truncate(opportunity.getTitle(), 35),
                    truncate(opportunity.getDescription(), 30),
                    truncate(opportunity.getPrefMajor(), 30),
                    truncate(opportunity.getOpenDate(), 30),
                    truncate(opportunity.getCloseDate(), 30),
                    truncate(String.valueOf(opportunity.getNumOfSlots() + ""), 30),
                    truncate(String.valueOf(opportunity.getStatus()), 30),
                    opportunity.getLevel());
        }
        // Read opportunities from CSV and filter by company
        /*List<String[]> rows = CSVUtil.readCSV("data/IntershipOpp_List.csv");
        for (int i = 1; i < rows.size(); i++) { // Skip header
            String[] row = rows.get(i);
            if (row.length >= 7 && row[6].equals(company.getName())) { // Assuming company name is at index 6
                System.out.println(String.join(" | ", row));
            }
        }*/
    }
    // public void di

    public void displayApplications(List<InternshipApplication> applications) {
        System.out.println("\n=== Applications ===");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s %-35s %-30s %-10s%n", "ID", "Student ID", "Internship ID", "Status");
        System.out.println("──────────────────────────────────────────────────────────────────────────────────────────");
        for (InternshipApplication application : applications){
            System.out.printf("%-5s %-35s %-30s %-10s%n",
                    application.getApplicationID(),
                    truncate(application.getStudentID(), 35),
                    truncate(application.getInternshipID(), 30),
                    application.getStatus());
        }
        /*CompanyRepresentative cuser = (CompanyRepresentative) user;
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
        }*/
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