package edu.ntu.ccds.sc2002.internship.util;

/**
 * Data Transfer Object for toggle visibility operations.
 * Follows proper encapsulation with private fields and getters/setters.
 */
public class ToggleVisHelper {
    private String id;
    private String state;

    public ToggleVisHelper() {
    }

    public ToggleVisHelper(String id, String state) {
        this.id = id;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }
}
