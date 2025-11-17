package edu.ntu.ccds.sc2002.internship.controller;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.dto.ToggleVisHelper;
import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.service.interfaces.ICompanyRepService;
import edu.ntu.ccds.sc2002.internship.util.InputValidation;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;

/**
 * Controller for Company Representative operations.
 * CONTROLLER LAYER: Coordinates between Service and View.
 * Uses Dependency Injection for service layer.
 */
public class CompanyRepController {
    private final CompanyRepView view;
    private final ICompanyRepService companyRepService;
    private final InputValidation validator;

    // Filter state persistence - maintains filter settings across menu pages
    private List<InternshipOpportunity> cachedOpportunities = null;
    private List<InternshipApplication> cachedApplications = null;
    private boolean opportunityFilterApplied = false;
    private boolean applicationFilterApplied = false;

    public CompanyRepController(CompanyRepView view, ICompanyRepService companyRepService, InputValidation validator) {
        this.view = view;
        this.companyRepService = companyRepService;
        this.validator = validator;
    }

    /**
     * Main controller method for company representative menu.
     * Returns true if user wants to logout.
     */
    public boolean handleCompanyRepMenu(User user) {
        view.showDashboard(user);
        String choice = view.getMenuChoice();

        switch (choice) {
            case "1":
                handleCreateInternOpp(user);
                break;
            case "2":
                handleViewOpportunities(user);
                break;
            case "3":
                handleFilterOpportunities(user);
                break;
            case "4":
                handleClearOpportunityFilters(user);
                break;
            case "5":
                handleViewApplications(user);
                break;
            case "6":
                handleFilterApplications(user);
                break;
            case "7":
                handleClearApplicationFilters(user);
                break;
            case "8":
                handleReviewApplications(user);
                break;
            case "9":
                handleManageOpportunities(user);
                break;
            case "10":
                return handleChangePassword(user);
            case "11":
                view.showLogout();
                return true;
            default:
                view.showInvalidChoice();
        }
        return false;
    }

