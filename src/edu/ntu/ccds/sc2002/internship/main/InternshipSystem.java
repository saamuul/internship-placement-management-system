package edu.ntu.ccds.sc2002.internship.main;

import edu.ntu.ccds.sc2002.internship.controller.AuthController;
import edu.ntu.ccds.sc2002.internship.controller.CareerStaffController;
import edu.ntu.ccds.sc2002.internship.controller.CompanyRepController;
import edu.ntu.ccds.sc2002.internship.controller.StudentController;
import edu.ntu.ccds.sc2002.internship.view.MainView;

/**
 * Main entry point for the Internship Placement Management System.
 * 
 * Architecture:
 * - model/ - Data entities and enums (User, Internship, Application, etc.)
 * - controller/ - Business logic and coordination (StudentController, etc.)
 * - view/ - User interface and display (StudentView, MainView, etc.)
 * - util/ - Helper classes for file I/O, validation, date handling
 * - main/ - Application entry point
 * - data/ - CSV files for data storage
 * - docs/ - Project documentation, diagrams, and reports
 */
public class InternshipSystem {
    public static void main(String[] args) {
        // Data file paths
        String studentFile = "data/student_list.csv";
        String staffFile = "data/staff_list.csv";
        String companyRepFile = "data/sample_company_representative_list.csv";

        // Initialize controllers (business logic layer)
        AuthController auth = new AuthController(studentFile, staffFile, companyRepFile);
        StudentController stu = new StudentController();
        CompanyRepController comp = new CompanyRepController();
        CareerStaffController staff = new CareerStaffController(auth);

        // Initialize main view (presentation layer)
        MainView view = new MainView(auth, stu, comp, staff);

        // Start the application
        view.start();
    }
}
