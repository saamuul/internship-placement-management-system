package edu.ntu.ccds.sc2002.internship.model;

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

    public String getApplicationID() { return applicationID;}
    public String getStudentID() { return studentID; }
    public String getInternshipID() { return internshipID; }
    public Status getStatus() { return status; }

    // Return a CSV row representation compatible with StudentCSV.appendLine
    public String[] toCSVRow() {
        return new String[] { applicationID, studentID, internshipID, status.toString() };
    }

    public void toggleStatus(Status status){
        this.status = status;
    }
    @Override
    public String toString() {
        return String.join(",", toCSVRow());
    }
}