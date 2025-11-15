package edu.ntu.ccds.sc2002.internship.controller;
import java.util.ArrayList;
import java.util.List;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;
import edu.ntu.ccds.sc2002.internship.model.Level;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.Internship;
import edu.ntu.ccds.sc2002.internship.model.OperationResult;
import edu.ntu.ccds.sc2002.internship.model.Status;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;
import edu.ntu.ccds.sc2002.internship.util.InternshipInputData;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.util.ToggleVisHelper;

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


    public void handleCreateInternOpp(User user){
        InternshipInputData input = view.createOpportunityInput();
        CompanyRepresentative companyRepModel = (CompanyRepresentative) user;

        Level levelnum = Level.valueOf(input.level.toUpperCase());

        OperationResult result = companyRepModel.createInternshipOpportunity(
            input.title,
            input.description,
            levelnum,
            input.prefMajor,
            input.openDate,
            input.closeDate,
            input.numOfSlots
        );

        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }
        return;
            
    }

    public void handleViewApplications(User user){
        CompanyRepresentative cuser = (CompanyRepresentative) user;
        view.displayApplications(cuser.getFilteredInternshipApplication());
        return;
    }
    
    public void handleViewOpportunities(User user){
        CompanyRepresentative cuser = (CompanyRepresentative) user;
        view.displayOpportunities(cuser.getCreatedInternshipOpportunities());
        return;
    }

    public void handleManageOpportunities(User user){
        CompanyRepresentative cuser = (CompanyRepresentative) user;
        handleViewOpportunities(user);
        ToggleVisHelper input = view.viewToggleVisibility();
        OperationResult result = cuser.toggleVisibilityforrep(input.id, input.state);
        if (result.isSuccess()) {
            view.showSuccess(result.getMessage());
        } else {
            view.showError(result.getMessage());
        }
        return;
        
    }

    public void handleReviewApplications(User user){
        OperationResult result = OperationResult.failure("No pending applications at the moment.");
        CompanyRepresentative cuser = (CompanyRepresentative) user;
        List<InternshipApplication> resultList = cuser.getPendingInternshipApplications();
        if (resultList == null || resultList.isEmpty()){
            view.showError(result.getMessage());
            return;
        }
        view.displayApplications(resultList);
        String choice = view.inputApplicationID();
        Status status = Status.valueOf(view.inputStatus());
        
        for (InternshipApplication row : resultList){
            if ((row.getStatus() == Status.PENDING) && (row.getApplicationID().equals(choice))){
                result = cuser.reviewApplications(row, status);
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