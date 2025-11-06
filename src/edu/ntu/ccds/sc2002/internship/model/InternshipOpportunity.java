package edu.ntu.ccds.sc2002.internship.model;

public class InternshipOpportunity {
    private String internshipID;
    private String title;
    private String description;
    private String prefMajor;
    private String openDate;
    private String closeDate;
    private CompanyRepresentative rep;
    private int numOfSlots;
    private boolean visibility;
    private Status status;
    private Level level;

<<<<<<< HEAD
    public InternshipOpportunity(String title, String description, String major, String startDate, String endDate,
            CompanyRepresentative rep, int slots, boolean visible, Level level) {
=======
    public InternshipOpportunity(String internshipID, String t, String descrip, String major, String startdate, String enddate,
            CompanyRepresentative r, int slots, boolean visible, Level l) {
        this.internshipID = internshipID;
>>>>>>> dee699dada54112ee383fb3ea32be961ca1746a4
        this.status = Status.PENDING;
        this.title = title;
        this.description = description;
        this.prefMajor = major;
        this.openDate = startDate;
        this.closeDate = endDate;
        this.rep = rep;
        this.numOfSlots = slots;
        this.visibility = visible;
        this.level = level;
    }

<<<<<<< HEAD
    public void setVisibility(boolean set) {
        this.visibility = set;
    }

    public CompanyRepresentative getRep() {
        return this.rep;
    }

    public Level getLevel() {
        return this.level;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPrefMajor() {
        return this.prefMajor;
    }

    public String getOpenDate() {
        return this.openDate;
    }

    public String getCloseDate() {
        return this.closeDate;
    }

    public Status getStatus() {
        return this.status;
    }

    public int getNumOfSlots() {
        return this.numOfSlots;
    }

    public boolean getVisibility() {
        return this.visibility;
    }
=======
    public void setVisibility(boolean set) { this.visibility = set; }
    public String getInternshipID() { return this.internshipID; }
    public CompanyRepresentative getRep() { return this.rep; }
    public Level getLevel() { return this.level; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getPrefMajor() { return this.prefMajor; }
    public String getOpenDate() { return this.opendate; }
    public String getCloseDate() { return this.closedate; }
    public Status getStatus() { return this.status; }
    public int getNumOfSlots() { return this.numofslots; }
    public boolean getVisibility() { return this.visibility; }
>>>>>>> dee699dada54112ee383fb3ea32be961ca1746a4

}