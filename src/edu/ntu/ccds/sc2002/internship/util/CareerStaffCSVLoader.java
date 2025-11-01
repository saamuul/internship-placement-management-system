package edu.ntu.ccds.sc2002.internship.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;

public class CareerStaffCSVLoader {

    public static List<String[]> readCSV(String filePath) {
        List<String[]> rows = new ArrayList<>();
        Path p = Paths.get(filePath);
        if (!Files.exists(p)) return rows;

        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] cols = line.split(",", -1); 
                rows.add(cols);
            }
        } catch (IOException e) {
            System.out.println("CareerStaffCSV.readCSV error: " + e.getMessage());
        }
        return rows;
    }

    // Append a new row
    public static boolean appendLine(String filePath, String[] cols) {
        Path p = Paths.get(filePath);
        try {
            if (p.getParent() != null)
                Files.createDirectories(p.getParent());
        } catch (IOException e) { }

        String line = String.join(",", cols);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(line);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("CareerStaffCSV.appendLine error: " + e.getMessage());
            return false;
        }
    }

    // Append a CompanyRepresentative object
    public static boolean appendLine(CompanyRepresentative comRep, String filePath) {
        if (comRep == null) return false;
        String[] row = new String[] {
            String.valueOf(comRep.getCompanyRepID()),
            comRep.getName(),
            comRep.getCompanyName(),
            comRep.getDepartment(),
            comRep.getPosition(),
            comRep.getEmail(),
            comRep.getStatus()
        };
        return appendLine(filePath, row);
    }

    public static boolean updateStatus(String filePath, int companyRepID, String newStatus) {
        List<String[]> rows = readCSV(filePath);
        boolean updated = false;
        for (String[] row : rows) {
            if (row.length > 0 && row[0].equals(String.valueOf(companyRepID))) {
                row[6] = newStatus;
                updated = true;
                break;
            }
        }
        if (updated) {
            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath))) {
                for (String[] row : rows) {
                    bw.write(String.join(",", row));
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("CareerStaffCSV.updateStatus error: " + e.getMessage());
                return false;
            }
        }
        return updated;
    }
}

