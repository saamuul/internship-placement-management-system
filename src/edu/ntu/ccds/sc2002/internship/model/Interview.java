package edu.ntu.ccds.sc2002.internship.model;

public class Interview {
    private String studentId;
    private String internshipId;
    private String proposedTime;
    private String confirmedTime;

    public Interview(String studentId, String internshipId, String proposedTime, String confirmedTime) {
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.proposedTime = proposedTime;
        this.confirmedTime = confirmedTime;
    }

    // Getters and setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getInternshipId() { return internshipId; }
    public void setInternshipId(String internshipId) { this.internshipId = internshipId; }

    public String getProposedTime() { return proposedTime; }
    public void setProposedTime(String proposedTime) { this.proposedTime = proposedTime; }

    public String getConfirmedTime() { return confirmedTime; }
    public void setConfirmedTime(String confirmedTime) { this.confirmedTime = confirmedTime; }
}