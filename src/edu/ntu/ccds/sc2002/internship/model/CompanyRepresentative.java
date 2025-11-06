package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;

import edu.ntu.ccds.sc2002.internship.util.CSVLoader;

public class CompanyRepresentative extends User {
    private Company company;
    private String department;
    private String position;
    private Status status;
    private ArrayList<InternshipOpportunity> createdOpportunities = new ArrayList<>();

    public CompanyRepresentative(String companyRepID, String name, String email, String password, Company company,
            String department,
            String position) {
        super(companyRepID, name, email, password, UserRole.COMPANY_REP);
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

    /**
     * Gets representative information as a formatted string.
     * MODEL LAYER: Returns data instead of printing.
     */
    public String getInfo() {
        return "ID: " + getUserId() + ", Name: " + getName() +
                ", Company: " + company.getName() + ", Department: " + department +
                ", Position: " + position + ", Status: " + status;
    }

    public boolean login(String inputID, String inputPassword) {
        if (status == Status.SUCCESSFUL) {
            return inputID.equals(this.getUserId()) &&
                    inputPassword.equals(this.getPassword());
        }
        return false;
    }

    public InternshipOpportunity createInternshipOpportunity(String title, String description, Level level,
            String preferredMajor, String applicationOpenDate,
            String applicationClosingDate, int numOfSlots, boolean visibility) {
        InternshipOpportunity oppo1 = new InternshipOpportunity(title, description,
                preferredMajor, applicationOpenDate, applicationClosingDate, this,
                numOfSlots, visibility, level);
        createdOpportunities.add(oppo1);
        String[][] data = {
                { oppo1.getTitle(), oppo1.getDescription(), oppo1.getPrefMajor(),
                        oppo1.getOpenDate(), oppo1.getCloseDate(), this.getUserId(),
                        String.valueOf(oppo1.getNumOfSlots()), String.valueOf(oppo1.getVisibility()),
                        oppo1.getLevel().toString() }
        };
        CSVLoader.write("data/IntershipOpp_List.csv", data);
        return oppo1;
    }

    /**
     * Reviews an internship application.
     * MODEL LAYER: Returns OperationResult instead of printing.
     */
    public OperationResult reviewApplications(InternshipApplication application, Status status) {
        // application.toggleStatus(status);
        String message = "Application Changed! Application ID: " + application.getApplicationID() +
                ", StudentID: " + application.getStudentID() +
                ", Status: " + application.getStatus().toString();
        return OperationResult.success(message);
    }

    public boolean toggleVisibility(InternshipOpportunity internOpportunity,
            boolean value) {
        internOpportunity.setVisibility(value);
        return true;
    }

    /**
     * Gets all internship applications data.
     * MODEL LAYER: Returns data instead of printing.
     */
    public String[][] viewInternshipApplication() {
        String filePath = "../data/Internship_Applications_List.csv";
        return CSVLoader.read(filePath);
    }
}