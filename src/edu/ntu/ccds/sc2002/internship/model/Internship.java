package edu.ntu.ccds.sc2002.internship.model;

public class Internship {
    private String internshipID;
    private String companyName;
    private String title;
    private Level level;

    public Internship(String internshipID, String companyName, String title, Level level) {
        this.internshipID = internshipID;
        this.companyName = companyName;
        this.title = title;
        this.level = level;
    }

    public String getInternshipID() {
        return internshipID;
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
        return "Internship ID: " + internshipID + "\nCompany Name: " + companyName + "\nTitle: " + title + "\nLevel: "
                + level;
    }
}