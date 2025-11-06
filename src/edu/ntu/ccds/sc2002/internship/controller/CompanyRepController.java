package edu.ntu.ccds.sc2002.internship.controller;
import java.util.Scanner;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;
<<<<<<< HEAD
import edu.ntu.ccds.sc2002.internship.model.Level;
=======
>>>>>>> 780658416d53fb694c0bc06dfdd5ed3d589ff1af

/**
 * Controller for Company Representative operations.
 * CONTROLLER LAYER: Coordinates between Model and View.
 * - Handles routing logic
 * - Coordinates View and Model
 * - Does NOT print to console (View's responsibility)
 * - Does NOT contain business logic (Model's responsibility)
 */
public class CompanyRepController {
<<<<<<< HEAD
    Scanner sc = new Scanner(System.in);
    public CompanyRepController() {
        // Initialize controller
        
=======
    private final CompanyRepView companyRepView;

    public CompanyRepController(CompanyRepView companyRepView) {
        this.companyRepView = companyRepView;
>>>>>>> 780658416d53fb694c0bc06dfdd5ed3d589ff1af
    }

    /**
     * Main controller method that handles company rep menu flow.
     * CONTROLLER: Handles routing, calls Model, tells View what to display.
     */
    public boolean handleCompanyRepMenu(User user) {
        if (!(user instanceof CompanyRepresentative)) {
            return false;
        }

        CompanyRepresentative rep = (CompanyRepresentative) user;

        // View: Display menu and get choice
        companyRepView.showDashboard(user);
        String choice = companyRepView.getMenuChoice();

        // Controller: Route based on choice
        switch (choice) {
<<<<<<< HEAD
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
=======
            case "1": // Create Internship Opportunity
                handleCreateInternshipOpportunity(rep);
                break;

            case "2": // View Applications
                handleViewApplications(rep);
                break;

            case "3": // Manage Opportunities
                handleManageOpportunities(rep);
>>>>>>> 780658416d53fb694c0bc06dfdd5ed3d589ff1af
                break;

            case "4": // Logout
                companyRepView.showLogout();
                return true;

            default:
                companyRepView.showInvalidChoice();
        }

        return false;
    }

    /**
     * Handles creating an internship opportunity.
     * CONTROLLER: Gets input from View, calls Model, displays result via View.
     */
    private void handleCreateInternshipOpportunity(CompanyRepresentative rep) {
        // TODO: Implement internship opportunity creation
        companyRepView.showError("Feature not yet implemented.");
    }

    /**
     * Handles viewing applications.
     * CONTROLLER: Gets data from Model, tells View to display.
     */
    private void handleViewApplications(CompanyRepresentative rep) {
        // TODO: Implement view applications
        companyRepView.showError("Feature not yet implemented.");
    }

    /**
     * Handles managing opportunities.
     * CONTROLLER: Gets input from View, calls Model, displays result via View.
     */
    private void handleManageOpportunities(CompanyRepresentative rep) {
        // TODO: Implement manage opportunities
        companyRepView.showError("Feature not yet implemented.");
    }
}