    public void handleCreateInternOpp(User user) {
        CompanyRepresentative companyRep = (CompanyRepresentative) user;
        String title, desc, major, levelStr, openStr, closeStr;
        int slots;

        // TITLE
        while (true) {
            try {
                title = validator.requireNonEmpty(view.inputTitle());
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }

        // DESCRIPTION
        while (true) {
            try {
                desc = validator.requireNonEmpty(view.inputDescription());
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
                continue;
            }
        }

        // MAJOR
        while (true) {
            try {
                major = validator.requireNonEmpty(view.inputPreferredMajor());
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }

        // LEVEL
        while (true) {
            try {
                levelStr = validator.parseLevel(view.inputLevel());
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }

        // OPEN DATE
        while (true) {
            try {
                openStr = validator.parseDate(view.inputOpenDate()).toString();
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }

        // CLOSE DATE
        while (true) {
            try {
                closeStr = validator.parseCloseDate(view.inputCloseDate(), openStr).toString();
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }

        // SLOTS
        while (true) {
            try {
                slots = validator.parseIntInRange(view.inputNumOfSlots(), 0, 10);
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }

        // Service operation after validation
        Level levelEnum = Level.valueOf(levelStr);
        OperationResult result = companyRepService.createOpportunity(
                companyRep.getUserId(), title, desc, levelEnum, major, openStr, closeStr, slots);

        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }

        return;
    }

    public void handleViewApplications(User user) {
        if (!applicationFilterApplied || cachedApplications == null) {
            cachedApplications = companyRepService.getApplicationsForMyOpportunities(user.getUserId());
        }
        view.displayApplications(cachedApplications);
        return;
    }

    public void handleViewOpportunities(User user) {
        if (!opportunityFilterApplied || cachedOpportunities == null) {
            cachedOpportunities = companyRepService.getCreatedOpportunities(user.getUserId());
        }
        view.displayOpportunities(cachedOpportunities);
        return;
    }

    public void handleManageOpportunities(User user) {
        handleViewOpportunities(user);
        ToggleVisHelper input = view.viewToggleVisibility();
        boolean visible = Boolean.parseBoolean(input.getState());
        OperationResult result = companyRepService.toggleVisibility(user.getUserId(), input.getId(), visible);
        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }
        return;
    }

    public void handleReviewApplications(User user) {
        OperationResult result = OperationResult.failure("No pending applications at the moment.");
        List<InternshipApplication> resultList = companyRepService.getPendingApplications(user.getUserId());
        if (resultList == null || resultList.isEmpty()) {
            view.showError(result.getMessage());
            return;
        }
        view.displayApplications(resultList);
        String choice = view.inputApplicationID();
        Status status = view.inputStatus();

        for (InternshipApplication row : resultList) {
            if ((row.getStatus() == Status.PENDING) && (row.getApplicationID().equals(choice))) {
                result = companyRepService.reviewApplication(user.getUserId(), row.getApplicationID(), status);
            }
        }

        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }

        // Refresh view to show updated data
        System.out.println();
        List<InternshipApplication> updatedList = companyRepService.getPendingApplications(user.getUserId());
        if (updatedList != null && !updatedList.isEmpty()) {
            view.displayApplications(updatedList);
        }
        return;
    }

    // Handles filtering opportunities
    private void handleFilterOpportunities(User user) {
        Filter filter = view.getOpportunityFilterInput();
        List<InternshipOpportunity> allOpportunities = companyRepService.getCreatedOpportunities(user.getUserId());

        List<InternshipOpportunity> filtered = new ArrayList<>();
        for (InternshipOpportunity opp : allOpportunities) {
            boolean matches = true;

            if (filter.getLevel() != null && opp.getLevel() != filter.getLevel()) {
                matches = false;
            }
            if (filter.getPreferredMajor() != null &&
                    !opp.getPrefMajor().toLowerCase().contains(filter.getPreferredMajor().toLowerCase())) {
                matches = false;
            }
            if (filter.getStatus() != null && opp.getStatus() != filter.getStatus()) {
                matches = false;
            }
            if (filter.isVisibility() != null && opp.getVisibility() != filter.isVisibility()) {
                matches = false;
            }
            if (filter.getClosingDate() != null && !opp.getCloseDate().equals(filter.getClosingDate())) {
                matches = false;
            }

            if (matches)
                filtered.add(opp);
        }

        cachedOpportunities = filtered;
        opportunityFilterApplied = true;
        view.displayOpportunities(cachedOpportunities);
        view.showSuccess("Filter applied. Showing " + cachedOpportunities.size() + " opportunity(s).");
    }

    // Handles clearing opportunity filters
    private void handleClearOpportunityFilters(User user) {
        cachedOpportunities = null;
        opportunityFilterApplied = false;
        view.showSuccess("Filters cleared.");
        handleViewOpportunities(user);
    }

    // Handles filtering applications
    private void handleFilterApplications(User user) {
        Filter filter = view.getApplicationFilterInput();
        List<InternshipApplication> allApplications = companyRepService
                .getApplicationsForMyOpportunities(user.getUserId());

        List<InternshipApplication> filtered = new ArrayList<>();
        for (InternshipApplication app : allApplications) {
            boolean matches = true;

            if (filter.getStatus() != null && app.getStatus() != filter.getStatus()) {
                matches = false;
            }
            if (filter.getRepName() != null &&
                    !app.getStudentID().toLowerCase().contains(filter.getRepName().toLowerCase())) {
                matches = false;
            }

            if (matches)
                filtered.add(app);
        }

        cachedApplications = filtered;
        applicationFilterApplied = true;
        view.displayApplications(cachedApplications);
        view.showSuccess("Filter applied. Showing " + cachedApplications.size() + " application(s).");
    }

    // Handles clearing application filters
    private void handleClearApplicationFilters(User user) {
        cachedApplications = null;
        applicationFilterApplied = false;
        view.showSuccess("Filters cleared.");
        handleViewApplications(user);
    }

    // Handles password change.
    private boolean handleChangePassword(User user) {
        // View: Get old password
        String oldPassword = view.getOldPasswordInput();

        // View: Get new password
        String newPassword = view.getNewPasswordInput();

        // View: Get password confirmation
        String confirmPassword = view.getConfirmPasswordInput();

        // Validate: Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            view.showError("Passwords do not match. Please try again.");
            return false; // Stay in menu
        }

        // Service: Change password
        OperationResult result = companyRepService.changePassword(user.getUserId(), oldPassword, newPassword);

        // View: Display result
        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
            return true; // Logout after successful password change
        } else {
            view.showError(result.getMessage());
            return false; // Stay in menu on error
        }
    }
}