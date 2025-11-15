package edu.ntu.ccds.sc2002.internship.controller;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.Level;
import edu.ntu.ccds.sc2002.internship.model.OperationResult;
import edu.ntu.ccds.sc2002.internship.model.Status;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.util.InternshipInputData;
import edu.ntu.ccds.sc2002.internship.util.ToggleVisHelper;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;

/**
 * Controller for Company Representative operations.
 * CONTROLLER LAYER: Coordinates between Model and View.
 */
public class CompanyRepController {
    private final CompanyRepView view;

    public CompanyRepController(CompanyRepView view) {
        this.view = view;
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
                handleViewApplications(user);
                break;
            case "3":
                handleViewOpportunities(user);
                break;
            case "4":
                handleReviewApplications(user);
                break;
            case "5":
                handleManageOpportunities(user);
                break;
            case "6":
                view.showLogout();
                return true;
            default:
                view.showInvalidChoice();
        }
        return false;
    }

    public void handleCreateInternOpp(User user) {
        InternshipInputData input = view.createOpportunityInput();
        CompanyRepresentative companyRep = (CompanyRepresentative) user;

        Level levelNum = Level.valueOf(input.level.toUpperCase());

        OperationResult result = companyRep.createInternshipOpportunity(
                input.title,
                input.description,
                levelNum,
                input.prefMajor,
                input.openDate,
                input.closeDate,
                input.numOfSlots);

        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }
        return;

    }

    public void handleViewApplications(User user) {
        CompanyRepresentative companyRep = (CompanyRepresentative) user;
        view.displayApplications(companyRep.getFilteredInternshipApplication());
        return;
    }

    public void handleViewOpportunities(User user) {
        CompanyRepresentative companyRep = (CompanyRepresentative) user;
        view.displayOpportunities(companyRep.getCreatedInternshipOpportunities());
        return;
    }

    public void handleManageOpportunities(User user) {
        CompanyRepresentative companyRep = (CompanyRepresentative) user;
        handleViewOpportunities(user);
        ToggleVisHelper input = view.viewToggleVisibility();
        OperationResult result = companyRep.toggleVisibilityForRep(input.id, input.state);
        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }
        return;

    }

    public void handleReviewApplications(User user) {
        OperationResult result = OperationResult.failure("No pending applications at the moment.");
        CompanyRepresentative companyRep = (CompanyRepresentative) user;
        List<InternshipApplication> resultList = companyRep.getPendingInternshipApplications();
        if (resultList == null || resultList.isEmpty()) {
            view.showError(result.getMessage());
            return;
        }
        view.displayApplications(resultList);
        String choice = view.inputApplicationID();
        Status status = Status.valueOf(view.inputStatus());

        for (InternshipApplication row : resultList) {
            if ((row.getStatus() == Status.PENDING) && (row.getApplicationID().equals(choice))) {
                result = companyRep.reviewApplications(row, status);
            }
        }

        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }
        return;
    }
}