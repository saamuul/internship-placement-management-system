package model;

public class Internship extends InternshipOpportunity {
    String internshipID;
    String companyName;
    String title; 
    Level level;
    InternshipOpportunity acceptedFrom;

    public Internship(String internshipID, String companyName, String title, Level level, InternshipOpportunity acceptedFrom) {
        super(title,level,acceptedFrom)
    }
    
    public String getDetails() {
        
    }
}