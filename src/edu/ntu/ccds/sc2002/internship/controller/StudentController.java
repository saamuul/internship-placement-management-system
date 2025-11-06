package edu.ntu.ccds.sc2002.internship.controller;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.Internship;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.OperationResult;
import edu.ntu.ccds.sc2002.internship.model.Student;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.view.StudentView;

/**
 * Controller for Student-related operations.
 * CONTROLLER LAYER: Coordinates between View and Model.
 * - Receives input from View
 * - Calls Model methods
 * - Passes results to View for display
 * - Handles routing logic
 * DOES NOT print directly.
 */
public class StudentController {
    private final StudentView studentView;

    public StudentController(StudentView studentView) {
        this.studentView = studentView;
    }

    /**
     * Main controller method that handles student menu flow.
     * CONTROLLER: Handles routing, calls Model, tells View what to display.
     */
    public boolean handleStudentMenu(User user) {
        if (!(user instanceof Student)) {
            return false;
        }

        // View: Display menu and get choice
        studentView.showDashboard(user);
        String choice = studentView.getMenuChoice();

        // Controller: Route based on choice
        switch (choice) {
            case "1": // View Available Internships
                handleViewInternships(user);
                break;

            case "2": // Apply for Internship
                handleApplyForInternship(user);
                break;

            case "3": // View Internship Application(s)
                handleViewInternshipApplications(user);
                break;

            case "4": // Accept Internship
                // TODO: Implement
                studentView.showError("Feature not yet implemented.");
                break;

            case "5": // Withdraw Internship Application(s)
                // TODO: Implement
                studentView.showError("Feature not yet implemented.");
                break;

            case "6": // Change Password
                handleChangePassword(user);
                break;

            case "7": // Logout
                studentView.showLogout();
                return true;

            default:
                studentView.showInvalidChoice();
        }

        return false;
    }

    // Handles viewing available internships.
    private void handleViewInternships(User user) {
        Student student = (Student) user;
        List<Internship> internships = student.viewInternships();
        studentView.displayInternships(internships);
    }

    // Handles applying for an internship.
    private void handleApplyForInternship(User user) {
        Student student = (Student) user;

        // View: Get input
        String internshipId = studentView.getInternshipIdInput();

        // Model: Process application
        OperationResult result = student.applyForInternship(internshipId);

        // View: Display result
        if (result.isSuccess()) {
            studentView.showSuccess(result.getMessage());
        } else {
            studentView.showError(result.getMessage());
        }
    }

    // Handles viewing student's internship applications.
    private void handleViewInternshipApplications(User user) {
        Student student = (Student) user;
        List<InternshipApplication> applications = student.getAppliedInternships();

        // Check if there are any applications
        if (applications == null || applications.isEmpty()) {
            studentView.showError("No internship applications found.");
            return;
        }

        // Prepare data: Create lists for parallel display
        List<String> appIds = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<String> companies = new ArrayList<>();
        List<String> levels = new ArrayList<>();
        List<String> statuses = new ArrayList<>();

        // For each application, get the internship and extract data
        for (InternshipApplication application : applications) {
            Internship internship = application.getInternship();

            appIds.add(application.getApplicationID());
            statuses.add(application.getStatus().toString());

            if (internship != null) {
                titles.add(internship.getTitle());
                companies.add(internship.getCompanyName());
                levels.add(internship.getLevel().toString());
            } else {
                titles.add("[Internship Not Found]");
                companies.add("N/A");
                levels.add("N/A");
            }
        }

        // Pass prepared data to view
        studentView.displayApplications(appIds, titles, companies, levels, statuses);
    }

    // Handles password change.
    private void handleChangePassword(User user) {
        // View: Get old password
        String oldPassword = studentView.getOldPasswordInput();

        // View: Get new password
        String newPassword = studentView.getNewPasswordInput();

        // Model: Change password
        OperationResult result = user.changePassword(oldPassword, newPassword);

        // View: Display result
        if (result.isSuccess()) {
            studentView.showSuccess(result.getMessage());
        } else {
            studentView.showError(result.getMessage());
        }
    }
}