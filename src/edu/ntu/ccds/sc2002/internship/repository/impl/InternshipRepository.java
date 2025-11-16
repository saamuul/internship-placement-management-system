package edu.ntu.ccds.sc2002.internship.repository.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.config.DataConfig;
import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.Company;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.repository.interfaces.IInternshipRepository;
import edu.ntu.ccds.sc2002.internship.util.CSVUtil;

/**
 * Implementation of IInternshipRepository for CSV-based storage.
 */
public class InternshipRepository implements IInternshipRepository {

    @Override
    public Optional<InternshipOpportunity> findById(String internshipId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.OPPORTUNITY_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equalsIgnoreCase(internshipId)) {
                return Optional.of(parseOpportunity(row));
            }
        }

        return Optional.empty();
    }

    @Override
    public List<InternshipOpportunity> findAll() {
        List<InternshipOpportunity> opportunities = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.OPPORTUNITY_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            opportunities.add(parseOpportunity(rows.get(i)));
        }

        return opportunities;
    }

    @Override
    public List<InternshipOpportunity> findByStatus(Status status) {
        List<InternshipOpportunity> opportunities = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.OPPORTUNITY_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length > 9 && row[9].trim().equalsIgnoreCase(status.toString())) {
                opportunities.add(parseOpportunity(row));
            }
        }

        return opportunities;
    }

    @Override
    public List<InternshipOpportunity> findByRepresentativeId(String repId) {
        List<InternshipOpportunity> opportunities = new ArrayList<>();
        List<String[]> rows = CSVUtil.readCSV(DataConfig.OPPORTUNITY_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length > 6 && row[6].trim().equalsIgnoreCase(repId)) {
                opportunities.add(parseOpportunity(row));
            }
        }

        return opportunities;
    }

    @Override
    public boolean save(InternshipOpportunity opportunity) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.OPPORTUNITY_CSV_PATH);

        // Try to find and update existing
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(opportunity.getInternshipID())) {
                row = opportunityToRow(opportunity);
                return CSVUtil.updateRow(DataConfig.OPPORTUNITY_CSV_PATH, i, row);
            }
        }

        // If not found, append new
        CSVUtil.appendRow(DataConfig.OPPORTUNITY_CSV_PATH, opportunityToRow(opportunity));
        return true;
    }

    @Override
    public boolean updateStatus(String opportunityId, Status status) {
        int count = CSVUtil.updateMatchingRows(
                DataConfig.OPPORTUNITY_CSV_PATH,
                row -> row.length > 0 && row[0].equals(opportunityId),
                row -> {
                    if (row.length >= 10) {
                        row[9] = status.toString();
                    }
                    return row;
                });
        return count > 0;
    }

    @Override
    public boolean updateVisibility(String opportunityId, boolean visible) {
        int count = CSVUtil.updateMatchingRows(
                DataConfig.OPPORTUNITY_CSV_PATH,
                row -> row.length > 0 && row[0].equals(opportunityId),
                row -> {
                    if (row.length >= 9) {
                        row[8] = String.valueOf(visible);
                    }
                    return row;
                });
        return count > 0;
    }

    @Override
    public boolean decrementSlot(String opportunityId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.OPPORTUNITY_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(opportunityId)) {
                int currentSlots = Integer.parseInt(row[7]);

                if (currentSlots <= 0) {
                    return false; // No slots available
                }

                currentSlots--;
                row[7] = String.valueOf(currentSlots);

                // If slots reach 0, mark as FILLED
                if (currentSlots == 0) {
                    row[9] = "FILLED";
                }

                return CSVUtil.updateRow(DataConfig.OPPORTUNITY_CSV_PATH, i, row);
            }
        }

        return false;
    }

    @Override
    public boolean incrementSlot(String opportunityId) {
        List<String[]> rows = CSVUtil.readCSV(DataConfig.OPPORTUNITY_CSV_PATH);

        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row[0].equals(opportunityId)) {
                int currentSlots = Integer.parseInt(row[7]);

                currentSlots++;
                row[7] = String.valueOf(currentSlots);

                // If was FILLED, restore to SUCCESSFUL
                if (row[9].equals("FILLED")) {
                    row[9] = "SUCCESSFUL";
                }

                return CSVUtil.updateRow(DataConfig.OPPORTUNITY_CSV_PATH, i, row);
            }
        }

        return false;
    }

    private InternshipOpportunity parseOpportunity(String[] row) {
        // CSV format:
        // ID,Title,Description,Major,OpenDate,CloseDate,RepName,Slots,Visibility,Status,Level
        String id = row[0];
        String title = row[1];
        String description = row[2];
        String major = row[3];
        String openDate = row[4];
        String closeDate = row[5];
        String repName = row[6];
        int slots = Integer.parseInt(row[7]);
        boolean visibility = Boolean.parseBoolean(row[8]);
        Status status = Status.valueOf(row[9].toUpperCase());
        Level level = Level.valueOf(row[10].toUpperCase());

        // Create a minimal CompanyRepresentative (just for holding the name)
        Company company = new Company(repName, 0);
        CompanyRepresentative rep = new CompanyRepresentative(repName, repName, "", "", company, "", "");

        InternshipOpportunity opp = new InternshipOpportunity(id, title, description, major,
                openDate, closeDate, rep, slots, level);
        opp.setVisibility(visibility);
        opp.setStatus(status.toString());
        return opp;
    }

    private String[] opportunityToRow(InternshipOpportunity opp) {
        String repName = opp.getRep() != null ? opp.getRep().getName() : "";
        return new String[] {
                opp.getInternshipID(),
                opp.getTitle(),
                opp.getDescription(),
                opp.getPrefMajor(),
                opp.getOpenDate(),
                opp.getCloseDate(),
                repName,
                String.valueOf(opp.getNumOfSlots()),
                String.valueOf(opp.getVisibility()),
                opp.getStatus().toString(),
                opp.getLevel().toString()
        };
    }

    @Override
    public int deleteExpiredOpportunities() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int deletedCount = CSVUtil.removeMatchingRows(
                DataConfig.OPPORTUNITY_CSV_PATH,
                row -> {
                    if (row.length <= 5) {
                        return false; // Invalid row, skip
                    }

                    String closeDateStr = row[5].trim(); // closeDate is at index 5

                    try {
                        LocalDate closeDate = LocalDate.parse(closeDateStr, formatter);
                        // Delete if closing date has passed (is before today)
                        return closeDate.isBefore(today);
                    } catch (DateTimeParseException e) {
                        // If date is invalid, don't delete
                        return false;
                    }
                });

        return deletedCount;
    }
}
