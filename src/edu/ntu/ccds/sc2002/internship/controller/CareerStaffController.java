package edu.ntu.ccds.sc2002.internship.controller;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.CareerStaffView;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.Report;
import edu.ntu.ccds.sc2002.internship.model.Level;
import edu.ntu.ccds.sc2002.internship.model.Status;

import java.util.Scanner;

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

            case "4": 
                handleApproveOpportunity(staff);
                break;

            case "5":
                handleViewAllApplications(staff);
                break;

            case "6":
                handleViewWithdrawalRequests(staff);
                break;

            case "7":
                handleApproveWithdrawalRequest(staff);
                break;

            case "8":
                handleGenerateReport(staff);
                break;

            case "9": // Logout
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
        // 1. Get pending reps from AuthController
        List<CompanyRepresentative> pendingReps = authController.getPendingCompanyReps();
        
        if (pendingReps.isEmpty()) {
            careerStaffView.showError("No pending company representatives.");
            return;
        }
        
        careerStaffView.displayPendingCompanyReps(pendingReps);
        
        // 2. Ask user for the ID of the rep to approve
        String repId = careerStaffView.getInRepId();
        
        // 3. Find the rep by ID
        CompanyRepresentative selectedRep = pendingReps.stream()
                .filter(rep -> rep.getUserId().equals(repId))
                .findFirst()
                .orElse(null);
        
        if (selectedRep == null) {
            careerStaffView.showError("Company Representative not found.");
            return;
        }
        
        // 4. Call model to approve
        boolean success = staff.authoriseComRepAcc(selectedRep);
        
        if (success) {
            careerStaffView.showSuccess("Company Representative approved successfully!");
        } else {
            careerStaffView.showError("Failed to approve the Company Representative.");
        }
    }


    /**
     * Handles viewing all internship opportunities.
     * CONTROLLER: Gets data from Model, tells View to display.
     */
    private void handleViewAllOpportunities(CareerStaff staff) {
        List<InternshipOpportunity> opportunities = authController.getAllOpportunities();
        if (opportunities.isEmpty()) {
            careerStaffView.showError("No internship opportunities found.");
            return;
        }
        careerStaffView.displayAllOpportunities(opportunities);
    }

    private void handleApproveOpportunity(CareerStaff staff) {
        // Implementation for approving internship opportunity
        List<InternshipOpportunity> pendingOpps = authController.getPendingOpportunities();
        
        if (pendingOpps.isEmpty()) {
            careerStaffView.showError("No pending internship opportunities.");
            return;
        }
        
        careerStaffView.displayPendingInternshipOpportunities(pendingOpps);
        
        // 2. Ask user for the ID of the rep to approve
        String oppId = careerStaffView.getInCarOppId();
        
        // 3. Find the rep by ID
        InternshipOpportunity selectedOpp = pendingOpps.stream()
                .filter(opp -> opp.getInternshipID().equals(oppId))
                .findFirst()
                .orElse(null);
        
        if (selectedOpp == null) {
            careerStaffView.showError("Internship Opportunity not found.");
            return;
        }
        
        // 4. Call model to approve
        boolean success = staff.approveOpportunity(selectedOpp);
        
        if (success) {
            careerStaffView.showSuccess("Internship Opportunity approved successfully!");
        } else {
            careerStaffView.showError("Failed to approve the Internship Opportunity.");
        } 
    }

    /**
     * Handles viewing all applications.
     * CONTROLLER: Gets data from Model, tells View to display.
     */
    private void handleViewAllApplications(CareerStaff staff) {
        List<InternshipApplication> applications = authController.getAllApplications();
        if (applications.isEmpty()) {
            careerStaffView.showError("No applications found.");
            return;
        }
        careerStaffView.displayAllApplications(applications);
    }

    private void handleViewWithdrawalRequests(CareerStaff staff) {
        List<InternshipApplication> withdrawalRequests = authController.getPendingWithdrawals();
        if (withdrawalRequests.isEmpty()) {
            careerStaffView.showError("No withdrawal requests found.");
            return;
        }
        careerStaffView.displayPendingWithdrawalRequests(withdrawalRequests);
    }

    private void handleApproveWithdrawalRequest(CareerStaff staff) {
        // Implementation for approving withdrawal request
        List<InternshipApplication> withdrawalRequests = authController.getPendingWithdrawals();
        
        if (withdrawalRequests.isEmpty()) {
            careerStaffView.showError("No withdrawal requests found.");
            return;
        }
        
        careerStaffView.displayPendingWithdrawalRequests(withdrawalRequests);
        
        // 2. Ask user for the ID of the application to approve
        System.out.print("Enter the ID of the internship application to approve withdrawal: ");
        String appId = careerStaffView.getInAppWithId();
        
        // 3. Find the application by ID
        InternshipApplication selectedApp = withdrawalRequests.stream()
                .filter(app -> app.getApplicationID().equals(appId))
                .findFirst()
                .orElse(null);
        
        if (selectedApp == null) {
            careerStaffView.showError("Internship Application not found.");
            return;
        }
        
        // 4. Call model to approve
        boolean success = staff.approveWithdrawal(selectedApp);
        
        if (success) {
            careerStaffView.showSuccess("Internship Application withdrawal approved successfully!");
        } else {
            careerStaffView.showError("Failed to approve the Internship Application withdrawal.");
        } 
    }

    private void handleGenerateReport(CareerStaff staff) {
        Filter f = careerStaffView.getInFilterReport();
        Report r = new Report(f, staff);
        careerStaffView.showReport(r);
    }

    /**
     * Gets pending company representatives.
     * CONTROLLER LAYER: Returns data for View to display.
     */
    public List<CompanyRepresentative> getPendingCompanyReps() {
        return authController.getPendingCompanyReps();
    }
}