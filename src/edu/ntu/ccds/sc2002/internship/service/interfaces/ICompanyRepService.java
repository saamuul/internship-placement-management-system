package edu.ntu.ccds.sc2002.internship.service.interfaces;

import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;
import edu.ntu.ccds.sc2002.internship.enums.Level;
import edu.ntu.ccds.sc2002.internship.dto.OperationResult;
import edu.ntu.ccds.sc2002.internship.enums.Status;

import java.util.List;

/**
 * Service interface for Company Representative business operations.
 * Follows Single Responsibility and Interface Segregation Principles.
 */
public interface ICompanyRepService {
    /**
     * Create a new internship opportunity
     */
    OperationResult createOpportunity(String repId, String title, String description,
            Level level, String major, String openDate,
            String closeDate, int slots);

    /**
     * Get all opportunities created by a representative
     */
    List<InternshipOpportunity> getCreatedOpportunities(String repId);

    /**
     * Toggle visibility of an internship opportunity
     */
    OperationResult toggleVisibility(String repId, String opportunityId, boolean visible);

    /**
     * Get all applications for opportunities created by this rep
     */
    List<InternshipApplication> getApplicationsForMyOpportunities(String repId);

    /**
     * Get pending applications for opportunities created by this rep
     */
    List<InternshipApplication> getPendingApplications(String repId);

    /**
     * Review an application (approve/reject)
     */
    OperationResult reviewApplication(String repId, String applicationId, Status newStatus);

    /**
     * Change representative password
     */
    OperationResult changePassword(String repId, String oldPassword, String newPassword);
}
