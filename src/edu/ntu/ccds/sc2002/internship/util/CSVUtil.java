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

/**
 * Unified CSV utility class for reading and writing CSV files.
 * UTILITY LAYER: Provides generic CSV operations for all entities.
 */
public class CSVUtil {

    // Read all rows from a CSV file.
    public static List<String[]> readCSV(String filePath) {
        List<String[]> rows = new ArrayList<>();
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return rows; // Return empty list if file doesn't exist
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                // Split with limit=-1 to preserve empty trailing columns
                String[] cols = line.split(",", -1);
                rows.add(cols);
            }
        } catch (IOException e) {
            System.err.println("[CSVUtil] Error reading file '" + filePath + "': " + e.getMessage());
        }

        return rows;
    }

    /**
     * Append a single row to a CSV file.
     * Creates parent directories if needed.
     */
    public static boolean appendRow(String filePath, String[] row) {
        Path path = Paths.get(filePath);

        try {
            // Create parent directories if they don't exist
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            // Append the row
            try (BufferedWriter bw = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
            return true;

        } catch (IOException e) {
            System.err.println("[CSVUtil] Error appending to file '" + filePath + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Write all rows to a CSV file (overwrites existing file).
     * Creates parent directories if needed.
     */
    public static boolean writeAllRows(String filePath, List<String[]> rows) {
        Path path = Paths.get(filePath);

        try {
            // Create parent directories if they don't exist
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            // Write all rows (truncate existing file)
            try (BufferedWriter bw = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (String[] row : rows) {
                    bw.write(String.join(",", row));
                    bw.newLine();
                }
            }
            return true;

        } catch (IOException e) {
            System.err.println("[CSVUtil] Error writing file '" + filePath + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * Update a specific row in a CSV file.
     * Reads entire file, modifies the row, writes back.
     */
    public static boolean updateRow(String filePath, int rowIndex, String[] newRow) {
        List<String[]> rows = readCSV(filePath);

        if (rowIndex < 0 || rowIndex >= rows.size()) {
            System.err.println("[CSVUtil] Invalid row index: " + rowIndex);
            return false;
        }

        rows.set(rowIndex, newRow);
        return writeAllRows(filePath, rows);
    }

    /**
     * Update rows based on a condition and updater function.
     * Useful for updating specific columns in matching rows.
     */
    public static int updateMatchingRows(String filePath,
            RowMatcher matcher,
            RowUpdater updater) {
        List<String[]> rows = readCSV(filePath);
        int updateCount = 0;

        for (int i = 0; i < rows.size(); i++) {
            if (matcher.matches(rows.get(i))) {
                rows.set(i, updater.update(rows.get(i)));
                updateCount++;
            }
        }

        if (updateCount > 0) {
            writeAllRows(filePath, rows);
        }

        return updateCount;
    }

    /**
     * Remove rows based on a condition
     * Useful for deleting matching rows from the CSV file
     */
    public static int removeMatchingRows(String filePath, RowMatcher matcher) {
        List<String[]> rows = readCSV(filePath);
        
        //Skip if file is empty
        if (rows.isEmpty()) return 0;

        int originalSize = rows.size();
        // Only keep rows that does not match the condition
        rows.removeIf(matcher::matches);

        // Write back only if anything changed
        if (rows.size() < originalSize) {
            writeAllRows(filePath, rows);
        }

        return originalSize - rows.size(); // return number of rows removed
    }


    public static int countDataRows(String filePath) {
    List<String[]> rows = readCSV(filePath);

    // Subtract 1 to exclude the header row, if it exists
    if (rows.size() > 0) {
        return rows.size() - 1;
    } else {
        return 0;
    }
    }


    // Functional interface for matching rows.
    public interface RowMatcher {
        boolean matches(String[] row);
    }

    // Functional interface for updating rows.
    public interface RowUpdater {
        String[] update(String[] row);
    }
}
