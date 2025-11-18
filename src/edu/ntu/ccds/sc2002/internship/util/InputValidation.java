package edu.ntu.ccds.sc2002.internship.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import edu.ntu.ccds.sc2002.internship.enums.Status;

public class InputValidation {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public String requireNonEmpty(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Field cannot be empty. Please try again.");
        }
        return input.trim();
    }

    public int parseInt(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid integer value. Please try again with an integer.");
        }
    }

    public int parseIntInRange(String input, int min, int max) {
        int value = parseInt(input);

        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    "Value must be between " + min + " and " + max + ". Please try again.");
        }
        return value;
    }

    public LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please try again. ");
        }
    }

    public LocalDate parseCloseDate(String close, String open) {
        LocalDate openDate = parseDate(open);
        LocalDate closeDate = parseDate(close);

        if (!closeDate.isAfter(openDate)) {
            throw new IllegalArgumentException("Close date must be AFTER open date. Please try again. ");
        }

        return closeDate;
    }

    public Status parseStatus(String input) {
        try {
            return Status.valueOf(input.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status. Please try again. ");
        }
    }

    public String parseLevel(String input) {
        String val = input.trim().toUpperCase();
        if (!val.equals("BASIC") && !val.equals("INTERMEDIATE") && !val.equals("ADVANCED")) {
            throw new IllegalArgumentException("Invalid level. Please try again.");
        }
        return val;
    }

    public String parseVisibility(String input) {
        String val = input.trim().toLowerCase();
        if (!val.equals("true") && !val.equals("false")) {
            throw new IllegalArgumentException("Invalid visibility. Please try again. ");
        }
        return val;
    }
    
    public String parseDateTime(String dateTime) {
        if (dateTime == null || dateTime.trim().isEmpty()) {
            throw new IllegalArgumentException("Date and time cannot be empty.");
        }
        
        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime.trim(), DATETIME_FORMATTER);
            
            // Validate that the datetime is not in the past
            LocalDateTime now = LocalDateTime.now();
            if (parsedDateTime.isBefore(now)) {
                throw new IllegalArgumentException("Interview time cannot be in the past. Please enter a future date and time.");
            }
            
            return parsedDateTime.format(DATETIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date-time format. Please use format: YYYY-MM-DD HH:MM (e.g., 2025-11-20 14:00)");
        }
    }
}
