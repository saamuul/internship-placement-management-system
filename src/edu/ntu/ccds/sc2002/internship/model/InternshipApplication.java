package edu.ntu.ccds.sc2002.internship.model;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

public class InternshipApplication {

    private String applicationID;
    private String studentID;
    private String internshipID;
    private Status status;

    public InternshipApplication(String applicationID, String studentID, String internshipID, Status status) {
        this.applicationID = applicationID;
        this.studentID = studentID;
        this.internshipID = internshipID;
        this.status = status;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getInternshipID() {
        return internshipID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void toggleStatus(Status status) {
        this.status = status;
    }

    // Gets the Internship object associated with this application.
    public Internship getInternship() {
        List<String[]> internshipData = CSVUtil.readCSV("data/Internship_Opportunity_List.csv");

        // Skip header row and find matching internship
        for (int i = 1; i < internshipData.size(); i++) {
            String[] row = internshipData.get(i);

            // Check if we have enough columns
            if (row.length < 11)
                continue;

            String id = row[0];

            // If this is the internship we're looking for
            if (id.equals(this.internshipID)) {
                String companyName = row[6]; // RepName column
                String title = row[1];
                Level level = Level.valueOf(row[10].toUpperCase());

                return new Internship(id, companyName, title, level);
            }
        }

        return null; // Internship not found
    }

    // Return a CSV row representation compatible with CSVUtil.appendRow
    public String[] toCSVRow() {
        return new String[] { applicationID, studentID, internshipID, status.toString() };
    }

    @Override
    public String toString() {
        return String.join(",", toCSVRow());
    }
}