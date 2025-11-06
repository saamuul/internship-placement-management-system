package edu.ntu.ccds.sc2002.internship.main;

import edu.ntu.ccds.sc2002.internship.controller.AuthController;
import edu.ntu.ccds.sc2002.internship.view.MainView;

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
        String studentFile = "data/student_list.csv";
        String staffFile = "data/staff_list.csv";
        String companyRepFile = "data/company_representative_list.csv";

        // Initialize authentication controller
        AuthController auth = new AuthController(studentFile, staffFile, companyRepFile);

        // Initialize main view (presentation layer)
        // Note: All role-specific controllers are created inside MainView with their
        // Views
        // This ensures proper MVC: Each Controller gets a reference to its View
        MainView view = new MainView(auth);

        // Start the application
        view.start();
    }
}
