package edu.ntu.ccds.sc2002.internship.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.config.DataConfig;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IWithdrawalRepository;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

/**
 * Implementation of IWithdrawalRepository for CSV-based storage.
 */
public class WithdrawalRepository implements IWithdrawalRepository {

    @Override
    public Optional<InternshipApplication> findById(String applicationId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.WITHDRAWAL_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equalsIgnoreCase(applicationId)) {
                return Optional.of(parseWithdrawal(row));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<InternshipApplication> findAll() {
        List<InternshipApplication> withdrawals = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.WITHDRAWAL_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            withdrawals.add(parseWithdrawal(rows.get(i)));
        }

        return withdrawals;
    }

    @Override
    public List<InternshipApplication> findByStatus(Status status) {
        List<InternshipApplication> withdrawals = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.WITHDRAWAL_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length > 3 && row[3].trim().equalsIgnoreCase(status.toString())) {
                withdrawals.add(parseWithdrawal(row));
            }
        }

        return withdrawals;
    }

    @Override
    public boolean save(InternshipApplication withdrawal) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.WITHDRAWAL_CSV_PATH);

        // Try to find and update existing
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(withdrawal.getApplicationID())) {
                String[] newRow = {
                        withdrawal.getApplicationID(),
                        withdrawal.getStudentID(),
                        withdrawal.getInternshipID(),
                        withdrawal.getStatus().toString()
                };
                return CSVUtil.updateRow(DataConfig.WITHDRAWAL_CSV_PATH, i, newRow);
            }
        }

        // If not found, append new
        String[] newRow = {
                withdrawal.getApplicationID(),
                withdrawal.getStudentID(),
                withdrawal.getInternshipID(),
                withdrawal.getStatus().toString()
        };
        CSVUtil.appendRow(DataConfig.WITHDRAWAL_CSV_PATH, newRow);
        return true;
    }

    @Override
    public boolean updateStatus(String applicationId, Status status) {
        int count = CSVUtil.updateMatchingRows(
                DataConfig.WITHDRAWAL_CSV_PATH,
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
    public boolean exists(String applicationId, String studentId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.WITHDRAWAL_CSV_PATH);

        for (String[] row : rows) {
            if (!row[0].equalsIgnoreCase("AppID") &&
                    row[0].equalsIgnoreCase(applicationId) &&
                    row[1].equalsIgnoreCase(studentId)) {
                return true;
            }
        }

        return false;
    }

    private InternshipApplication parseWithdrawal(String[] row) {
        // CSV format: AppID,StudentID,InternshipID,Status
        String appId = row[0];
        String studentId = row[1];
        String internshipId = row[2];
        Status status = row.length > 3 ? Status.valueOf(row[3].toUpperCase()) : Status.PENDING;

        return new InternshipApplication(appId, studentId, internshipId, status);
    }
}
