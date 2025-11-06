package edu.ntu.ccds.sc2002.internship.controller;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.CareerStaffView;

/**
 * Controller for Career Staff operations.
 * CONTROLLER LAYER: Coordinates between Model and View.
 * - Handles routing logic
 * - Coordinates View and Model
 * - Does NOT print to console (View's responsibility)
 * - Does NOT contain business logic (Model's responsibility)
 */
public class CareerStaffController {
    private final AuthController authController;
    private final CareerStaffView careerStaffView;

    public CareerStaffController(AuthController authController, CareerStaffView careerStaffView) {
        this.authController = authController;
        this.careerStaffView = careerStaffView;
    }

    /**
     * Main controller method that handles career staff menu flow.
     * CONTROLLER: Handles routing, calls Model, tells View what to display.
     */
    public boolean handleCareerStaffMenu(User user) {
        if (!(user instanceof CareerStaff)) {
            return false;
        }

        CareerStaff staff = (CareerStaff) user;

        // View: Display menu and get choice
        careerStaffView.showDashboard(user);
        String choice = careerStaffView.getMenuChoice();

        // Controller: Route based on choice
        switch (choice) {
            case "1": // View Pending Company Representatives
                handleViewPendingCompanyReps();
                break;

            case "2": // Approve Company Representative
                handleApproveCompanyRep(staff);
                break;

            case "3": // View All Internship Opportunities
                handleViewAllOpportunities(staff);
                break;

            case "4": // View Applications
                handleViewAllApplications(staff);
                break;

            case "5": // Logout
                careerStaffView.showLogout();
                return true;

            default:
                careerStaffView.showInvalidChoice();
        }

        return false;
    }

    /**
     * Handles viewing pending company representatives.
     * CONTROLLER: Gets data from Model, tells View to display.
     */
    private void handleViewPendingCompanyReps() {
        List<CompanyRepresentative> pendingReps = authController.getPendingCompanyReps();
        careerStaffView.displayPendingCompanyReps(pendingReps);
    }

    /**
     * Handles approving a company representative.
     * CONTROLLER: Gets input from View, calls Model, displays result via View.
     */
    private void handleApproveCompanyRep(CareerStaff staff) {
        // TODO: Implement approve company rep
        careerStaffView.showError("Feature not yet implemented.");
    }

    /**
     * Handles viewing all internship opportunities.
     * CONTROLLER: Gets data from Model, tells View to display.
     */
    private void handleViewAllOpportunities(CareerStaff staff) {
        // TODO: Implement view all opportunities
        careerStaffView.showError("Feature not yet implemented.");
    }

    /**
     * Handles viewing all applications.
     * CONTROLLER: Gets data from Model, tells View to display.
     */
    private void handleViewAllApplications(CareerStaff staff) {
        // TODO: Implement view all applications
        careerStaffView.showError("Feature not yet implemented.");
    }

    /**
     * Gets pending company representatives.
     * CONTROLLER LAYER: Returns data for View to display.
     */
    public List<CompanyRepresentative> getPendingCompanyReps() {
        return authController.getPendingCompanyReps();
    }
}