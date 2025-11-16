package edu.ntu.ccds.sc2002.internship.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.config.DataConfig;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.Company;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.Student;
import edu.ntu.ccds.sc2002.internship.model.User;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IUserRepository;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

/**
 * Implementation of IUserRepository for CSV-based storage.
 * Handles all user data access operations.
 */
public class UserRepository implements IUserRepository {

    @Override
    public Optional<User> findById(String userId) {
        // Try to find in students
        Optional<Student> student = findStudentById(userId);
        if (student.isPresent()) {
            return Optional.of(student.get());
        }

        // Try to find in company reps
        List<CompanyRepresentative> reps = findAllCompanyReps();
        for (CompanyRepresentative rep : reps) {
            if (rep.getUserId().equalsIgnoreCase(userId)) {
                return Optional.of(rep);
            }
        }

        // Try to find in career staff
        List<CareerStaff> staff = findAllCareerStaff();
        for (CareerStaff s : staff) {
            if (s.getUserId().equalsIgnoreCase(userId)) {
                return Optional.of(s);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STUDENT_CSV_PATH);

        // Skip header row
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 8)
                continue;

            // CSV format:
            // StudentID,Name,Password,Major,Year,Email,AppliedInternships,AcceptedInternship
            String studentId = row[0];
            String name = row[1];
            String password = row[2];
            String major = row[3];
            int yearOfStudy = Integer.parseInt(row[4]);
            String email = row[5];

            // Parse applied internships (stored as semicolon-separated IDs)
            List<InternshipApplication> appliedInternships = new ArrayList<>();
            String appliedIds = row[6];
            if (!appliedIds.isEmpty()) {
                // Applications will be loaded separately when needed
            }

            // Parse accepted internship
            InternshipApplication acceptedInternship = null;
            String acceptedId = row[7];
            if (!acceptedId.isEmpty()) {
                // Will be loaded when needed
            }

            students.add(new Student(studentId, name, email, password, yearOfStudy, major,
                    appliedInternships, acceptedInternship));
        }

