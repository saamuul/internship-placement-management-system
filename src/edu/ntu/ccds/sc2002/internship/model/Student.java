package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.StudentCSV;

public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<InternshipApplication> appliedInternships; // Will this change..? As this will be blank every time
                                                            // terminal resets too
    private InternshipApplication acceptedInternship;

    public Student(String userID, String name, int yearOfStudy, String major) {
        super(userID, name);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.appliedInternships = new ArrayList<>(); // Prob need change later? It will be blank every time terminal
                                                     // resets
        this.acceptedInternship = null;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    // public List<Internship> viewInternships(){
    // //Fill in later
    // }

    public void applyForInternship(String internshipID) {
        // Helper to navigate CSV
        StudentCSV csvhelper = new StudentCSV();

        // Short-form for each file (use repository CSV filenames from data folder)
        String opportunityFile = "data/Internship_Opportunity_List.csv";
        String applicationFile = "data/Internship_Applications_List.csv";

        // Find the internship with internshipID from the csv file (maybe become another
        // function?)
        List<String[]> internOppor = StudentCSV.readCSV(opportunityFile);
        String[] target = null;
        for (String[] row : internOppor) {
            if (row[0].equalsIgnoreCase(internshipID)) {
                target = row;
                break;
            }
        }

        if (target == null) {
            System.out.println("Internship ID: " + internshipID + " is not found.");
            return;
        }

        String level = target[10].trim();

        // Ensure Year 1 and 2 can only apply to Basic Internship
        if (yearOfStudy <= 2 && !level.equalsIgnoreCase("Basic")) {
            System.out.println("Year " + yearOfStudy + " students can only apply for Basic-level internships");
            return;
        }

        // Check that they do not have more than 3 active applications
        // (maybe another function that helps count..? Or maybe just use the list if it
        // is updated at the start..?)
        List<String[]> application = StudentCSV.readCSV(applicationFile);
        int count = 0;
        for (String[] row : application) {
            if (row[1].equalsIgnoreCase(getUserID())) {
                String status = row[3].trim();
                if (status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase("Successful")) {
                    count++;
                }
            }
        }

        if (count >= 3) {
            System.out.println("Student already applied for 3 internship opportunities.");
            return;
        }

        // Check if they applied for it before and is rejected
        for (String[] row : application) {
            if (row[1].equalsIgnoreCase(getUserID()) && row[2].equalsIgnoreCase(internshipID)) {
                String status = row[3].trim();
                if (!status.equalsIgnoreCase("Unsuccessful")) {
                    System.out.println("Student have already applied for this internship (Result: " + status + ").");
                    return;
                }
            }
        }

        // To get the AppID (maybe can be another helper function..?)
        int maxID = 0;
        for (String[] row : application) {
            try {
                int id = Integer.parseInt(row[0].trim());
                if (id > maxID) {
                    maxID = id;// Find largest current ID
                }
            } catch (NumberFormatException e) {
            }
        }
        String appID = String.valueOf(maxID + 1);

        // Create an application and append it
        InternshipApplication app = new InternshipApplication(appID, getUserID(), internshipID, "Pending");
        csvhelper.appendLine(app, applicationFile);

        appliedInternships.add(app); // Append to the current attribute(might remove depending on above..?)

        System.out.println(getName() + " successfully applied for " + internshipID + ".");
    }

    // public void viewInternshipApplications(){
    // //Fill in later
    // }

    // public void acceptInternship(String applicationID){
    // //Fill in later
    // }

    // public void withdrawApplication(String applicationID){
    // //Fill in later
    // }

    // public void autoRegister(File studentListFile){
    // //Fill in later
    // }
}