package edu.ntu.ccds.sc2002.internship.service.interfaces;

import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.Filter;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.model.Report;
import edu.ntu.ccds.sc2002.internship.enums.Status;

import java.util.List;

/**
 * Service interface for Career Staff business operations.
 * Follows Single Responsibility and Interface Segregation Principles.
 */
public interface ICareerStaffService {
    /**
     * Get all pending company representative registrations
     */
    List<CompanyRepresentative> getPendingCompanyReps();

    /**
     * Approve or reject a company representative account
     */
    OperationResult authorizeCompanyRep(String staffId, String repId, boolean approved);

    /**
     * Get all internship opportunities
     */
    List<InternshipOpportunity> getAllOpportunities();

    /**
     * Get pending internship opportunities
     */
    List<InternshipOpportunity> getPendingOpportunities();

    /**
     * Approve or reject an internship opportunity
     */
    OperationResult approveOpportunity(String staffId, String opportunityId, boolean approved);

    /**
     * Get all withdrawal requests
     */
    List<InternshipApplication> getWithdrawalRequests();

    /**
     * Get pending withdrawal requests
     */
    List<InternshipApplication> getPendingWithdrawals();

    /**
     * Approve or reject a withdrawal request
     */
    OperationResult approveWithdrawal(String staffId, String applicationId, boolean approved);

    /**
     * Generate a report based on filter criteria
     */
    Report generateReport(String staffId, Filter filter);

    /**
     * Change staff password
     */
    OperationResult changePassword(String staffId, String oldPassword, String newPassword);
}
