package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.StudentCSV;

public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<InternshipApplication> appliedInternships;
    private InternshipApplication acceptedInternship;

    public Student(String studentID, String name, String email, String Password, int yearOfStudy, String major,
            List<InternshipApplication> appliedInternships, InternshipApplication acceptedInternship) {
        super(studentID, name, email, Password, UserRole.STUDENT);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.appliedInternships = appliedInternships;
        this.acceptedInternship = acceptedInternship;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public List<InternshipApplication> getAppliedInternships() {
        return appliedInternships;
    }

    public InternshipApplication getAcceptedInternship() {
        return acceptedInternship;
    }

    public List<Internship> viewInternships() {
        List<Internship> internships = new ArrayList<>();
        List<String[]> internOppor = StudentCSV.readCSV("data/Internship_Opportunity_List.csv");

        // Skip the header row (first row)
        boolean isFirstRow = true;
        for (String[] row : internOppor) {
            if (isFirstRow) {
                isFirstRow = false;
                continue; // Skip header
            }

            if (row.length < 11)
                continue; // Ensure row has enough columns

            String internshipID = row[0];
            String companyName = row[6]; // RepName
            String title = row[1];
            Level level = Level.valueOf(row[10].toUpperCase()); // Level column

            Internship internship = new Internship(internshipID, companyName, title, level);
            internships.add(internship);
        }
        return internships;
    }

    /**
     * Apply for an internship.
     * MODEL LAYER: Contains business logic, validates rules, manipulates data.
     * DOES NOT print - returns OperationResult for View to display.
     */
    public OperationResult applyForInternship(String internshipID) {
        // Helper to navigate CSV
        StudentCSV csvhelper = new StudentCSV();

        // Short-form for each file (use repository CSV filenames from data folder)
        String opportunityFile = "data/Internship_Opportunity_List.csv";
        String applicationFile = "data/Internship_Applications_List.csv";

        // Find the internship with internshipID from the csv file
        List<String[]> internOppor = StudentCSV.readCSV(opportunityFile);
        String[] target = null;
        for (String[] row : internOppor) {
            if (row[0].equalsIgnoreCase(internshipID)) {
                target = row;
                break;
            }
        }

        if (target == null) {
            return OperationResult.failure("Internship ID: " + internshipID + " not found.");
        }

        String level = target[10].trim();

        // Ensure Year 1 and 2 can only apply to Basic Internship
        if (yearOfStudy <= 2 && !level.equalsIgnoreCase("Basic")) {
            return OperationResult
                    .failure("Year " + yearOfStudy + " students can only apply for Basic-level internships.");
        }

        // Check that they do not have more than 3 active applications
        List<String[]> application = StudentCSV.readCSV(applicationFile);
        int count = 0;
        for (String[] row : application) {
            if (row[1].equalsIgnoreCase(getUserId())) {
                String status = row[3].trim();
                if (status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase("Successful")) {
                    count++;
                }
            }
        }

        if (count >= 3) {
            return OperationResult.failure("You already have 3 active internship applications.");
        }

        // Check if they applied for it before and is rejected
        for (String[] row : application) {
            if (row[1].equalsIgnoreCase(getUserId()) && row[2].equalsIgnoreCase(internshipID)) {
                String status = row[3].trim();
                if (!status.equalsIgnoreCase("Unsuccessful")) {
                    return OperationResult
                            .failure("You have already applied for this internship (Status: " + status + ").");
                }
            }
        }

        // To get the AppID
        int maxID = 0;
        for (String[] row : application) {
            try {
                int id = Integer.parseInt(row[0].trim());
                if (id > maxID) {
                    // Find largest current ID
                    maxID = id;
                }
            } catch (NumberFormatException e) {
            }
        }
        String appID = String.valueOf(maxID + 1);

        // Create an application and append it
        InternshipApplication app = new InternshipApplication(appID, getUserId(), internshipID, "Pending");
        csvhelper.appendLine(app, applicationFile);

        // Append to the current attribute
        appliedInternships.add(app);

        return OperationResult.success(getName() + " successfully applied for internship ID: " + internshipID);
    }

    // public void viewInternshipApplications(){
    // Fill in later
    // }

    // public void acceptInternship(String applicationID){
    // Fill in later
    // }

    // public void withdrawApplication(String applicationID){
    // Fill in later
    // }
}