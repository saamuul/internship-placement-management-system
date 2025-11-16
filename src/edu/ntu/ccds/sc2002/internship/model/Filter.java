package edu.ntu.ccds.sc2002.internship.model;

import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.enums.Status;

public class Filter {
    private Level levelFilter;
    private String preferredMajor;
    private String repName;
    private Status statusFilter;
    private Boolean visibility;
    private String closingDate; // Filter by closing date

    public Filter(Level levelFilter, String preferredMajor, String repName, Status statusFilter, Boolean visibility,
            String closingDate) {
        this.levelFilter = levelFilter;
        this.preferredMajor = preferredMajor;
        this.repName = repName;
        this.statusFilter = statusFilter;
        this.visibility = visibility;
        this.closingDate = closingDate;
    }

    // Backward compatibility constructor
    public Filter(Level levelFilter, String preferredMajor, String repName, Status statusFilter, Boolean visibility) {
        this(levelFilter, preferredMajor, repName, statusFilter, visibility, null);
    }

    public Level getLevel() {
        return levelFilter;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public String getRepName() {
        return repName;
    }

    public Status getStatus() {
        return statusFilter;
    }

    public Boolean isVisibility() {
        return visibility;
    }

    public String getClosingDate() {
        return closingDate;
    }
}