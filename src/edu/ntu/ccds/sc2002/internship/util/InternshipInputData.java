package edu.ntu.ccds.sc2002.internship.util;

/**
 * Data Transfer Object for internship opportunity input data.
 * Follows proper encapsulation with private fields and getters/setters.
 */
public class InternshipInputData {
    private String title;
    private String description;
    private String prefMajor;
    private String openDate;
    private String closeDate;
    private int numOfSlots;
    private String level;

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPrefMajor() { return prefMajor; }
    public String getOpenDate() { return openDate; }
    public String getCloseDate() { return closeDate; }
    public int getNumOfSlots() { return numOfSlots; }
    public String getLevel() { return level; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPrefMajor(String prefMajor) { this.prefMajor = prefMajor; }
    public void setOpenDate(String openDate) { this.openDate = openDate; }
    public void setCloseDate(String closeDate) { this.closeDate = closeDate; }
    public void setNumOfSlots(int numOfSlots) { this.numOfSlots = numOfSlots; }
    public void setLevel(String level) { this.level = level; }
}