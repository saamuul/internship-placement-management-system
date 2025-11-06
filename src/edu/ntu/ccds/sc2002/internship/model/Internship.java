package edu.ntu.ccds.sc2002.internship.model;

public class Internship {
    private String internshipId;
    private String companyName;
    private String title;
    private Level level;

    public Internship(String internshipId, String companyName, String title, Level level) {
        this.internshipId = internshipId;
        this.companyName = companyName;
        this.title = title;
        this.level = level;
    }

    public String getInternshipId() {
        return internshipId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTitle() {
        return title;
    }

    public Level getLevel() {
        return level;
    }

    public String getDetails() {
        // Placeholder
        return "Internship ID: " + internshipId + "\nCompany Name: " + companyName + "\nTitle: " + title + "\nLevel: "
                + level;
    }
}