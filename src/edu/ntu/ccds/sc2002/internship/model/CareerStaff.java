package edu.ntu.ccds.sc2002.internship.model;

import java.util.ArrayList;

import edu.ntu.ccds.sc2002.internship.util.CareerStaffCSV;

public class CareerStaff extends User {
    private static final String STAFF_CSV_PATH = "data/sample_company_representative_list.csv";

    private String department;
    private String position;
    private String email;

    private static ArrayList<CareerStaff> registeredStaff = new ArrayList<>();

    public CareerStaff(String staffID, String name, String email, String password, String position, String department) {
        super(staffID, name, email, password, UserRole.CAREER_STAFF);
        this.position = position;
        this.department = department;
    }

    public String getEmail() { return this.email; }
    public String getDepartment() { return this.department; }
    public String getPosition() { return this.position; }
    public static ArrayList<CareerStaff> getRegisteredStaff() { return registeredStaff;}

    public boolean approveOpportunity(InternshipOpportunity intopp) { return true; }
    public boolean approveWithdrawal(InternshipApplication intappl) { return true; }

    public boolean authoriseComRepAcc(CompanyRepresentative comrep) {
        return CareerStaffCSV.updateStatus(STAFF_CSV_PATH, Integer.parseInt(comrep.getUserId()), "SUCCESSFUL");
    }

    public Report generateReport(Filter f, CareerStaff generatedBy) {
        return new Report(f, generatedBy);
    }
}
