package edu.ntu.ccds.sc2002.internship.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.config.DataConfig;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IApplicationRepository;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

/**
 * Implementation of IApplicationRepository for CSV-based storage.
 */
public class ApplicationRepository implements IApplicationRepository {

    @Override
    public Optional<InternshipApplication> findById(String applicationId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.APPLICATION_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equalsIgnoreCase(applicationId)) {
                return Optional.of(parseApplication(row));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<InternshipApplication> findAll() {
        List<InternshipApplication> applications = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.APPLICATION_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            applications.add(parseApplication(rows.get(i)));
        }

        return applications;
    }

    @Override
    public List<InternshipApplication> findByStudentId(String studentId) {
        List<InternshipApplication> applications = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.APPLICATION_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[1].equalsIgnoreCase(studentId)) {
                applications.add(parseApplication(row));
            }
        }

        return applications;
    }

    @Override
    public List<InternshipApplication> findByInternshipId(String internshipId) {
        List<InternshipApplication> applications = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.APPLICATION_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[2].equalsIgnoreCase(internshipId)) {
                applications.add(parseApplication(row));
            }
        }

        return applications;
    }

    @Override
    public List<InternshipApplication> findByStatus(Status status) {
        List<InternshipApplication> applications = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.APPLICATION_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[3].trim().equalsIgnoreCase(status.toString())) {
                applications.add(parseApplication(row));
            }
        }

        return applications;
    }

    @Override
    public boolean save(InternshipApplication application) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.APPLICATION_CSV_PATH);

        // Try to find and update existing
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(application.getApplicationID())) {
                row = application.toCSVRow();
                return CSVUtil.updateRow(DataConfig.APPLICATION_CSV_PATH, i, row);
            }
        }

        // If not found, append new
        CSVUtil.appendRow(DataConfig.APPLICATION_CSV_PATH, application.toCSVRow());
        return true;
    }

    @Override
    public boolean updateStatus(String applicationId, Status status) {
        int count = CSVUtil.updateMatchingRows(
                DataConfig.APPLICATION_CSV_PATH,
                row -> row.length > 0 && row[0].equals(applicationId),
                row -> {
                    if (row.length >= 4) {
                        row[3] = status.toString();
                    }
                    return row;
                });
        return count > 0;
    }

    @Override
    public boolean delete(String applicationId) {
        int count = CSVUtil.removeMatchingRows(
                DataConfig.APPLICATION_CSV_PATH,
                row -> row[0].equalsIgnoreCase(applicationId));
        return count > 0;
    }

    @Override
    public int deleteByStudentExcept(String studentId, String exceptApplicationId) {
        return CSVUtil.removeMatchingRows(
                DataConfig.APPLICATION_CSV_PATH,
                row -> !row[0].equalsIgnoreCase("ApplicationID") &&
                        row[1].equalsIgnoreCase(studentId) &&
                        !row[0].equalsIgnoreCase(exceptApplicationId));
    }

    @Override
    public String getNextApplicationId() {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.APPLICATION_CSV_PATH);
        int maxId = 0;

        for (String[] row : rows) {
            try {
                int id = Integer.parseInt(row[0].trim());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Skip non-numeric IDs
            }
        }

        return String.valueOf(maxId + 1);
    }

    private InternshipApplication parseApplication(String[] row) {
        // CSV format: ApplicationID,StudentID,InternshipID,Status
        String appId = row[0];
        String studentId = row[1];
        String internshipId = row[2];
        Status status = Status.valueOf(row[3].toUpperCase());

        return new InternshipApplication(appId, studentId, internshipId, status);
    }
}
