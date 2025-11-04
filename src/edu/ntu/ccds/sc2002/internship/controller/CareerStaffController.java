package edu.ntu.ccds.sc2002.internship.controller;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * Controller for Career Staff operations.
 * Handles business logic for staff actions like approving company
 * representatives,
 * managing internship opportunities, and overseeing applications.
 */
public class CareerStaffController {
    private final AuthController authController;

    public CareerStaffController(AuthController authController) {
        this.authController = authController;
    }

    public boolean handleMenuChoice(User user, String choice) {
        if (!(user instanceof CareerStaff)) {
            return false;
        }

        CareerStaff staff = (CareerStaff) user;

        switch (choice) {
            case "1":
                // Business logic: View pending company representatives
                viewPendingCompanyReps();
                break;
            case "2":
                // Business logic: Approve company representative
                System.out.println("[Controller] Processing approval...");
                // approveCompanyRep(staff);
                break;
            case "3":
                // TODO: View all internship opportunities
                System.out.println("[Controller] Fetching all opportunities...");
                // viewAllOpportunities();
                break;
            case "4":
                // TODO: View applications
                System.out.println("[Controller] Fetching applications...");
                // viewAllApplications();
                break;
            case "5":
                return true; // Logout
            default:
                System.out.println("Invalid option. Please try again.");
        }

        return false;
    }

    /**
     * Business logic: Retrieves and displays pending company representatives.
     */
    private void viewPendingCompanyReps() {
        List<CompanyRepresentative> pendingReps = authController.getPendingCompanyReps();
        if (pendingReps.isEmpty()) {
            System.out.println("No pending company representatives.");
        } else {
            System.out.println("\n--- Pending Company Representatives ---");
            for (CompanyRepresentative rep : pendingReps) {
                System.out.println("ID: " + rep.getUserId() + " | Name: " + rep.getName() +
                        " | Company: " + rep.getCompany().getName());
            }
        }
    }
}