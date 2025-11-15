package edu.ntu.ccds.sc2002.internship.model;

public class Filter {
    private Level levelFilter;
    private String preferredMajor;
    private String applicationOpenDate;
    private String applicationCloseDate;
    private String repName;
    private int numOfSlots;
    private Status statusFilter;
    private boolean visibility;

    public Filter(Level levelFilter, String preferredMajor, String applicationOpenDate, String applicationCloseDate,
            String repName, int numOfSlots, Status statusFilter, boolean visibility) {
        this.levelFilter = levelFilter;
        this.preferredMajor = preferredMajor;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.repName = repName;
        this.numOfSlots = numOfSlots;
        this.statusFilter = statusFilter;
        this.visibility = visibility;
    }

    public Level getLevel() {
        return levelFilter;
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
        return statusFilter;
    }

    public boolean isVisibility() {
        return visibility;
    }
}