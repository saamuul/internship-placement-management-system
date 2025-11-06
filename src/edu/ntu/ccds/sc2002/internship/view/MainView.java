package edu.ntu.ccds.sc2002.internship.view;

import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.controller.AuthController;
import edu.ntu.ccds.sc2002.internship.controller.AuthResult;
import edu.ntu.ccds.sc2002.internship.controller.CareerStaffController;
import edu.ntu.ccds.sc2002.internship.controller.CompanyRepController;
import edu.ntu.ccds.sc2002.internship.controller.RegistrationResult;
import edu.ntu.ccds.sc2002.internship.controller.StudentController;
import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * Main View class that orchestrates the CLI interface.
 * MAIN VIEW: Entry point for user interaction.
 * - Handles login/registration
 * - Routes to appropriate role-specific Views
 * - Does NOT handle role-specific display logic (delegated to role Views)
 */
public class MainView {
    private final AuthController authController;
    private final StudentController studentController;
    private final CompanyRepController companyController;
    private final CareerStaffController staffController;

    // View instances - responsible only for display
    private final StudentView studentView;
    private final CompanyRepView companyRepView;
    private final CareerStaffView careerStaffView;

    private final Scanner sc = new Scanner(System.in);

    public MainView(AuthController auth) {
        this.authController = auth;

        // Initialize view instances with shared scanner
        this.studentView = new StudentView(sc);
        this.companyRepView = new CompanyRepView(sc);
        this.careerStaffView = new CareerStaffView(sc);

        // Initialize controllers with their views (MVC pattern)
        this.studentController = new StudentController(studentView);
        this.companyController = new CompanyRepController(companyRepView);
        this.staffController = new CareerStaffController(auth, careerStaffView);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Internship Placement Management System ===");
            System.out.println("1) Login");
            System.out.println("2) Register as Company Representative");
            System.out.println("3) Exit");
            System.out.print("Select option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> login();
                case "2" -> registerRep();
                case "3" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void login() {
        System.out.print("User ID: ");
        String id = sc.nextLine();
        System.out.print("Password: ");
        String pw = sc.nextLine();

        AuthResult result = authController.login(id, pw);
        if (!result.isSuccess()) {
            System.out.println(result.getMessage());
            return;
        }

        User user = result.getUser();
        switch (user.getRole()) {
            case STUDENT -> studentMenu(user);
            case COMPANY_REP -> companyMenu(user);
            case CAREER_STAFF -> staffMenu(user);
        }
    }

    /**
     * Handles student menu.
     * MAIN VIEW: Delegates to StudentController which handles all routing logic.
     */
    private void studentMenu(User u) {
        while (true) {
            // Controller handles all routing, View interaction, and Model calls
            boolean logout = studentController.handleStudentMenu(u);
            if (logout) {
                return;
            }
        }
    }

    /**
     * Handles company representative menu.
     * MAIN VIEW: Delegates to CompanyRepController which handles all routing logic.
     */
    private void companyMenu(User u) {
        while (true) {
            // Controller handles all routing, View interaction, and Model calls
            boolean logout = companyController.handleCompanyRepMenu(u);
            if (logout) {
                return;
            }
        }
    }

    /**
     * Handles career staff menu.
     * MAIN VIEW: Delegates to CareerStaffController which handles all routing
     * logic.
     */
    private void staffMenu(User u) {
        while (true) {
            // Controller handles all routing, View interaction, and Model calls
            boolean logout = staffController.handleCareerStaffMenu(u);
            if (logout) {
                return;
            }
        }
    }

    /* For Company Representatives */

    private void registerRep() {
        System.out.println("\n-- Company Rep Registration --");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Company: ");
        String company = sc.nextLine();
        System.out.print("Department: ");
        String dept = sc.nextLine();
        System.out.print("Position: ");
        String pos = sc.nextLine();

        RegistrationResult res = authController.registerCompanyRep(email, name, email, company, dept, pos);
        System.out.println(switch (res) {
            case SUCCESS -> "Registration successful. Awaiting approval.";
            case ALREADY_EXISTS -> "Account already exists.";
            default -> "Registration failed.";
        });
    }
}
