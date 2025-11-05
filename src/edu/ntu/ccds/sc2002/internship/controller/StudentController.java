package edu.ntu.ccds.sc2002.internship.controller;

import edu.ntu.ccds.sc2002.internship.model.Student;
import edu.ntu.ccds.sc2002.internship.model.User;

import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);

        switch (choice) {
            case "1":
                // TODO: View available internships
                System.out.println("[Controller] Fetching available internships...");
                // viewAvailableInternships(student);
                break;
            case "2":
                // TODO: Apply for internship
                int internshipID = scanner.nextInt();
                student.applyForInternship(internshipID);

                // promptAndApplyForInternship(student);
                break;
            case "3":
                // TODO: View application status


            case "4":
                // TODO: Accept internship


            case "5":
                // TODO: Withdraw application


            case "6":
                // TODO: Change Password


            case "7":
                return true; // Logout

            default:
                System.out.println("Invalid option. Please try again.");
        }

        return false;
    }
}