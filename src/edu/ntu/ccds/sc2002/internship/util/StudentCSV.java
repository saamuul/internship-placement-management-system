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

public class StudentCSV {

    // Read a CSV file and return rows as arrays of String (splits on comma).
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
            System.out.println("CompanyRepCSV.readCSV error: " + e.getMessage());
        }
        return rows;
    }

    // Append a row (array of columns) to a CSV file. Creates parent directories if

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
            System.out.println("CompanyRepCSV.appendLine error: " + e.getMessage());
            return false;
        }
    }

    // Convenience overload to append an InternshipApplication object directly.
    // This matches usage in Student.applyForInternship which calls
    // csvhelper.appendLine(app, applicationFile);
    public boolean appendLine(InternshipApplication app, String filePath) {
        if (app == null)
            return false;
        return StudentCSV.appendLine(filePath, app.toCSVRow());
    }
}