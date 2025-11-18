package edu.ntu.ccds.sc2002.internship.main;

import java.util.Scanner;

import edu.ntu.ccds.sc2002.internship.controller.AuthController;
import edu.ntu.ccds.sc2002.internship.controller.CareerStaffController;
import edu.ntu.ccds.sc2002.internship.controller.CompanyRepController;
import edu.ntu.ccds.sc2002.internship.controller.MainController;
import edu.ntu.ccds.sc2002.internship.controller.StudentController;
import edu.ntu.ccds.sc2002.internship.repository.impl.ApplicationRepository;
import edu.ntu.ccds.sc2002.internship.repository.impl.InternshipRepository;
import edu.ntu.ccds.sc2002.internship.repository.impl.UserRepository;
import edu.ntu.ccds.sc2002.internship.repository.impl.WithdrawalRepository;
import edu.ntu.ccds.sc2002.internship.repository.impl.InterviewRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IApplicationRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInternshipRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IUserRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IWithdrawalRepository;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInterviewRepository;
import edu.ntu.ccds.sc2002.internship.service.impl.CareerStaffService;
import edu.ntu.ccds.sc2002.internship.service.impl.CompanyRepService;
import edu.ntu.ccds.sc2002.internship.service.impl.StudentService;
import edu.ntu.ccds.sc2002.internship.service.interfaces.ICareerStaffService;
import edu.ntu.ccds.sc2002.internship.service.interfaces.ICompanyRepService;
import edu.ntu.ccds.sc2002.internship.service.interfaces.IStudentService;
import edu.ntu.ccds.sc2002.internship.util.InputValidation;
import edu.ntu.ccds.sc2002.internship.view.CareerStaffView;
import edu.ntu.ccds.sc2002.internship.view.CompanyRepView;
import edu.ntu.ccds.sc2002.internship.view.MainView;
import edu.ntu.ccds.sc2002.internship.view.StudentView;

/**
 * Main entry point for the Internship Placement Management System.
 * 
 * Architecture (MVC Pattern with Dependency Injection):
 * - Model (model/) - Data entities (User, Internship, Application, etc.)
 * - View (view/) - User interface and display (StudentView, MainView, etc.)
 * - Controller (controller/) - Coordination between View and Service
 * - Service (service/) - Business logic layer
 * - Repository (repository/) - Data access layer
 * - Utility (util/) - Helper classes for file I/O, validation
 * - DTO (dto/) - Data Transfer Objects for operation results
 * - Enums (enums/) - Enumerations for statuses and types
 * - Config (config/) - Configuration files and constants
 * - Main (main/) - Application entry point with DI setup
 * - Data (data/) - CSV files for data storage
 * - Docs (docs/) - Project documentation, diagrams, and reports
 */
public class InternshipSystem {
    public static void main(String[] args) {
        // Shared scanner for all views
        Scanner scanner = new Scanner(System.in);

        // Initialize repositories (Data Access Layer)
        IUserRepository userRepository = new UserRepository();
        IInternshipRepository internshipRepository = new InternshipRepository();
        IApplicationRepository applicationRepository = new ApplicationRepository();
        IWithdrawalRepository withdrawalRepository = new WithdrawalRepository();
        IInterviewRepository interviewRepository = new InterviewRepository();

        // Cleanup: Delete expired internship opportunities (past closing date)
        int deletedCount = internshipRepository.deleteExpiredOpportunities();
        if (deletedCount > 0) {
            System.out.println("Removed " + deletedCount +
                    " expired internship opportunity(s) past their closing date.");
        }

        // Initialize services (Business Logic Layer)
        IStudentService studentService = new StudentService(
                userRepository,
                internshipRepository,
                applicationRepository,
                withdrawalRepository,
                interviewRepository);

        ICompanyRepService companyRepService = new CompanyRepService(
                userRepository,
                internshipRepository,
                applicationRepository,
                interviewRepository);

        ICareerStaffService careerStaffService = new CareerStaffService(
                userRepository,
                internshipRepository,
                applicationRepository,
                withdrawalRepository);

        // Initialize authentication controller with DI (now follows SOLID principles)
        AuthController authController = new AuthController(userRepository);

        // Initialize all views (View layer)
        MainView mainView = new MainView(scanner);
        StudentView studentView = new StudentView(scanner);
        CompanyRepView companyRepView = new CompanyRepView(scanner);
        CareerStaffView careerStaffView = new CareerStaffView(scanner);

        // Initialize role-specific controllers (Controller layer) with DI
        InputValidation inputValidation = new InputValidation();
        StudentController studentController = new StudentController(studentView, studentService, inputValidation);
        CompanyRepController companyRepController = new CompanyRepController(
                companyRepView,
                companyRepService,
                inputValidation);
        CareerStaffController careerStaffController = new CareerStaffController(
                careerStaffView,
                careerStaffService);

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
