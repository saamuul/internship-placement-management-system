package edu.ntu.ccds.sc2002.internship.controller;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.Internship;
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
                // TODO: Implement
                studentView.showError("Feature not yet implemented.");
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

    /**
     * Handles viewing available internships.
     * CONTROLLER: Gets data from Model, tells View to display.
     */
    private void handleViewInternships(User user) {
        Student student = (Student) user;
        List<Internship> internships = student.viewInternships();
        studentView.displayInternships(internships);
    }

    /**
     * Handles applying for an internship.
     * CONTROLLER: Gets input from View, calls Model, displays result via View.
     */
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

    /**
     * Handles password change.
     * CONTROLLER: Gets input from View, calls Model, displays result via View.
     */
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

    /**
     * Get list of available internships.
     * CONTROLLER: Calls Model, returns data to View.
     */
    public List<Internship> viewInternships(User user) {
        if (!(user instanceof Student)) {
            return null;
        }
        Student student = (Student) user;
        return student.viewInternships();
    }

    /**
     * Apply for an internship.
     * CONTROLLER: Calls Model with data from View, returns result to View.
     */
    public OperationResult applyForInternship(User user, String internshipID) {
        if (!(user instanceof Student)) {
            return OperationResult.failure("Only students can apply for internships.");
        }
        Student student = (Student) user;
        return student.applyForInternship(internshipID);
    }
}