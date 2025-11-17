package edu.ntu.ccds.sc2002.internship.controller;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.Report;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.service.interfaces.ICareerStaffService;
import edu.ntu.ccds.sc2002.internship.view.CareerStaffView;

/**
 * Controller for Career Staff operations.
 * CONTROLLER LAYER: Coordinates between Service and View.
 * - Handles routing logic
 * - Coordinates View and Service
 * - Does NOT print to console (View's responsibility)
 * - Does NOT contain business logic (Service's responsibility)
 * Uses Dependency Injection for service layer.
 */
public class CareerStaffController {
    private final CareerStaffView careerStaffView;
    private final ICareerStaffService careerStaffService;

    private List<CompanyRepresentative> cachedReps = null;
    private boolean filterApplied = false;

    public CareerStaffController(CareerStaffView careerStaffView, ICareerStaffService careerStaffService) {
        this.careerStaffView = careerStaffView;
        this.careerStaffService = careerStaffService;
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
                handleViewWithdrawalRequests(staff);
                break;

            case "6":
                handleApproveWithdrawalRequest(staff);
                break;

            case "7":
                handleGenerateReport(staff);
                break;

            case "8":
                return handleChangePassword(staff);

            case "9":
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
     * Filter state is maintained across menu navigation.
     */
    private void handleViewPendingCompanyReps() {
        // If no filter applied or cache is empty, fetch fresh data
        if (!filterApplied || cachedReps == null) {
            cachedReps = careerStaffService.getPendingCompanyReps();
        }

        // Display cached reps (filtered or fresh)
        careerStaffView.displayPendingCompanyReps(cachedReps);
    }

    /**
     * Handles approving a company representative.
     * CONTROLLER: Gets input from View, calls Model, displays result via View.
     */
    private void handleApproveCompanyRep(CareerStaff staff) {
        // Implementation for approving company representative
        List<CompanyRepresentative> pendingReps = careerStaffService.getPendingCompanyReps();

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

        // 4. Ask whether or not staff wants to approve
        boolean Approved = careerStaffView.getInRepAppr();

        // 5. Call service to approve
        OperationResult result = careerStaffService.authorizeCompanyRep(staff.getUserId(), selectedRep.getUserId(),
                Approved);

        // 6. Display result
        if (result.isSuccess()) {
            careerStaffView.showSuccess(result.getMessage());
        } else {
            careerStaffView.showError(result.getMessage());
        }
    }

    /**
     * Handles viewing all internship opportunities.
     * CONTROLLER: Gets data from Model, tells View to display.
     */
    private void handleViewAllOpportunities(CareerStaff staff) {
        List<InternshipOpportunity> opportunities = careerStaffService.getAllOpportunities();
        if (opportunities.isEmpty()) {
            careerStaffView.showError("No internship opportunities found.");
            return;
        }
        careerStaffView.displayAllOpportunities(opportunities);
    }

    private void handleApproveOpportunity(CareerStaff staff) {
        // Implementation for approving internship opportunity
        List<InternshipOpportunity> pendingOpps = careerStaffService.getPendingOpportunities();

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

        // 4. Ask whether or not staff wants to approve
        boolean Approved = careerStaffView.getInCarOppAppr();

        // 5. Call service to approve
        OperationResult result = careerStaffService.approveOpportunity(staff.getUserId(), selectedOpp.getInternshipID(),
                Approved);

        if (result.isSuccess()) {
            careerStaffView.showSuccess(result.getMessage());
        } else {
            careerStaffView.showError(result.getMessage());
        }
    }

    private void handleViewWithdrawalRequests(CareerStaff staff) {
        List<InternshipApplication> withdrawalRequests = careerStaffService.getPendingWithdrawals();
        if (withdrawalRequests.isEmpty()) {
            careerStaffView.showError("No withdrawal requests found.");
            return;
        }
        careerStaffView.displayPendingWithdrawalRequests(withdrawalRequests);
    }

    private void handleApproveWithdrawalRequest(CareerStaff staff) {
        // Implementation for approving withdrawal request
        List<InternshipApplication> withdrawalRequests = careerStaffService.getPendingWithdrawals();

        if (withdrawalRequests.isEmpty()) {
            careerStaffView.showError("No withdrawal requests found.");
            return;
        }

        careerStaffView.displayPendingWithdrawalRequests(withdrawalRequests);

        // 2. Ask user for the ID of the application to approve
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

        // 4. Ask whether or not staff wants to approve
        boolean Approved = careerStaffView.getInAppWithAppr();

        // 5. Call service to approve
        OperationResult result = careerStaffService.approveWithdrawal(staff.getUserId(), selectedApp.getApplicationID(),
                Approved);

        if (result.isSuccess()) {
            careerStaffView.showSuccess(result.getMessage());
        } else {
            careerStaffView.showError(result.getMessage());
        }
    }

    private void handleGenerateReport(CareerStaff staff) {
        Filter f = careerStaffView.getInFilterReport();
        Report r = careerStaffService.generateReport(staff.getUserId(), f);
        careerStaffView.showReport(r);
    }

    // Handles password change.
    private boolean handleChangePassword(User user) {
        // View: Get old password
        String oldPassword = careerStaffView.getOldPasswordInput();

        // View: Get new password
        String newPassword = careerStaffView.getNewPasswordInput();

        // View: Get password confirmation
        String confirmPassword = careerStaffView.getConfirmPasswordInput();

        // Validate: Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            careerStaffView.showError("Passwords do not match. Please try again.");
            return false; // Stay in menu
        }

        // Service: Change password
        OperationResult result = careerStaffService.changePassword(user.getUserId(), oldPassword, newPassword);

        // View: Display result
        if (result.isSuccess()) {
            careerStaffView.showSuccess(result.getMessage());
            return true; // Logout after successful password change
        } else {
            careerStaffView.showError(result.getMessage());
            return false; // Stay in menu on error
        }
    }
}