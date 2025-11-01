package edu.ntu.ccds.sc2002.internship.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import util.CareerStaffCSVLoader;

public class CareerStaff extends User {
    private static final String STAFF_CSV_PATH = "data/sample_company_representative_list.csv";

    private String email;
    private String department;
    private String role;

    private static ArrayList<CareerStaff> registeredStaff = new ArrayList<>();

    public CareerStaff(String userID, String name, String role, String email, String department) {
        super(userID, name);
        this.role = role;
        this.email = email;
        this.department = department;
    }

    public void autoRegister(File staffListFile) {
        List<String[]> rows = CareerStaffCSVLoader.readCSV(staffListFile.getPath());
        registeredStaff.clear(); // optional: clear previous registrations
        for (String[] parts : rows) {
            if (parts.length >= 7) { // CSV has headers + status
                String userID = parts[0].trim();
                String name = parts[1].trim();
                String company = parts[2].trim();
                String department = parts[3].trim();
                String role = parts[4].trim();
                String email = parts[5].trim();

                CareerStaff staff = new CareerStaff(userID, name, role, email, department);
                registeredStaff.add(staff);
            }
        }
    }

    public static ArrayList<CareerStaff> getRegisteredStaff() {
        return registeredStaff;
    }

    public boolean authoriseComRepAcc(CompanyRepresentative comrep) {
        return CareerStaffCSVLoader.updateStatus(STAFF_CSV_PATH, Integer.parseInt(comrep.getUserID()), "SUCCESSFUL");
    }

    public boolean approveOpportunity(InternshipOpportunity intopp) {
        return true; 
    }

    public boolean approveWithdrawal(InternshipApplication intappl) {
        return true;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getRole() {
        return this.role;
    }

    public Report generateReport(Filter f, CareerStaff generatedBy) {
        return new Report(f, generatedBy);
    }
}