        return students;
    }

    @Override
    public Optional<Student> findStudentById(String studentId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STUDENT_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equalsIgnoreCase(studentId)) {
                // CSV format:
                // StudentID,Name,Password,Major,Year,Email,AppliedInternships,AcceptedInternship
                String name = row[1];
                String password = row[2];
                String major = row[3];
                int yearOfStudy = Integer.parseInt(row[4]);
                String email = row[5];

                List<InternshipApplication> appliedInternships = new ArrayList<>();
                InternshipApplication acceptedInternship = null;

                return Optional.of(new Student(studentId, name, email, password, yearOfStudy,
                        major, appliedInternships, acceptedInternship));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<CompanyRepresentative> findAllCompanyReps() {
        List<CompanyRepresentative> reps = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.COMPANY_REP_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 8)
                continue;

            // CSV: ID,Name,Password,CompanyName,Department,Position,Email,Status
            String name = row[1];
            String password = row[2];
            String companyName = row[3];
            String department = row[4];
            String position = row[5];
            String email = row[6]; // Use email as userId for login
            Status status = Status.valueOf(row[7].toUpperCase());

            Company company = new Company(companyName, 0);
            CompanyRepresentative rep = new CompanyRepresentative(email, name, email, password,
                    company, department, position);
            rep.setStatus(status);
            reps.add(rep);
        }

        return reps;
    }

    @Override
    public List<CareerStaff> findAllCareerStaff() {
        List<CareerStaff> staffList = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STAFF_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 5)
                continue;

            String staffId = row[0];
            String name = row[1];
            String password = row[2];
            String position = row[3];
            String department = row[4];
            String email = row[5];

            staffList.add(new CareerStaff(staffId, name, email, password, position, department));
        }

        return staffList;
    }

    @Override
    public boolean save(User user) {
        if (user instanceof Student) {
            return saveStudent((Student) user);
        } else if (user instanceof CompanyRepresentative) {
            return saveCompanyRep((CompanyRepresentative) user);
        } else if (user instanceof CareerStaff) {
            return saveCareerStaff((CareerStaff) user);
        }
        return false;
    }

    private boolean saveStudent(Student student) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STUDENT_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(student.getUserId())) {
                row[1] = student.getName();
                row[2] = student.getPassword();
                row[3] = student.getEmail();
                row[4] = String.valueOf(student.getYearOfStudy());
                row[5] = student.getMajor();
                // row[6] and row[7] (applications) handled separately
                return CSVUtil.updateRow(DataConfig.STUDENT_CSV_PATH, i, row);
            }
        }
        return false;
    }

    private boolean saveCompanyRep(CompanyRepresentative rep) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.COMPANY_REP_CSV_PATH);

        // Try to update existing rep
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[6].equalsIgnoreCase(rep.getUserId())) { // Email is at index 6
                row[1] = rep.getName();
                row[2] = rep.getPassword();
                row[3] = rep.getCompany().getName();
                row[4] = rep.getDepartment();
                row[5] = rep.getPosition();
                row[6] = rep.getUserId(); // Email
                row[7] = rep.getStatus().toString();
                return CSVUtil.updateRow(DataConfig.COMPANY_REP_CSV_PATH, i, row);
            }
        }

        // If not found, append new rep
        String newId = String.valueOf(rows.size()); // Next ID
        String[] newRow = {
                newId,
                rep.getName(),
                rep.getPassword(),
                rep.getCompany().getName(),
                rep.getDepartment(),
                rep.getPosition(),
                rep.getUserId(), // Email
                rep.getStatus().toString()
        };
        return CSVUtil.appendRow(DataConfig.COMPANY_REP_CSV_PATH, newRow);
    }

    private boolean saveCareerStaff(CareerStaff staff) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STAFF_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(staff.getUserId())) {
                row[1] = staff.getName();
                row[2] = staff.getPassword();
                row[3] = staff.getEmail();
                row[4] = staff.getDepartment();
                return CSVUtil.updateRow(DataConfig.STAFF_CSV_PATH, i, row);
            }
        }
        return false;
    }

    @Override
    public boolean updatePassword(String userId, String newPassword) {
        Optional<User> userOpt = findById(userId);
        if (!userOpt.isPresent()) {
            return false;
        }

        User user = userOpt.get();
        user.setPassword(newPassword);
        return save(user);
    }

    @Override
    public String getAcceptedInternshipId(String studentId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STUDENT_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equalsIgnoreCase(studentId)) {
                String acceptedId = row[7].trim();
                return acceptedId.isEmpty() ? null : acceptedId;
            }
        }

        return null;
    }

    @Override
    public boolean addAppliedInternship(String studentId, String applicationId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STUDENT_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equalsIgnoreCase(studentId)) {
                String existing = row[6].trim();
                if (existing.isEmpty()) {
                    row[6] = applicationId;
                } else {
                    row[6] = existing + ";" + applicationId;
                }
                return CSVUtil.updateRow(DataConfig.STUDENT_CSV_PATH, i, row);
            }
        }

        return false;
    }

    @Override
    public boolean setAcceptedInternship(String studentId, String applicationId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STUDENT_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equalsIgnoreCase(studentId)) {
                row[6] = ""; // Clear applied internships
                row[7] = applicationId; // Set accepted internship
                return CSVUtil.updateRow(DataConfig.STUDENT_CSV_PATH, i, row);
            }
        }

        return false;
    }

    @Override
    public boolean clearStudentInternships(String studentId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.STUDENT_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equalsIgnoreCase(studentId)) {
                row[6] = ""; // Clear applied internships
                row[7] = ""; // Clear accepted internship
                return CSVUtil.updateRow(DataConfig.STUDENT_CSV_PATH, i, row);
            }
        }

        return false;
    }
}
