package edu.ntu.ccds.sc2002.internship.model;

import edu.ntu.ccds.sc2002.internship.enums.Level;

public class Internship {
    private String internshipId;
    private String companyRep;
    private String title;
    private Level level;
    private String description;
    private String openDate;
    private String closeDate;
    private int slots;

    public Internship(String internshipId, String companyRep, String title, Level level) {
        this.internshipId = internshipId;
        this.companyRep = companyRep;
        this.title = title;
        this.level = level;
        this.description = "";
        this.openDate = "";
        this.closeDate = "";
        this.slots = 0;
    }

    public Internship(String internshipId, String companyRep, String title, Level level,
            String description, String openDate, String closeDate, int slots) {
        this.internshipId = internshipId;
        this.companyRep = companyRep;
        this.title = title;
        this.level = level;
        this.description = description;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.slots = slots;
    }

    public String getInternshipId() {
        return internshipId;
    }

    public String getCompanyRep() {
        return companyRep;
    }

    public String getTitle() {
        return title;
    }

    public Level getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public String getOpenDate() {
        return openDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public int getSlots() {
        return slots;
    }

    public String getDetails() {
        // Placeholder
        return "Internship ID: " + internshipId + "\nCompany Representative: " + companyRep + "\nTitle: " + title + "\nLevel: "
                + level;
    }
}