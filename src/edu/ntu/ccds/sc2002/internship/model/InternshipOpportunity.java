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

    public InternshipOpportunity(String id, String title, String description, String major, String startDate,
            String endDate,
            CompanyRepresentative rep, int slots, Level level) {
        this.internshipID = id;
        this.title = title;
        this.description = description;
        this.prefMajor = major;
        this.openDate = startDate;
        this.closeDate = endDate;
        this.rep = rep;
        this.numOfSlots = slots;
        this.visibility = true;
        this.status = Status.PENDING;
        this.level = level;
    }

    public void setVisibility(boolean set) {
        this.visibility = set;
    }

    public void setInternshipID(String id) {
        this.internshipID = id;
    }

    public void setStatus(String stat) {
        Status statNum = Status.valueOf(stat.trim().toUpperCase());
        this.status = statNum;
    }

    public String getInternshipID() {
        return this.internshipID;
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

}