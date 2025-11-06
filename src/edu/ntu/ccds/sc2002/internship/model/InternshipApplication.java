package edu.ntu.ccds.sc2002.internship.model;

public class InternshipApplication {
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
        return new String[] { applicationId, studentId, internshipId, status };
    }

    @Override
    public String toString() {
        return String.join(",", toCSVRow());
    }
}