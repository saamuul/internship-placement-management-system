package edu.ntu.ccds.sc2002.internship.controller;

import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * Controller for Company Representative operations.
 * Handles business logic for company rep actions like creating internship
 * opportunities,
 * managing applications, and viewing student applications.
 */
public class CompanyRepController {

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
                // createInternshipOpportunity(rep);
                break;
            case "2":
                // TODO: View applications
                System.out.println("[Controller] Fetching applications...");
                // viewApplications(rep);
                break;
            case "3":
                // TODO: Manage opportunities
                System.out.println("[Controller] Managing opportunities...");
                // manageOpportunities(rep);
                break;
            case "4":
                return true; // Logout
            default:
                System.out.println("Invalid option. Please try again.");
        }

        return false;
    }
}