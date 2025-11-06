package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<InternshipApplication> appliedInternships;
    private InternshipApplication acceptedInternship;

    public Student(String studentId, String name, String email, String password, int yearOfStudy, String major,
            List<InternshipApplication> appliedInternships, InternshipApplication acceptedInternship) {
        super(studentId, name, email, password, UserRole.STUDENT);
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
        List<String[]> internshipOpportunities = CSVUtil.readCSV("data/Internship_Opportunity_List.csv");

        // Skip the header row (first row)
        boolean isFirstRow = true;
        for (String[] row : internshipOpportunities) {
            if (isFirstRow) {
                isFirstRow = false;
                continue; // Skip header
            }

            if (row.length < 11)
                continue; // Ensure row has enough columns

            String internshipId = row[0];
            String companyName = row[6]; // RepName
            String title = row[1];
            Level level = Level.valueOf(row[10].toUpperCase()); // Level column

            Internship internship = new Internship(internshipId, companyName, title, level);
            internships.add(internship);
        }
        return internships;
    }

    /**
     * Apply for an internship.
     * MODEL LAYER: Contains business logic, validates rules, manipulates data.
     * DOES NOT print - returns OperationResult for View to display.
     */
    public OperationResult applyForInternship(String internshipId) {
        // Short-form for each file (use repository CSV filenames from data folder)
        String opportunityFile = "data/Internship_Opportunity_List.csv";
        String applicationFile = "data/Internship_Applications_List.csv";

        // Find the internship with internshipId from the csv file
        List<String[]> internshipOpportunities = CSVUtil.readCSV(opportunityFile);
        String[] target = null;
        for (String[] row : internshipOpportunities) {
            if (row[0].equalsIgnoreCase(internshipId)) {
                target = row;
                break;
            }
        }

        if (target == null) {
            return OperationResult.failure("Internship ID: " + internshipId + " not found.");
        }

        String level = target[10].trim();

        // Ensure Year 1 and 2 can only apply to Basic Internship
        if (yearOfStudy <= 2 && !level.equalsIgnoreCase("Basic")) {
            return OperationResult
                    .failure("Year " + yearOfStudy + " students can only apply for Basic-level internships.");
        }

        // Check that they do not have more than 3 active applications
        List<String[]> application = CSVUtil.readCSV(applicationFile);
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
            if (row[1].equalsIgnoreCase(getUserId()) && row[2].equalsIgnoreCase(internshipId)) {
                String status = row[3].trim();
                if (!status.equalsIgnoreCase("Unsuccessful")) {
                    return OperationResult
                            .failure("You have already applied for this internship (Status: " + status + ").");
                }
            }
        }

        // To get the AppID
        int maxId = 0;
        for (String[] row : application) {
            try {
                int id = Integer.parseInt(row[0].trim());
                if (id > maxId) {
                    // Find largest current ID
                    maxId = id;
                }
            } catch (NumberFormatException e) {
            }
        }
        String appId = String.valueOf(maxId + 1);

        // Create an application and append it
        InternshipApplication app = new InternshipApplication(appId, getUserId(), internshipId, "Pending");
        CSVUtil.appendRow(applicationFile, app.toCSVRow());

        // Append to the current attribute
        appliedInternships.add(app);

        return OperationResult.success(getName() + " successfully applied for internship ID: " + internshipId);
    }

    // public void acceptInternship(String applicationId){
    //     // Fill in later
    // }

    // public void withdrawApplication(String applicationId){
    // // Fill in later
    // }
}