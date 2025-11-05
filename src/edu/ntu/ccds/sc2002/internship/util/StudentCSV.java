package edu.ntu.ccds.sc2002.internship.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.Student;

/**
 * Utility class for reading and writing Student data to CSV files.
 * CSV Format:
 * StudentID,Name,Password,Major,Year,Email,AppliedInternships,AcceptedInternship
 * 
 * - AppliedInternships: Semicolon-separated list of application IDs (e.g.,
 * "APP001;APP002;APP003")
 * - AcceptedInternship: Single application ID or empty if none accepted
 */
public class StudentCSV {

    /**
     * Read a CSV file and return rows as arrays of String (splits on comma).
     */
    public static List<String[]> readCSV(String filePath) {
        List<String[]> rows = new ArrayList<>();
        Path p = Paths.get(filePath);
        if (!Files.exists(p))
            return rows;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                String[] cols = line.split(",", -1);
                rows.add(cols);
            }
        } catch (IOException e) {
            System.out.println("StudentCSV.readCSV error: " + e.getMessage());
        }
        return rows;
    }

    /**
     * Append a row (array of columns) to a CSV file. Creates parent directories if
     * needed.
     */
    public static boolean appendLine(String filePath, String[] cols) {
        Path p = Paths.get(filePath);
        try {
            if (p.getParent() != null)
                Files.createDirectories(p.getParent());
        } catch (IOException e) {
            // ignore directory creation failure, will surface on write
        }

        String line = String.join(",", cols);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(line);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("StudentCSV.appendLine error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convenience overload to append an InternshipApplication object directly.
     */
    public boolean appendLine(InternshipApplication app, String filePath) {
        if (app == null)
            return false;
        return StudentCSV.appendLine(filePath, app.toCSVRow());
    }

    /**
     * Write or update a student's record in the CSV file.
     * Updates the entire file, replacing the student's row if it exists.
     */
    public static boolean updateStudent(String filePath, Student student) {
        List<String[]> rows = readCSV(filePath);
        boolean updated = false;

        // Create the student row
        String[] studentRow = studentToCSVRow(student);

        // Update existing student or prepare to add new one
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).length > 0 && rows.get(i)[0].equals(student.getUserId())) {
                rows.set(i, studentRow);
                updated = true;
                break;
            }
        }

        // If student not found, add as new row
        if (!updated) {
            rows.add(studentRow);
        }

        // Write all rows back to file
        return writeAllRows(filePath, rows);
    }

    /**
     * Convert a Student object to CSV row format.
     * Format:
     * StudentID,Name,Password,Major,Year,Email,AppliedInternships,AcceptedInternship
     */
    private static String[] studentToCSVRow(Student student) {
        // TODO: Implement logic to serialize appliedInternships and acceptedInternship
        // For now, using empty strings as placeholders
        String appliedInternships = ""; // TODO: Join application IDs with semicolon
        String acceptedInternship = ""; // TODO: Get accepted internship ID

        return new String[] {
                student.getUserId(),
                student.getName(),
                student.getPassword(),
                student.getMajor(),
                String.valueOf(student.getYearOfStudy()),
                student.getUserId(), // Using userID as email for now, update if separate email field exists
                appliedInternships,
                acceptedInternship
        };
    }

    /**
     * Write all rows to a CSV file, replacing existing content.
     */
    private static boolean writeAllRows(String filePath, List<String[]> rows) {
        Path p = Paths.get(filePath);
        try {
            if (p.getParent() != null)
                Files.createDirectories(p.getParent());
        } catch (IOException e) {
            System.out.println("StudentCSV.writeAllRows: Failed to create directories - " + e.getMessage());
            return false;
        }

        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String[] row : rows) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("StudentCSV.writeAllRows error: " + e.getMessage());
            return false;
        }
    }
}