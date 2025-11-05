package edu.ntu.ccds.sc2002.internship.view;

import edu.ntu.ccds.sc2002.internship.model.Company;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.util.CSVLoader;

/**
 * View class for Company Representative interface.
 * Responsible ONLY for displaying information to the user.
 * Does NOT contain business logic - that belongs in CompanyRepController.
 */
public class CompanyRepView {

    public void showDashboard(User user) {
        System.out.println("\n=== Company Representative Dashboard ===");
        System.out.println("Welcome, " + user.getName());
        System.out.println("1) Create Internship Opportunity");
        System.out.println("2) View Applications");
        System.out.println("3) Manage Opportunities");
        System.out.println("4) Logout");
        System.out.print("Choose: ");
    }

    public static void displayOpportunities(User user) {
        System.out.println("\n=== Your Internship Opportunities ===");
        // TODO: Display opportunities list
        CompanyRepresentative cuser = (CompanyRepresentative) user;
        System.out.println("ID | Title | Description | Preferred Major | Open Date | Closing Date | Number Of Slots | Visibility | Level");
        for (InternshipOpportunity opportunity : cuser.getCreatedInternshipOpportunities()) {
            System.out.println(opportunity.getInternshipID() + " | " +
            opportunity.getTitle() + " | " +
            opportunity.getDescription() +" | " +
            opportunity.getPrefMajor() +" | " +
            opportunity.getOpenDate() +" | " +
            opportunity.getCloseDate() +" | " +
            opportunity.getNumOfSlots() +" | " +
            opportunity.getVisibility() +" | " +
            opportunity.getLevel());
        }
        
    }

    public static void displayApplications(User user) {
        System.out.println("\n=== Applications ===");
        CompanyRepresentative cuser = (CompanyRepresentative) user;
        String filePath = "../../data/Internship_Applications_List.csv";
        String[][] data = CSVLoader.read(filePath);

        String[] header = data[0];
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
        for (int i = 1; i < data.length; i++) {
            String internshipID = data[i][oppIdCol];

            // Check if this ID is in the rep's list
            boolean matches = cuser.getCreatedInternshipOpportunities()
                                 .stream()
                                 .anyMatch(o -> o.getInternshipID().equals(internshipID));

            if (matches) {
                System.out.println(String.join(" | ", data[i]));
            }
        }
    }

    public static void showSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public static void showError(String message) {
        System.out.println("✗ " + message);
    }
}