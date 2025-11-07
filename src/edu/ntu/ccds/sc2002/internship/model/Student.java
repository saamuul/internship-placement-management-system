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

        //Check for visibility of internship 
        String visibility = target[8].trim();
        if (!visibility.equalsIgnoreCase("true")) {
            return OperationResult.failure("Internship ID: " + internshipId + "is not visible or open for applications");
        }

        //Check that the internship we applying for is for our major
        String internmajor = target[3].trim();
        if (!internmajor.equalsIgnoreCase(major)) {
            return OperationResult.failure("Student Major("+major+") is not eligible for this internship");
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
        InternshipApplication app = new InternshipApplication(appId, getUserId(), internshipId, Status.PENDING);
        CSVUtil.appendRow(applicationFile, app.toCSVRow());

        // Append to the current attribute
        appliedInternships.add(app);

        return OperationResult.success(getName() + " successfully applied for internship ID: " + internshipId);
    }

    public List<InternshipApplication> getAppliedInternships() {
        return appliedInternships;
    }

    public InternshipApplication getAcceptedInternship() {
        return acceptedInternship;
    }

    public OperationResult acceptInternship(String applicationId){
        InternshipApplication targetApp = null;

        //Find the student application
        for (InternshipApplication app: appliedInternships) {
            if (app.getStudentID().equalsIgnoreCase(getUserId()) && app.getApplicationID().equalsIgnoreCase(applicationId)) {
                targetApp = app;
                break;
            }
        }

        if (targetApp == null) {
            return OperationResult.failure("Student have not applied with application ID: " + applicationId);
        }

        //Only accept application if the application was successful
        if (targetApp.getStatus() != Status.SUCCESSFUL) {
            return OperationResult.failure("Only applications with status 'Successful' can be accepted.");
        }

        // Check that this student has not already accepted another internship
        if (acceptedInternship != null) {
            return OperationResult.failure("Student have already accepted an internship placement.");
        }
    
        //If all condition needed is fulfilled, record internship acceptance
        acceptedInternship = targetApp;

        //Remove all other applications by this student excluding the accepted one (Updates the file as well)
        CSVUtil.removeMatchingRows("data/Internship_Applications_List.csv", row -> !row[0].equalsIgnoreCase("ApplicationID") && row[1].equalsIgnoreCase(getUserId()) && !row[0].equalsIgnoreCase(applicationId));



        //Mark the internship opportunity that we accepted as FILLED (assumes only 1 placement)
        List<String[]> opportunities = CSVUtil.readCSV("data/Internship_Opportunity_List.csv");
        for (int i = 0; i < opportunities.size(); i++) {
            String[] newRow = opportunities.get(i);
            //Check that row is not header and that we are looking at the internship we accepted
            if (newRow.length > 0 && newRow[0].equalsIgnoreCase(targetApp.getInternshipID())) {
                //Set status to FILLED
                newRow[9] = Status.FILLED.toString();
                //Update that row in csv file to reflect changes
                CSVUtil.updateRow("data/Internship_Opportunity_List.csv",i, newRow);
                break;
            }
        }

        return OperationResult.success("Internship accepted. Other applications have been withdrawn and removed.");
    }

    public void withdrawApplication(String applicationId){
    // Fill in later
    }
}