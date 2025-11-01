package edu.ntu.ccds.sc2002.internship.model;

public class InternshipApplication {
    private String applicationID;
    private String studentID;
    private String internshipID;
    private String status;

    public InternshipApplication(String applicationID, String studentID, String internshipID, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Return a CSV row representation compatible with StudentCSV.appendLine
    public String[] toCSVRow() {
        return new String[] { applicationID, studentID, internshipID, status };
    }

    @Override
    public String toString() {
        return String.join(",", toCSVRow());
    }
}