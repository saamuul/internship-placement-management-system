package edu.ntu.ccds.sc2002.internship.controller;
import java.util.Scanner;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;
import edu.ntu.ccds.sc2002.internship.model.Level;

/**
 * Controller for Company Representative operations.
 * Handles business logic for company rep actions like creating internship
 * opportunities,
 * managing applications, and viewing student applications.
 */
public class CompanyRepController {
    Scanner sc = new Scanner(System.in);
    public CompanyRepController() {
        // Initialize controller
        
    }

    public boolean handleMenuChoice(User user, String choice) {
        if (!(user instanceof CompanyRepresentative)) {
            return false;
        }

        CompanyRepresentative rep = (CompanyRepresentative) user;

        switch (choice) {
            case "1":
                // TODO: Create internship opportunity
                System.out.println("[Controller] Creating internship opportunity...");
                System.out.println("Type ID: ");
                String id = sc.nextLine();
                System.out.println("Type Title: ");
                String title = sc.nextLine();
                System.out.println("Type Description: ");
                String description = sc.nextLine();
                System.out.println("Type Preferred Major: ");
                String preferredMajor = sc.nextLine();
                System.out.println("Type Application Open Date: ");
                String openDate = sc.nextLine();
                System.out.println("Type Application Closing Date: ");
                String closeDate = sc.nextLine();
                System.out.println("Type Number Of Slots: ");
                int numOfSlots = Integer.parseInt(sc.nextLine());
                System.out.println("Type Level (e.g., INTERN, ENTRY_LEVEL, etc.): ");
                String levelStr = sc.nextLine();
                System.out.println("Type Visibility (true/false): ");
                boolean visibility = Boolean.parseBoolean(sc.nextLine());
                System.out.println("Type Level: (BASIC, INTERMEDIATE, ADVANCED)");
                Level level = Level.valueOf(levelStr);

                rep.createInternshipOpportunity(id , title, description, level,
                        preferredMajor, openDate, closeDate, numOfSlots, visibility);
                break;
            case "2":
                // TODO: View applications
                System.out.println("[Controller] Fetching applications...");
                CompanyRepView.displayApplications(user);
                System.out.println("Input which ID to update: ");
                String ID = sc.nextLine();
                switch (index) {
                    case 1:
                        System.out.println("Application Status updated to Accepted.");
                        break;
                    case 2:
                        System.out.println("Application Status updated to Rejected.");
                        break;
                    default:
                        System.out.println("Invalid index.");
                }
                break;
            case "3":
                // TODO: View opportunities
                System.out.println("[Controller] Managing opportunities...");
                CompanyRepView.displayOpportunities(user);
                break;
            case "4":
                return true; // Logout
            default:
                System.out.println("Invalid option. Please try again.");
        }

        return false;
    }
}