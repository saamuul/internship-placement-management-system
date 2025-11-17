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
import edu.ntu.ccds.sc2002.internship.model.Interview;
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

    private List<String> activityLog = new ArrayList<>();

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
        view.showDashboard(user, activityLog);
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
                handleEditOpportunity(user);
                break;
            case "11":
                handleDeleteOpportunity(user);
            case "12": // View Proposed Interviews
                handleViewProposedInterviews(user.getUserId());
                break;
            case "13": // Propose Interview
                handleProposeInterview(user.getUserId());
                break;
            case "14": // Confirm Interview
                handleConfirmInterview(user.getUserId());
                break;
            case "15":
                return handleChangePassword(user);
            case "16":
                cachedApplications = null;
                cachedOpportunities = null;
                applicationFilterApplied = false;
                opportunityFilterApplied = false;
                activityLog.clear(); 
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
            activityLog.add("Created internship opportunity: " + title);
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
            activityLog.add("Toggled visibility for opportunity ID: " + input.getId());
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
            activityLog.add("Reviewed application ID: " + choice + " to status: " + status);
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
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

        boolean anyFilterApplied =  filter.getLevel() != null ||
                (filter.getPreferredMajor() != null && !filter.getPreferredMajor().isEmpty()) ||
                (filter.getRepName() != null && !filter.getRepName().isEmpty()) ||
                filter.getStatus() != null ||
                filter.isVisibility() != null ||
                (filter.getClosingDate() != null && !filter.getClosingDate().isEmpty());

        if (anyFilterApplied) {
            view.showSuccess("Filter applied. Showing " + cachedApplications.size() + " application(s).");
        } else {
            view.showSuccess("No filter applied. Showing all " + cachedApplications.size() + " application(s).");
        }
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

    public void handleEditOpportunity(User user) {
        CompanyRepresentative companyRep = (CompanyRepresentative) user;
        
        // First show opportunities
        handleViewOpportunities(user);
        
        // Get opportunity ID to edit
        String opportunityId = view.getOpportunityIdForEdit();
        
        // Fetch the existing opportunity
        List<InternshipOpportunity> myOpps = companyRepService.getCreatedOpportunities(user.getUserId());
        InternshipOpportunity existingOpp = null;
        for (InternshipOpportunity opp : myOpps) {
            if (opp.getInternshipID().equals(opportunityId)) {
                existingOpp = opp;
                break;
            }
        }
        
        if (existingOpp == null) {
            view.showError("Opportunity not found or you don't have permission to edit it.");
            return;
        }
        
        // Display current details
        view.displayCurrentOpportunityDetails(existingOpp);
        
        // Get new values with validation
        String title, desc, major, levelStr, openStr, closeStr;
        int slots;
        
        // TITLE
        while (true) {
            try {
                String input = view.promptForFieldUpdate("title", existingOpp.getTitle());
                title = input.isEmpty() ? existingOpp.getTitle() : validator.requireNonEmpty(input);
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
        
        // DESCRIPTION
        while (true) {
            try {
                String input = view.promptForFieldUpdate("description", existingOpp.getDescription());
                desc = input.isEmpty() ? existingOpp.getDescription() : validator.requireNonEmpty(input);
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
        
        // MAJOR
        while (true) {
            try {
                String input = view.promptForFieldUpdate("preferred major", existingOpp.getPrefMajor());
                major = input.isEmpty() ? existingOpp.getPrefMajor() : validator.requireNonEmpty(input);
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
        
        // LEVEL
        while (true) {
            try {
                String input = view.promptForFieldUpdate("level", existingOpp.getLevel().toString());
                levelStr = input.isEmpty() ? existingOpp.getLevel().toString() : validator.parseLevel(input);
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
        
        // OPEN DATE
        while (true) {
            try {
                String input = view.promptForFieldUpdate("open date", existingOpp.getOpenDate());
                openStr = input.isEmpty() ? existingOpp.getOpenDate() : validator.parseDate(input).toString();
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
        
        // CLOSE DATE
        while (true) {
            try {
                String input = view.promptForFieldUpdate("close date", existingOpp.getCloseDate());
                closeStr = input.isEmpty() ? existingOpp.getCloseDate() : validator.parseCloseDate(input, openStr).toString();
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
        
        // SLOTS
        while (true) {
            try {
                String input = view.promptForFieldUpdate("number of slots", String.valueOf(existingOpp.getNumOfSlots()));
                slots = input.isEmpty() ? existingOpp.getNumOfSlots() : validator.parseIntInRange(input, 1, 10);
                break;
            } catch (Exception e) {
                view.showError(e.getMessage());
            }
        }
        
        // Perform update
        Level levelEnum = Level.valueOf(levelStr);
        OperationResult result = companyRepService.editOpportunity(
                companyRep.getUserId(), opportunityId, title, desc, levelEnum, major, openStr, closeStr, slots);
        
        if (result.isSuccess()) {
            activityLog.add("Edited opportunity ID: " + opportunityId);
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }
    }
    
    public void handleDeleteOpportunity(User user) {
        // First show opportunities
        handleViewOpportunities(user);
        
        // Get opportunity ID to delete
        String opportunityId = view.getOpportunityIdForDelete();
        
        // Confirm deletion
        if (!view.confirmDelete(opportunityId)) {
            view.showError("Deletion cancelled.");
            return;
        }
        
        // Perform deletion
        OperationResult result = companyRepService.deleteOpportunity(user.getUserId(), opportunityId);
        
        if (result.isSuccess()) {
            activityLog.add("Deleted opportunity ID: " + opportunityId);
            view.showSuccess(result.getMessage());
            // Clear cache to refresh the list
            cachedOpportunities = null;
            opportunityFilterApplied = false;
        } else {
            view.showError(result.getMessage());
        }
    }
    
    public void handleViewProposedInterviews(String companyRepId) {
        List<Interview> interviews = companyRepService.getProposedInterviews(companyRepId);
        if (interviews.isEmpty()) {
            view.showError("No proposed interviews found for your internships.");
        } else {
            view.displayProposedInterviews(interviews);
        }
    }
    
    public void handleProposeInterview(String companyRepId) {
        System.out.print("Enter Internship ID to propose interview: ");
        String internshipId = view.getInternshipIdInput();
        String studentId = view.getStudentIdInput();
        System.out.print("Enter proposed time (e.g., 2025-11-20 14:00): ");
        String proposedTime = view.getConfirmedTimeInput();
        
        companyRepService.proposeInterview(companyRepId, internshipId, studentId, proposedTime);
        activityLog.add("Proposed interview for student " + studentId + " on internship " + internshipId);
        view.showSuccess("Interview time proposed successfully.");
    }

    public void handleConfirmInterview(String companyRepId) {
        // First show proposed interviews
        handleViewProposedInterviews(companyRepId);
        
        System.out.println("\nSelect an interview to confirm:");
        String internshipId = view.getInternshipIdInput();
        String studentId = view.getStudentIdInput();
        String confirmedTime = view.getConfirmedTimeInput();
        
        companyRepService.confirmInterview(companyRepId, internshipId, studentId, confirmedTime);
        activityLog.add("Confirmed interview for student " + studentId + " on internship " + internshipId);
        view.showSuccess("Interview confirmed.");
    }
}