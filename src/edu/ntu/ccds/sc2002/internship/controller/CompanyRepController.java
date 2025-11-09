package edu.ntu.ccds.sc2002.internship.controller;

import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;
import edu.ntu.ccds.sc2002.internship.model.Level;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.OperationResult;
import edu.ntu.ccds.sc2002.internship.util.InternshipInputData;

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
                view.showSuccess("Feature coming soon");
                break;
            case "4":
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

        companyRepModel.createInternshipOpportunity(
            input.title,
            input.description,
            levelnum,
            input.prefMajor,
            input.openDate,
            input.closeDate,
            input.numOfSlots
        );

        System.out.println("Internship opportunity created successfully!");
    }

    public void handleViewApplications(User user){
        CompanyRepresentative cuser = (CompanyRepresentative) user;
        view.displayApplications(cuser.getFilteredInternshipApplication());
        return;
    }
    
    

}