package edu.ntu.ccds.sc2002.internship.main;

import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.controller.AuthController;
import edu.ntu.ccds.sc2002.internship.controller.CareerStaffController;
import edu.ntu.ccds.sc2002.internship.controller.CompanyRepController;
import edu.ntu.ccds.sc2002.internship.controller.MainController;
import edu.ntu.ccds.sc2002.internship.controller.StudentController;
import edu.ntu.ccds.sc2002.internship.view.CareerStaffView;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;
import edu.ntu.ccds.sc2002.internship.view.MainView;
import edu.ntu.ccds.sc2002.internship.view.StudentView;

/**
 * Main entry point for the Internship Placement Management System.
 * 
 * Architecture (MVC Pattern):
 * - Model (model/) - Data entities and business logic (User, Internship,
 * Application, etc.)
 * - View (view/) - User interface and display (StudentView, MainView, etc.)
 * - Controller (controller/) - Coordination between Model and View
 * (StudentController, etc.)
 * - Utility (util/) - Helper classes for file I/O, validation, date handling
 * - Main (main/) - Application entry point
 * - Data (data/) - CSV files for data storage
 * - Docs (docs/) - Project documentation, diagrams, and reports
 */
public class InternshipSystem {
    public static void main(String[] args) {
        String studentFile = "data/Student_List.csv";
        String staffFile = "data/Staff_List.csv";
        String companyRepFile = "data/Company_Representative_List.csv";

        // Shared scanner for all views
        Scanner scanner = new Scanner(System.in);

        // Initialize authentication controller (Model layer)
        AuthController authController = new AuthController(studentFile, staffFile, companyRepFile);

        // Initialize all views (View layer)
        MainView mainView = new MainView(scanner);
        StudentView studentView = new StudentView(scanner);
        CompanyRepView companyRepView = new CompanyRepView(scanner);
        CareerStaffView careerStaffView = new CareerStaffView(scanner);

        // Initialize role-specific controllers (Controller layer)
        StudentController studentController = new StudentController(studentView);
        CompanyRepController companyRepController = new CompanyRepController(companyRepView);
        CareerStaffController careerStaffController = new CareerStaffController(authController, careerStaffView);

        // Initialize main controller (Controller layer)
        MainController mainController = new MainController(
                authController,
                studentController,
                companyRepController,
                careerStaffController,
                mainView);

        // Start the application - main loop
        while (true) {
            boolean exit = mainController.handleMainMenu();
            if (exit) {
                scanner.close();
                break;
            }
        }
    }
}
