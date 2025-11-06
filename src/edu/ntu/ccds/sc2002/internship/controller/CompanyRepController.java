package edu.ntu.ccds.sc2002.internship.controller;

import edu.ntu.ccds.sc2002.internship.model.User;
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
                view.showSuccess("Feature coming soon");
                break;
            case "2":
                view.showSuccess("Feature coming soon");
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

    
}