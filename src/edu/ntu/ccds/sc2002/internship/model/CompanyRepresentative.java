package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;

public class CompanyRepresentative extends User {
    private Company company;
    private String department;
    private String position;
    private Status status;
    private ArrayList<InternshipOpportunity> createdOpportunities = new ArrayList<>();

    public CompanyRepresentative(String email, String name, String password, Company company, String department,
            String position) {
        super(email, name, password, UserRole.COMPANY_REP);
        this.company = company;
        this.department = department;
        this.position = position;
        this.status = Status.PENDING;
    }

    public Company getCompany() {
        return company;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<InternshipOpportunity> getCreatedInternshipOpportunities() {
        return createdOpportunities;
    }

    public void printInfo() {
        System.out.println("ID: " + getUserId() + ", Name: " + getName() +
                ", Company: " + company.getName() + ", Department: " + department +
                ", Position: " + position + ", Status: " + status);
    }

    // public void register() {
    // //Fill in later
    // }

    // public boolean login(String inputID, String inputPassword) {
    // if (status == Status.SUCCESSFUL){
    // return inputID.equals(this.getUserID()) &&
    // inputPassword.equals(this.getPassword());
    // }
    // return false;
    // }

    // public InternshipOpportunity createInternshipOpportunity(String title, String
    // description, Level level, String preferredMajor, String applicationOpenDate,
    // String applicationClosingDate, int numOfSlots, boolean visibility) {
    // InternshipOpportunity oppo1 = new InternshipOpportunity(title, description,
    // preferredMajor, applicationOpenDate, applicationClosingDate, this,
    // numOfSlots, visibility, level);
    // createdOpportunities.add(oppo1);
    // String[][] data = {
    // {oppo1.getTitle(), oppo1.getDescription(), oppo1.getPreferredMajor(),
    // oppo1.getOpenDate(), oppo1.getClosingDate(),this.getUserID(),
    // String.valueOf(oppo1.getNumOfSlots()), String.valueOf(oppo1.getVisibility()),
    // oppo1.getInternshipLevel().toString()}
    // };
    // CompanyRepCSV.write("data/IntershipOpp_List.csv", data);
    // return oppo1;
    // }

    // public boolean approveApplications(InternshipApplication application) {

    // }

    // public boolean toggleVisibility(InternshipOpportunity internOpportunity,
    // boolean value) {
    // internOpportunity.setVisibility(value);
    // return true;
    // }

    // public void viewStudentApplication(Student student) {
    // //Fill in later
    // }
}