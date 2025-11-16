package edu.ntu.ccds.sc2002.internship.model;

import java.util.List;

import edu.ntu.ccds.sc2002.internship.util.CSVUtil;
import edu.ntu.ccds.sc2002.internship.config.DataConfig;
import edu.ntu.ccds.sc2002.internship.enums.UserRole;

public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<InternshipApplication> appliedInternships;
    private InternshipApplication acceptedInternship;

    public Student(String studentId, String name, String email, String password, int yearOfStudy, String major,
            List<InternshipApplication> appliedInternships, InternshipApplication acceptedInternship) {
        super(studentId, name, email, password, UserRole.STUDENT);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.appliedInternships = appliedInternships;
        this.acceptedInternship = acceptedInternship;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public List<InternshipApplication> getAppliedInternships() {
        return appliedInternships;
    }

    public InternshipApplication getAcceptedInternship() {
        return acceptedInternship;
    }

    // Override the method at User.java to save new password into the student list
    // csv file
    @Override
    protected boolean savePasswordChange() {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STUDENT_CSV_PATH);

        // Start from 1 to skip header row
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);

            // Ensure correct student to update the password
            if (row[0].equals(getUserId())) {
                row[2] = getPassword(); // Updates password column
                return CSVUtil.updateRow(DataConfig.STUDENT_CSV_PATH, i, row); // Save it back to the csv file
            }
        }

        // No student of that userid is found
        return false;
    }
}