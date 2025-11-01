package edu.ntu.ccds.sc2002.internship.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CareerStaff extends User {
    private String email;
    private String department;
    private String role;

    public CareerStaff(String userID, String name, String role, String email, String department) {
        super(userID, name);
        this.role = role;
        this.email = email;
        this.department = department;
    }

    private static ArrayList<CareerStaff> registeredStaff = new ArrayList<>();

    public void autoRegister(File staffListFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(staffListFile))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String userID = parts[0].trim();
                    String name = parts[1].trim();
                    String role = parts[2].trim();
                    String department = parts[3].trim();
                    String email = parts[4].trim();

                    CareerStaff staff = new CareerStaff(userID, name, role, email, department);
                    registeredStaff.add(staff);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CareerStaff> getRegisteredStaff() {
        return registeredStaff;
    }

    public boolean authoriseComRepAcc(CompanyRepresentative comrep) {

        // Condition 1: Must be linked to an existing company
        if (comrep.getCompany() == null) {
            return false;
        }

        // Condition 3: Status cannot already be approved
        if (comrep.getStatus() == Status.SUCCESSFUL) {
            return false;
        }

        // If all conditions pass → Approve representative
        comrep.setStatus(Status.SUCCESSFUL);
        return true;
    }

    public boolean approveOpportunity(InternshipOpportunity intopp) {
        // Conditions to be added later
        return true;
    }
    public boolean approveWithdrawal(InternshipApplication intappl) {
        //Conditions to be added later
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
        Report report = new Report(f, generatedBy);
        return report;
    }
}
