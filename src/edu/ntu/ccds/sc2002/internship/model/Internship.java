public class Internship {
    private String internshipID ;
    private String companyName;
    private String title; 
    private Level level; 
    private InternshipOpportunity acceptedFrom;
        
    public String getDetails() {
        //Placeholder
        return "Internship ID: " + internshipID + "\nCompany Name: " + companyName + "\nTitle: " + title + "\nLevel: " + level;
    }
}