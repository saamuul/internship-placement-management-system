package edu.ntu.ccds.sc2002.internship.controller;

import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;

/**
 * Controller for Company Representative operations.
 * CONTROLLER LAYER: Coordinates between Model and View.
 * - Handles routing logic
 * - Coordinates View and Model
 * - Does NOT print to console (View's responsibility)
 * - Does NOT contain business logic (Model's responsibility)
 */
public class CompanyRepController {
    private final CompanyRepView companyRepView;

    public CompanyRepController(CompanyRepView companyRepView) {
        this.companyRepView = companyRepView;
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
            case "1": // Create Internship Opportunity
                handleCreateInternshipOpportunity(rep);
                break;

            case "2": // View Applications
                handleViewApplications(rep);
                break;

            case "3": // Manage Opportunities
                handleManageOpportunities(rep);
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