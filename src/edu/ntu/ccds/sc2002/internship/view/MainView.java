package edu.ntu.ccds.sc2002.internship.view;

import java.util.Scanner;

/**
 * Main View class for the CLI interface.
 * VIEW LAYER: Responsible ONLY for displaying information and getting user
 * input.
 * - Shows main menu
 * - Gets login/registration input
 * - Formats output
 * DOES NOT contain business logic or routing decisions.
 */
public class MainView {
    private final Scanner scanner;

    public MainView(Scanner scanner) {
        this.scanner = scanner;
    }

    // Display main menu.
    public void showMainMenu() {
        System.out.println("\n=== Internship Placement Management System ===");
        System.out.println("1) Login");
        System.out.println("2) Register as Company Representative");
        System.out.println("3) Exit");
        System.out.print("Select option: ");
    }

    // Get menu choice from user.
    public String getMenuChoice() {
        return scanner.nextLine();
    }

    // Prompts for and gets user ID from user.
    public String getUserIdInput() {
        System.out.print("User ID: ");
        return scanner.nextLine();
    }

    // Prompts for and gets password from user.
    public String getPasswordInput() {
        System.out.print("Password: ");
        return scanner.nextLine();
    }

    // Prompts for and gets name from user.
    public String getNameInput() {
        System.out.println("\n-- Company Rep Registration --");
        System.out.print("Name: ");
        return scanner.nextLine();
    }

    // Prompts for and gets email from user.
    public String getEmailInput() {
        System.out.print("Email: ");
        return scanner.nextLine();
    }

    // Prompts for and gets company name from user.
    public String getCompanyInput() {
        System.out.print("Company: ");
        return scanner.nextLine();
    }

    // Prompts for and gets department from user.
    public String getDepartmentInput() {
        System.out.print("Department: ");
        return scanner.nextLine();
    }

    // Prompts for and gets position from user.
    public String getPositionInput() {
        System.out.print("Position: ");
        return scanner.nextLine();
    }

    // Display success message.
    public void showSuccess(String message) {
        System.out.println("✓ SUCCESS: " + message);
    }

    // Display error message.
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }

    // Display invalid choice message.
    public void showInvalidChoice() {
        System.out.println("Invalid choice. Please try again.");
    }

    // Display goodbye message.
    public void showGoodbye() {
        System.out.println("Goodbye!");
    }
}
