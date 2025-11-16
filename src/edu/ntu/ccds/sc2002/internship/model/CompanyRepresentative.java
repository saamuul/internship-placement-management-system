package edu.ntu.ccds.sc2002.internship.model;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.CSVUtil;
import edu.ntu.ccds.sc2002.internship.config.DataConfig;
import edu.ntu.ccds.sc2002.internship.enums.UserRole;
import edu.ntu.ccds.sc2002.internship.enums.Status;

public class CompanyRepresentative extends User {
    private Company company;
    private String department;
    private String position;
    private Status status;

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

    // Override the method at User.java to save new password into the company rep
    // csv file
    @Override
    protected boolean savePasswordChange() {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.COMPANY_REP_CSV_PATH);

        // Start from 1 to skip header row
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Ensure correct company rep to update the password
            if (row[0].equals(getUserId())) {
                row[2] = getPassword();
                return CSVUtil.updateRow(DataConfig.COMPANY_REP_CSV_PATH, i, row);
            }
        }

        return false; // Company Rep not found
    }

}