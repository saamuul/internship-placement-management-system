import java.util.ArrayList;

public class CompanyRepresentative extends User{
    private Company company;
    private String department;
    private String position;
    private String email;
    private Status status;
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

    public boolean login() {
        //Fill in later
    }

    public InternshipOpportunity createInternshipOpportunity(String title, String description, Level level, String preferredMajor, String applicationOpenDate, String applicationClosingDate, Status status, int numOfSlots, boolean visibility) {
        //Fill in later
    }

    public boolean approveApplications(InternshipApplication application) {
        //Fill in later
    }

    public boolean toggleVisibility(InternshipOpportunity internOpportunity) {
        //Fill in later
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

    public Status getStatus() {
        return status;
    }

    public ArrayList<InternshipOpportunity> getCreatedInternshipOpportunities() {
        return createdOpportunities;
    }

    public void viewStudentApplication(Student student) {
        //Fill in later
    }
}