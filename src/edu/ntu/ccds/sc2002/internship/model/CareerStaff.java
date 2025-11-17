package edu.ntu.ccds.sc2002.internship.model;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.config.DataConfig;
import edu.ntu.ccds.sc2002.internship.enums.UserRole;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

public class CareerStaff extends User {

    private String department;
    private String position;
    private String email;

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

    // Override the method at User.java to save new password into the career staff
    // csv file
    @Override
    protected boolean savePasswordChange() {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STAFF_CSV_PATH);

        // Start from 1 to skip header row
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Ensure correct career staff to update the password
            if (row[0].equals(getUserId())) {
                row[2] = getPassword();
                return CSVUtil.updateRow(DataConfig.STAFF_CSV_PATH, i, row);
            }
        }

        return false; // Career Staff not found
    }
}
