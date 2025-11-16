package edu.ntu.ccds.sc2002.internship.controller;

import edu.ntu.ccds.sc2002.internship.dto.AuthResult;
import edu.ntu.ccds.sc2002.internship.enums.RegistrationResult;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.MainView;

/**
 * Controller for main menu operations.
 * CONTROLLER LAYER: Coordinates between Model and View for main menu.
 * - Handles routing logic for login/registration
 * - Coordinates View and AuthController
 * - Does NOT print to console (View's responsibility)
 * - Does NOT contain business logic (Model/AuthController's responsibility)
 */
public class MainController {
    private final AuthController authController;
    private final StudentController studentController;
    private final CompanyRepController companyController;
    private final CareerStaffController staffController;
    private final MainView mainView;

    public MainController(AuthController authController,
            StudentController studentController,
            CompanyRepController companyController,
            CareerStaffController staffController,
            MainView mainView) {
        this.authController = authController;
        this.studentController = studentController;
        this.companyController = companyController;
        this.staffController = staffController;
        this.mainView = mainView;
    }

    /**
     * Main controller method that handles main menu flow.
     * CONTROLLER: Handles routing, calls AuthController, tells View what to
     * display.
     */
    public boolean handleMainMenu() {
        // View: Display menu and get choice
        mainView.showMainMenu();
        String choice = mainView.getMenuChoice();

        // Controller: Route based on choice
        switch (choice) {
            case "1": // Login
                handleLogin();
                break;

            case "2": // Register as Company Representative
                handleRegisterCompanyRep();
                break;

            case "3": // Exit
                mainView.showGoodbye();
                return true; // Signal to exit

            default:
                mainView.showInvalidChoice();
        }

        return false; // Continue running
    }

    /**
     * Handles login flow.
     * CONTROLLER: Gets input from View, calls AuthController, routes based on
     * result.
     */
    private void handleLogin() {
        // View: Get credentials
        String userId = mainView.getUserIdInput();
        String password = mainView.getPasswordInput();

        // Model: Authenticate
        AuthResult result = authController.login(userId, password);

        // Controller: Check result and route
        if (!result.isSuccess()) {
            mainView.showError(result.getMessage());
            return;
        }

        // Controller: Route to appropriate menu based on user role
        User user = result.getUser();
        switch (user.getRole()) {
            case STUDENT -> handleStudentMenu(user);
            case COMPANY_REP -> handleCompanyRepMenu(user);
            case CAREER_STAFF -> handleCareerStaffMenu(user);
        }
    }

    /**
     * Handles student menu loop.
     * CONTROLLER: Delegates to StudentController.
     */
    private void handleStudentMenu(User user) {
        while (true) {
            boolean logout = studentController.handleStudentMenu(user);
            if (logout) {
                return;
            }
        }
    }

    /**
     * Handles company representative menu loop.
     * CONTROLLER: Delegates to CompanyRepController.
     */
    private void handleCompanyRepMenu(User user) {
        while (true) {
            boolean logout = companyController.handleCompanyRepMenu(user);
            if (logout) {
                return;
            }
        }
    }

    /**
     * Handles career staff menu loop.
     * CONTROLLER: Delegates to CareerStaffController.
     */
    private void handleCareerStaffMenu(User user) {
        while (true) {
            boolean logout = staffController.handleCareerStaffMenu(user);
            if (logout) {
                return;
            }
        }
    }

    /**
     * Handles company representative registration.
     * CONTROLLER: Gets input from View, calls AuthController, displays result via
     * View.
     */
    private void handleRegisterCompanyRep() {
        // View: Get registration information
        String name = mainView.getNameInput();
        String email = mainView.getEmailInput();
        String company = mainView.getCompanyInput();
        String department = mainView.getDepartmentInput();
        String position = mainView.getPositionInput();

        // Model: Register the company representative
        RegistrationResult result = authController.registerCompanyRep(email, name, email, company, department,
                position);

        // Controller: Route based on result
        switch (result) {
            case SUCCESS:
                mainView.showSuccess("Registration successful. Awaiting approval.");
                break;
            case ALREADY_EXISTS:
                mainView.showError("Account already exists.");
                break;
            default:
                mainView.showError("Registration failed.");
        }
    }
}
