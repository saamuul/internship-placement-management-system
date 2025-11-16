package edu.ntu.ccds.sc2002.internship.config;

/**
 * Configuration class for CSV file paths.
 * Centralizes all file path constants to follow Open/Closed Principle.
 * This makes it easier to change storage implementation in the future.
 */
public class DataConfig {
    // CSV File Paths
    public static final String STUDENT_CSV_PATH = "data/Student_List.csv";
    public static final String STAFF_CSV_PATH = "data/Staff_List.csv";
    public static final String COMPANY_REP_CSV_PATH = "data/Company_Representative_List.csv";
    public static final String OPPORTUNITY_CSV_PATH = "data/Internship_Opportunity_List.csv";
    public static final String APPLICATION_CSV_PATH = "data/Internship_Applications_List.csv";
    public static final String WITHDRAWAL_CSV_PATH = "data/Internship_Withdrawal_Request_List.csv";
    
    // Private constructor to prevent instantiation
    private DataConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
