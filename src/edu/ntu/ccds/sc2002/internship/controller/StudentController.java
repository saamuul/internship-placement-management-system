package edu.ntu.ccds.sc2002.internship.controller;

import edu.ntu.ccds.sc2002.internship.model.Student;
import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * Controller for Student-related operations.
 * Handles business logic for student actions like applying for internships,
 * viewing opportunities, and managing applications.
 */
public class StudentController {

    public void applyForInternship(Student student, String internshipID) {
        // Business logic: delegate to model
        student.applyForInternship(internshipID);
    }

    public boolean handleMenuChoice(User user, String choice) {
        if (!(user instanceof Student)) {
            return false;
        }

        Student student = (Student) user;

        switch (choice) {
            case "1":
                // TODO: View available internships
                System.out.println("[Controller] Fetching available internships...");
                // viewAvailableInternships(student);
                break;
            case "2":
                // TODO: Apply for internship
                System.out.println("[Controller] Processing internship application...");
                student.applyForInternship("internshipID");
                System.out.println("[Controller] Processing internship application...");

                // promptAndApplyForInternship(student);
                break;
            case "3":
                return true; // Logout
            default:
                System.out.println("Invalid option. Please try again.");
        }

        return false;
    }
}