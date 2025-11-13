package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.CSVUtil;
import edu.ntu.ccds.sc2002.internship.model.OperationResult;

public class CompanyRepresentative extends User {
    private Company company;
    private String department;
    private String position;
    private Status status;
    private ArrayList<InternshipOpportunity> createdOpportunities = new ArrayList<>();

    public CompanyRepresentative(String companyRepId, String name, String email, String password, Company company,
            String department,
            String position) {
        super(companyRepId, name, email, password, UserRole.COMPANY_REP);
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

    public String getInfo() {
        return "ID: " + getUserId() + ", Name: " + getName() +
                ", Company: " + company.getName() + ", Department: " + department +
                ", Position: " + position + ", Status: " + status;
    }

    public OperationResult createInternshipOpportunity(String title, String description, Level level,
            String preferredMajor, String applicationOpenDate,
            String applicationClosingDate, int numOfSlots) {

            int count = CSVUtil.countDataRows("data/Intership_Opportunity_List.csv");
            int newID = count + 1;
            String sID = String.valueOf(newID);

            InternshipOpportunity oppo1 = new InternshipOpportunity(sID, title, description,
                    preferredMajor, applicationOpenDate, applicationClosingDate, this,
                    numOfSlots, level);
            createdOpportunities.add(oppo1);

            

            
            String[] row = {
                sID,
                oppo1.getTitle(),
                oppo1.getDescription(),
                oppo1.getPrefMajor(),
                oppo1.getOpenDate(),
                oppo1.getCloseDate(),
                this.getName(),
                String.valueOf(oppo1.getNumOfSlots()),
                oppo1.getLevel().toString()
            };

            if(row == null){
                return OperationResult.failure("Internship Opportunity creation failure");
            }

            CSVUtil.appendRow("data/Intership_Opportunity_List.csv", row);
            return OperationResult.success("Successfully created Intership Opportunity: " + oppo1.getTitle());
        }

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

    public List<InternshipApplication> getFilteredInternshipApplication() {
        String filePath = "data/Internship_Applications_List.csv";
        List<String[]> data = CSVUtil.readCSV(filePath);
        List<InternshipApplication> result = new ArrayList<>();

        String[] header = data.get(0);
        int oppIdCol = -1;
        for (int i = 0; i < header.length; i++) {
            if (header[i].equalsIgnoreCase("InternOppID")) {
                oppIdCol = i;
                break;
            }
        }

        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            String internshipID = row[oppIdCol];

            // Check if this ID is in the rep's list
            boolean matches = this.getCreatedInternshipOpportunities()
                            .stream()
                            .anyMatch(o -> o.getInternshipID().equals(internshipID));


            if (matches) {
                InternshipApplication newdata = new InternshipApplication(row[0], row[1], row[2], Status.valueOf(row[3]));
                result.add(newdata);
            }
        }
        return result;
    }

    // Override the method at User.java to save new password into the company rep csv file
    protected boolean savePasswordChange() {
        List<String[]> rows = CSVUtil.readCSV("data/company_representative_list.csv");

        // Start from 1 to skip header row
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Ensure correct company rep to update the password
            if (row[0].equals(getUserId())) {
                row[2] = getPassword();
                return CSVUtil.updateRow("data/company_representative_list.csv", i, row);
            }
        }

        return false; // Company Rep not found
    }

}