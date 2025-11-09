package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;

import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

public class CareerStaff extends User {
    private static final String REPRESENTATIVE_CSV_PATH = "data/company_representative_list.csv";
    private static final String WITHDRAWAL_CSV_PATH = "data/Internship_Withdrawal_Request_List.csv";
    private static final String OPPORTUNITY_CSV_PATH = "data/internship_opportunity_list.csv";

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
}
