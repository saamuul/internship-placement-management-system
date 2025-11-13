package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

public class CareerStaff extends User {
    private static final String REPRESENTATIVE_CSV_PATH = "data/Company_Representative_List.csv";
    private static final String WITHDRAWAL_CSV_PATH = "data/Internship_Withdrawal_Request_List.csv";
    private static final String OPPORTUNITY_CSV_PATH = "data/Internship_Opportunity_List.csv";
    private static final String STAFF_CSV_PATH = "data/Staff_List.csv";

    private String department;
    private String position;
    private String email;

    private static ArrayList<CareerStaff> registeredStaff = new ArrayList<>();

    public CareerStaff(String staffId, String name, String email, String password, String position, String department) {
        super(staffId, name, email, password, UserRole.CAREER_STAFF);
        this.position = position;
        this.department = department;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getPosition() {
        return this.position;
    }

    public static ArrayList<CareerStaff> getRegisteredStaff() {
        return registeredStaff;
    }

    public boolean approveOpportunity(InternshipOpportunity internshipOpportunity) {
        int updatedCount = CSVUtil.updateMatchingRows(
                OPPORTUNITY_CSV_PATH,
                row -> row.length > 0 && row[0].equals(internshipOpportunity.getInternshipID()), 
                row -> {
                    if (row.length >= 9) {
                        row[9] = "SUCCESSFUL"; 
                    }
                    return row;
                });
        return updatedCount > 0;
    }

    public boolean approveWithdrawal(InternshipApplication internshipApplication) {
        int updatedCount = CSVUtil.updateMatchingRows(
                WITHDRAWAL_CSV_PATH,
                row -> row.length > 0 && row[0].equals(internshipApplication.getApplicationID()), 
                row -> {
                    if (row.length >= 3) {
                        row[3] = "SUCCESSFUL"; 
                    }
                    return row;
                });
        return updatedCount > 0;
    }

    public boolean authoriseComRepAcc(CompanyRepresentative companyRep) {
        int updatedCount = CSVUtil.updateMatchingRows(
                REPRESENTATIVE_CSV_PATH,
                row -> row.length > 0 && row[0].equals(companyRep.getUserId()), 
                row -> {
                    if (row.length > 7) {
                        row[7] = "SUCCESSFUL";
                    }
                    return row;
                });
        return updatedCount > 0;
    }

    public Report generateReport(Filter filter, CareerStaff generatedBy) {
        return new Report(filter, generatedBy);
    }

    // Override the method at User.java to save new password into the career staff csv file
    protected boolean savePasswordChange() {
        List<String[]> rows = CSVUtil.readCSV(STAFF_CSV_PATH);

        // Start from 1 to skip header row
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Ensure correct career staff to update the password
            if (row[0].equals(getUserId())) {
                row[2] = getPassword();
                return CSVUtil.updateRow(STAFF_CSV_PATH, i, row);
            }
        }

        return false; // Career Staff not found
    }

}
