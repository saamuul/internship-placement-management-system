package edu.ntu.ccds.sc2002.internship.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.CareerStaffCSV;

public class CareerStaff extends User {
    private static final String STAFF_CSV_PATH = "data/sample_company_representative_list.csv";

    private String department;
    private String position;
    private String email;

    private static ArrayList<CareerStaff> registeredStaff = new ArrayList<>();

    public CareerStaff(String userID, String name, String password, String position, String department, String email ) {
        super(userID, name, password, UserRole.CAREER_STAFF);
        this.position = position;
        this.department = department;
        this.email = email;
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

    public void autoRegister(File staffListFile) {
        List<String[]> rows = CareerStaffCSV.readCSV(staffListFile.getPath());
        registeredStaff.clear(); // optional: clear previous registrations
        for (String[] parts : rows) {
            if (parts.length >= 7) { // CSV has headers + status
                String userID = parts[0].trim();
                String name = parts[1].trim();
                String department = parts[2].trim();
                String position = parts[3].trim();
                String email = parts[4].trim();

                CareerStaff staff = new CareerStaff(userID, name, this.getPassword(), position, department, email);
                registeredStaff.add(staff);
            }
        }
    }

    public Report generateReport(Filter f, CareerStaff generatedBy) {
        return new Report(f, generatedBy);
    }
}
