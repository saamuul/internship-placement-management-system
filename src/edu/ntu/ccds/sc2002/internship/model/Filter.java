package edu.ntu.ccds.sc2002.internship.model;

public class Filter {
    private Level level;
    private String preferredMajor;
    private String applicationOpenDate;
    private String applicationCloseDate;
    private String repName;
    private int numOfSlots;
    private Status status;
    private boolean visibility;

    public Filter(Level level, String preferredMajor, String applicationOpenDate, String applicationCloseDate,
            String repName, int numOfSlots, Status status, boolean visibility) {
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.repName = repName;
        this.numOfSlots = numOfSlots;
        this.status = status;
        this.visibility = visibility;
    }

    public Level getLevel() {
        return level;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public String getApplicationOpenDate() {
        return applicationOpenDate;
    }

    public String getApplicationCloseDate() {
        return applicationCloseDate;
    }

    public String getRepName() {
        return repName;
    }

    public int getNumOfSlots() {
        return numOfSlots;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isVisibility() {
        return visibility;
    }
}