package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

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
        this.createdOpportunities = getCreatedInternshipOpportunities();
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
        String filePath = "data/Internship_Opportunity_List.csv";
        List<String[]> data = CSVUtil.readCSV(filePath);
        ArrayList<InternshipOpportunity> result = new ArrayList<>();

        String[] header = data.get(0);
        int repName = -1;
        for (int i = 0; i < header.length; i++) {
            if (header[i].equalsIgnoreCase("RepName")) {
                repName = i;
                break;
            }
        }

        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            String name = row[repName];

            // Check if this ID is in the rep's list
            boolean matches = this.getName().equals(name);

            if (matches) {
                InternshipOpportunity newData = new InternshipOpportunity(row[0], row[1], row[2], row[3], row[4],
                        row[5], this, Integer.parseInt(row[7]), Level.valueOf(row[10]));
                newData.setVisibility(Boolean.parseBoolean(row[8]));
                newData.setStatus(row[9]);
                result.add(newData);
            }
        }
        return result;
    }

    public String getInfo() {
        return "ID: " + getUserId() + ", Name: " + getName() +
                ", Company: " + company.getName() + ", Department: " + department +
                ", Position: " + position + ", Status: " + status;
    }

    public OperationResult createInternshipOpportunity(String title, String description, Level level,
            String preferredMajor, String applicationOpenDate,
            String applicationClosingDate, int numOfSlots) {

        int count = CSVUtil.countDataRows("data/Internship_Opportunity_List.csv");
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
                String.valueOf(oppo1.getVisibility()),
                oppo1.getStatus().toString(),
                oppo1.getLevel().toString()
        };

        CSVUtil.appendRow("data/Internship_Opportunity_List.csv", row);
        return OperationResult.success("Successfully created Internship Opportunity: " + oppo1.getTitle());
    }

    public OperationResult reviewApplications(InternshipApplication application, Status status) {
        application.toggleStatus(status);
        int index = 0;
        String[] saved = null;
        List<String[]> datas = CSVUtil.readCSV("data/Internship_Applications_List.csv");
        for (String[] data : datas) {
            if (data[0].equals(application.getApplicationID())) {
                saved = data;
                saved[3] = status.toString();
                CSVUtil.updateRow("data/Internship_Applications_List.csv", index, saved);
                break;
            }
            index++;
        }

        if (saved == null) {
            return OperationResult.failure("Internship Application not found.");
        }

        String message = "Application Changed! Application ID: " + application.getApplicationID() +
                ", StudentID: " + application.getStudentID() +
                ", Status: " + application.getStatus().toString();
        return OperationResult.success(message);
    }
    
    /*
     * public OperationResult toggleVisibility(String choice, boolean value) {
     * 
     * boolean updated = false;
     * 
     * for (InternshipOpportunity row : this.createdOpportunities){
     * if(row.getInternshipID() == choice){
     * row.setVisibility(value);
     * updated = true;
     * 
     * break;
     * }
     * }
     * 
     * if(updated == true){
     * return OperationResult.success("Successfully toggled visibility to" + value);
     * }else{
     * return OperationResult.failure("Failed to toggle visibility");
     * }
     * }
     */

    public OperationResult toggleVisibilityForRep(String choice, String value) {

        // 1️⃣ Validate input string
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            return OperationResult.failure("Invalid visibility value: " + value + ". Must be 'true' or 'false'.");
        }

        boolean boolValue = Boolean.parseBoolean(value); // convert string to boolean

        boolean updated = false;

        // 2️⃣ Update in-memory object
        for (InternshipOpportunity row : this.createdOpportunities) {
            if (row.getInternshipID().equals(choice)) {
                row.setVisibility(boolValue); // use boolean for internal state
                updated = true;
                break;
            }
        }

        if (!updated) {
            return OperationResult.failure("Failed to toggle visibility: ID not found");
        }

        // 3️⃣ Update the CSV
        String filePath = "data/Internship_Opportunity_List.csv";
        List<String[]> data = CSVUtil.readCSV(filePath);

        if (data.isEmpty()) {
            return OperationResult.failure("CSV file is empty");
        }

        // Find column indexes
        String[] header = data.get(0);
        int idCol = -1;
        int visCol = -1;

        for (int i = 0; i < header.length; i++) {
            if (header[i].equalsIgnoreCase("ID"))
                idCol = i;
            if (header[i].equalsIgnoreCase("Visibility"))
                visCol = i;
        }

        if (idCol == -1 || visCol == -1) {
            return OperationResult.failure("CSV missing required columns");
        }

        // Find row in CSV by ID
        int csvRowIndex = -1;
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i)[idCol].equals(choice)) {
                csvRowIndex = i;
                break;
            }
        }

        if (csvRowIndex != -1) {
            data.get(csvRowIndex)[visCol] = value.toLowerCase(); // store string "true"/"false" in CSV
            boolean saved = CSVUtil.writeAllRows(filePath, data);
            if (!saved) {
                return OperationResult.failure("Failed to save changes to CSV");
            }
        }

        return OperationResult.success("Successfully toggled visibility to " + value.toLowerCase());
    }

    public List<InternshipApplication> getPendingInternshipApplications() {
        List<InternshipApplication> filteredList = this.getFilteredInternshipApplication();
        List<InternshipApplication> result = new ArrayList<>();
        for (InternshipApplication row : filteredList) {
            if (row.getStatus() == Status.PENDING) {
                result.add(row);
            }
        }
        return result;
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
                InternshipApplication newData = new InternshipApplication(row[0], row[1], row[2],
                        Status.valueOf(row[3]));
                result.add(newData);
            }
        }
        return result;
    }

    // Override the method at User.java to save new password into the company rep
    // csv file
    protected boolean savePasswordChange() {
        List<String[]> rows = CSVUtil.readCSV("data/Company_Representative_List.csv");

        // Start from 1 to skip header row
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Ensure correct company rep to update the password
            if (row[0].equals(getUserId())) {
                row[2] = getPassword();
                return CSVUtil.updateRow("data/Company_Representative_List.csv", i, row);
            }
        }

        return false; // Company Rep not found
    }

}