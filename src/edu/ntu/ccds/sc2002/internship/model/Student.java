package model;

import java.io.File;
import java.util.List;

public class Student extends User{
    private int yearOfStudy;
    private String major;
    private <InternshipApplication> appliedInternships;
    private InternshipApplication acceptedInternship;

    public Student(String userID, String name, int yearOfStudy, String major){
        super(userID, name);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
    }

    public int getYearOfStudy(){ return yearOfStudy; }

    public String getMajor(){ return major; }

    public List<Internship> viewInterships(){
         
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