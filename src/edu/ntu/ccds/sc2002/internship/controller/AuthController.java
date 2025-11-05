package edu.ntu.ccds.sc2002.internship.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.Company;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.Status;
import edu.ntu.ccds.sc2002.internship.model.Student;
import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * Controller for authentication and user management.
 * Handles business logic for login, registration, and user authorization.
 * Loads user data from CSV files in the data/ folder.
 */
public class AuthController {
    private final Map<String, User> userRepo = new HashMap<>();

    private final String studentFilePath;
    private final String staffFilePath;
    private final String companyRepFilePath;

    public AuthController(String studentFilePath, String staffFilePath, String companyRepFilePath) {
        this.studentFilePath = studentFilePath;
        this.staffFilePath = staffFilePath;
        this.companyRepFilePath = companyRepFilePath;
        loadStudentsFromFile();
        loadStaffFromFile();
        loadCompanyRepsFromFile();
    }

    // -----------------------------
    // File Initialization
    // -----------------------------
    private void loadStudentsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row
                    continue;
                }
                if (line.isBlank())
                    continue;
                String[] parts = line.split(",", -1); // -1 to keep empty trailing fields
                if (parts.length < 6)
                    continue;
                // CSV format:
                // StudentID,Name,Password,Major,Year,Email,AppliedInternships,AcceptedInternship
                String id = parts[0].trim();
                String name = parts[1].trim();
                String pw = parts[2].trim();
                String major = parts[3].trim();
                int year = Integer.parseInt(parts[4].trim());
                // Note: parts[5] is email, parts[6] is appliedInternships, parts[7] is
                // acceptedInternship
                // These will be loaded separately if needed

                userRepo.put(id, new Student(id, name, pw, year, major));
            }
            System.out.println("[INFO] Loaded students from file.");
        } catch (IOException e) {
            System.out.println("[WARN] Could not load students.txt: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid year format in student data: " + e.getMessage());
        }
    }

    private void loadStaffFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(staffFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row
                    continue;
                }
                if (line.isBlank())
                    continue;
                String[] parts = line.split(",");
                if (parts.length < 5)
                    continue;
                // CSV format: StaffID,Name,Role,Department,Email
                String id = parts[0].trim();
                String name = parts[1].trim();
                String position = parts[2].trim(); // Role
                String dept = parts[3].trim();
                String email = parts[4].trim();
                String pw = "password"; // Default password
                userRepo.put(id, new CareerStaff(id, name, pw, position, dept, email));
            }
            System.out.println("[INFO] Loaded staff from file.");
        } catch (IOException e) {
            System.out.println("[WARN] Could not load staff.txt: " + e.getMessage());
        }
    }

    private void loadCompanyRepsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(companyRepFilePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row
                    continue;
                }
                if (line.isBlank())
                    continue;
                String[] parts = line.split(",", -1); // -1 to preserve trailing empty strings
                if (parts.length < 9)
                    continue;
                // CSV format:
                // CompanyRepID,Name,Password,CompanyName,Department,Position,Email,Status,CreatedOpportunities
                String id = parts[0].trim();
                String name = parts[1].trim();
                String password = parts[2].trim();
                String companyName = parts[3].trim();
                String dept = parts[4].trim();
                String position = parts[5].trim();
                String email = parts[6].trim();
                String statusStr = parts[7].trim();
                // parts[8] contains CreatedOpportunities (semicolon-separated) - not loaded yet

                Company company = new Company(companyName, Integer.parseInt(id));
                CompanyRepresentative rep = new CompanyRepresentative(email, name, password, company, dept, position);

                // Set status from CSV
                try {
                    Status status = Status.valueOf(statusStr);
                    rep.setStatus(status);
                } catch (IllegalArgumentException e) {
                    rep.setStatus(Status.PENDING); // Default if invalid
                }

                // TODO: Parse createdOppsStr and load InternshipOpportunity objects
                // For now, we skip loading the opportunities list

                userRepo.put(email, rep);
            }
            System.out.println("[INFO] Loaded company representatives from file.");
        } catch (IOException e) {
            System.out.println("[WARN] Could not load company representatives: " + e.getMessage());
        }
    }

    // -----------------------------
    // Authentication & Registration
    // -----------------------------
    public AuthResult login(String userId, String password) {
        User user = userRepo.get(userId);
        if (user == null || !user.login(userId, password))
            return new AuthResult(false, "Invalid credentials.", null);
        if (user instanceof CompanyRepresentative rep && rep.getStatus() != Status.SUCCESSFUL)
            return new AuthResult(false, "Account pending approval.", null);
        return new AuthResult(true, "Login successful.", user);
    }

    public boolean changePassword(String userId, String oldPw, String newPw) {
        User u = userRepo.get(userId);
        if (u != null) {
            u.changePassword(oldPw, newPw);
            return true;
        }
        return false;
    }

    public RegistrationResult registerCompanyRep(String name, String email, String companyName, String dept,
            String pos) {
        if (userRepo.containsKey(email))
            return RegistrationResult.ALREADY_EXISTS;
        Company company = new Company(companyName, userRepo.size() + 1);
        userRepo.put(email, new CompanyRepresentative(email, name, "password", company, dept, pos));
        return RegistrationResult.SUCCESS;
    }

    // -----------------------------
    // Administrative Access
    // -----------------------------
    public List<CompanyRepresentative> getPendingCompanyReps() {
        List<CompanyRepresentative> reps = new ArrayList<>();
        for (User u : userRepo.values()) {
            if (u instanceof CompanyRepresentative rep && rep.getStatus() != Status.SUCCESSFUL)
                reps.add(rep);
        }
        return reps;
    }

    public void approveCompanyRep(String email) {
        User u = userRepo.get(email);
        if (u instanceof CompanyRepresentative rep) {
            rep.setStatus(Status.SUCCESSFUL);
        }
    }

    public Collection<User> getAllUsers() {
        return userRepo.values();
    }
}
