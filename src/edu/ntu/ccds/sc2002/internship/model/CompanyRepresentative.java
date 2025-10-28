package model;
import java.util.ArrayList;
import util.CSVLoader;

public class CompanyRepresentative extends User{
    private Company company;
    private String department;
    private String position;
    private String email;
    private InternshipStatus status;
    private ArrayList<InternshipOpportunity> createdOpportunities = new ArrayList<>();

    public CompanyRepresentative(String userID, String name, Company company, String department, String position, String email) {
        super(userID,name);
        this.company = company;
        this.department = department;
        this.position = position;
        this.email = email;
    }

    public void register() {
        //Fill in later
    }

    public boolean login(String inputID, String inputPassword) {
        if (status == InternshipStatus.SUCCESSFUL){
            return inputID.equals(this.getUserID()) && inputPassword.equals(this.getPassword());
        }
        return false;
    }

    public InternshipOpportunity createInternshipOpportunity(String title, String description, InternshipLevel level, String preferredMajor, String applicationOpenDate, String applicationClosingDate, int numOfSlots, boolean visibility) {
        InternshipOpportunity oppo1 = new InternshipOpportunity(title, description, preferredMajor, applicationOpenDate, applicationClosingDate, this, numOfSlots, visibility, level);
        createdOpportunities.add(oppo1);
        String[][] data = {
            {oppo1.getTitle(), oppo1.getDescription(), oppo1.getPreferredMajor(),
            oppo1.getOpenDate(), oppo1.getClosingDate(),this.getUserID(), 
            String.valueOf(oppo1.getNumOfSlots()), String.valueOf(oppo1.getVisibility()),
            oppo1.getInternshipLevel().toString()}
        };
        CSVLoader.write("data/IntershipOpp_List.csv", data);
        return oppo1;
    }

    public boolean approveApplications(InternshipApplication application) {
        
    }

    public boolean toggleVisibility(InternshipOpportunity internOpportunity, boolean value) {
        internOpportunity.setVisibility(value);
        return true;
    }
    public Company getCompany(){
        return company;
    }
    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public InternshipStatus getStatus() {
        return status;
    }

    public ArrayList<InternshipOpportunity> getCreatedInternshipOpportunities() {
        return createdOpportunities;
    }

    public void viewStudentApplication(Student student) {
        //Fill in later
    }
}