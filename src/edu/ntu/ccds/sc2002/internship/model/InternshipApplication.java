package edu.ntu.ccds.sc2002.internship.model;

public class InternshipApplication {
<<<<<<< HEAD
    private String applicationId;
    private String studentId;
    private String internshipId;
    private String status;

    public InternshipApplication(String applicationId, String studentId, String internshipId, String status) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.status = status;
    }

    public String getApplicationId() {
        return applicationId;
    }
=======
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
>>>>>>> dee699dada54112ee383fb3ea32be961ca1746a4

    public String getStudentId() {
        return studentId;
    }

    public String getInternshipId() {
        return internshipId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Return a CSV row representation compatible with CSVUtil.appendRow
    public String[] toCSVRow() {
<<<<<<< HEAD
        return new String[] { applicationId, studentId, internshipId, status };
=======
        return new String[] { applicationID, studentID, internshipID, status.toString() };
>>>>>>> dee699dada54112ee383fb3ea32be961ca1746a4
    }

    public void toggleStatus(Status status){
        this.status = status;
    }
    @Override
    public String toString() {
        return String.join(",", toCSVRow());
    }
}