package edu.ntu.ccds.sc2002.internship.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ntu.ccds.sc2002.internship.model.AuthResult;
import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.Company;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.model.Level;
import edu.ntu.ccds.sc2002.internship.model.RegistrationResult;
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
    private final Map<String, InternshipApplication> applicationRepo = new HashMap<>();
    private final Map<String, InternshipOpportunity> opportunityRepo = new HashMap<>();

    private final String studentFilePath;
    private final String staffFilePath;
    private final String companyRepFilePath;

    public AuthController(String studentFilePath, String staffFilePath, String companyRepFilePath) {
        this.studentFilePath = studentFilePath;
        this.staffFilePath = staffFilePath;
        this.companyRepFilePath = companyRepFilePath;
        loadApplicationsFromFile(); // Load applications first
        loadStudentsFromFile();
        loadStaffFromFile();
        loadCompanyRepsFromFile();
        loadOpportunitiesFromFile();
    }

    // -----------------------------
    // File Initialization
    // -----------------------------
    private void loadApplicationsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/Internship_Applications_List.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row
                    continue;
                }
                if (line.isBlank())
                    continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 4)
                    continue;
                // CSV format: AppID,StudentID,InternOppID,Status
                String appId = parts[0].trim();
                String studentId = parts[1].trim();
                String internshipId = parts[2].trim();
                String statusStr = parts[3].trim();

                Status status = Status.valueOf(statusStr.toUpperCase());
                InternshipApplication app = new InternshipApplication(appId, studentId, internshipId, status);
                applicationRepo.put(appId, app);
            }
            System.out.println("[INFO] Loaded " + applicationRepo.size() + " internship applications from file.");
        } catch (IOException e) {
            System.out.println("[WARN] Could not load applications: " + e.getMessage());
        }
    }

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
                String email = parts[5].trim();

                // Parse appliedInternships - find all applications for this student
                List<InternshipApplication> appliedInternships = new ArrayList<>();
                for (InternshipApplication app : applicationRepo.values()) {
                    if (app.getStudentID().equals(id)) {
                        appliedInternships.add(app);
                    }
                }

                // Parse acceptedInternship - find the accepted application (if any)
                InternshipApplication acceptedInternship = null;
                if (parts.length > 7 && !parts[7].trim().isEmpty()) {
                    String acceptedAppId = parts[7].trim();
                    acceptedInternship = applicationRepo.get(acceptedAppId);
                }

                userRepo.put(id, new Student(id, name, email, pw, year, major, appliedInternships, acceptedInternship));
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
                String id = parts[0];
                String name = parts[1].trim();
                String password = parts[2].trim();
                String companyName = parts[3].trim();
                String dept = parts[4].trim();
                String position = parts[5].trim();
                String email = parts[6].trim();
                String statusStr = parts[7].trim();
                // parts[8] contains CreatedOpportunities (semicolon-separated) - not loaded yet
                
                Company company = new Company(companyName, Integer.parseInt(id));
                CompanyRepresentative rep = new CompanyRepresentative(id, name, email, password, company, dept,
                        position);

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

    private void loadOpportunitiesFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("data/Internship_Opportunity_List.csv"))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                } // skip header
                if (line.isBlank())
                    continue;

                String[] parts = line.split(",", -1);
                if (parts.length < 11)
                    continue; // Need all 11 columns

                String internshipId = parts[0].trim();
                String title = parts[1].trim();
                String description = parts[2].trim();
                String prefMajor = parts[3].trim();
                String openDate = parts[4].trim();
                String closeDate = parts[5].trim();
                String repName = parts[6].trim(); // This is the representative's NAME, not ID
                int numSlots = Integer.parseInt(parts[7].trim());
                boolean visible = Boolean.parseBoolean(parts[8].trim());
                Status status = Status.valueOf(parts[9].trim().toUpperCase());
                Level level = Level.valueOf(parts[10].trim().toUpperCase());

                // Try to find the CompanyRepresentative by name
                CompanyRepresentative rep = null;
                for (User user : userRepo.values()) {
                    if (user instanceof CompanyRepresentative && user.getName().equalsIgnoreCase(repName)) {
                        rep = (CompanyRepresentative) user;
                        break;
                    }
                }

                InternshipOpportunity opp = new InternshipOpportunity(internshipId, title, description, prefMajor,
                        openDate, closeDate,
                        rep, numSlots, level);
                opp.setInternshipID(internshipId); // Set the ID from CSV
                //Store with internshipId as key;
                opportunityRepo.put(internshipId, opp);
                opportunityRepo.put(title, opp);
            }
            System.out.println("[INFO] Loaded " + opportunityRepo.size() + " internship opportunities from file.");
        } catch (IOException e) {
            System.out.println("[WARN] Could not load internship opportunities: " + e.getMessage());
        }
    }

    // -----------------------------
    // Authentication & Registration
    // -----------------------------
    public AuthResult login(String userId, String password) {
        User user = userRepo.get(userId);
        
        // Check if the user exists or if credentials match
        if (user == null || !user.login(userId, password))
            return new AuthResult(false, "Invalid credentials.", null);
        
        // Check if user is a CompanyRepresentative and if they are approved
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

    public RegistrationResult registerCompanyRep(String id, String name, String email, String companyName,
            String dept, String pos) {
        if (userRepo.containsKey(email))
            return RegistrationResult.ALREADY_EXISTS;
        Company company = new Company(companyName, userRepo.size() + 1);
        userRepo.put(email, new CompanyRepresentative(id, email, name, "password", company, dept, pos));
        return RegistrationResult.SUCCESS;
    }

    // -----------------------------
    // Career Staff Access
    // -----------------------------
    public List<CompanyRepresentative> getPendingCompanyReps() {
        loadCompanyRepsFromFile();
        List<CompanyRepresentative> reps = new ArrayList<>();
        for (User u : userRepo.values()) {
            if (u instanceof CompanyRepresentative rep && rep.getStatus() != Status.SUCCESSFUL)
                reps.add(rep);
        }
        return reps;
    }   

    public List<InternshipOpportunity> getPendingOpportunities() {
        loadOpportunitiesFromFile();
        List<InternshipOpportunity> opportunities = new ArrayList<>();
        for (InternshipOpportunity opp : opportunityRepo.values()) {
            if (opp.getStatus() != Status.SUCCESSFUL) {
                opportunities.add(opp);
            }
        }
        return opportunities;
    }

    public List<InternshipApplication> getPendingWithdrawals() {
        loadApplicationsFromFile();
        List<InternshipApplication> withdrawals = new ArrayList<>();
        for (InternshipApplication app : applicationRepo.values()) {
            if (app.getStatus() != Status.SUCCESSFUL) {
                withdrawals.add(app);
            }
        }
        return withdrawals;
    }

    public List<InternshipOpportunity> getAllOpportunities() {
        return new ArrayList<>(opportunityRepo.values());
    }

    public List<InternshipApplication> getAllApplications() {
        return new ArrayList<>(applicationRepo.values());
    }
}
