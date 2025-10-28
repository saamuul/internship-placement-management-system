import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Student extends User{
    private int yearOfStudy;
    private String major;
    private List<InternshipApplication> appliedInternships;
    private InternshipApplication acceptedInternship;

    public Student(String userID, String name, int yearOfStudy, String major){
        super(userID, name);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.appliedInternships = new ArrayList<>();
        this.acceptedInternship = null;
    }

    public int getYearOfStudy(){ return yearOfStudy; }

    public String getMajor(){ return major; }

    public List<Internship> viewInternships(){
         
    }

    public void applyForIntership(String intershipID){
        
    }

    public void viewInternshipApplications(){

    }

    public void acceptInternship(String applicationID){
        
    }

    public void withdrawApplication(String applicationID){

    }

    public void autoRegister(File studentListFile){
        
    }
}