package edu.ntu.ccds.sc2002.internship.repository.interfaces;

import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.InternshipOpportunity;

/**
 * Repository interface for Internship Opportunity data access operations.
 * Follows Interface Segregation Principle and Dependency Inversion Principle.
 */
public interface IInternshipRepository {
    /**
     * Find an internship opportunity by ID
     */
    Optional<InternshipOpportunity> findById(String internshipId);

    /**
     * Find all internship opportunities
     */
    List<InternshipOpportunity> findAll();

    /**
     * Find opportunities by status
     */
    List<InternshipOpportunity> findByStatus(Status status);

    /**
     * Find opportunities created by a specific company representative
     */
    List<InternshipOpportunity> findByRepresentativeId(String repId);

    /**
     * Save or update an internship opportunity
     */
    boolean save(InternshipOpportunity opportunity);

    /**
     * Update opportunity status
     */
    boolean updateStatus(String opportunityId, Status status);

    /**
     * Update opportunity visibility
     */
    boolean updateVisibility(String opportunityId, boolean visible);

    /**
     * Decrement available slots for an opportunity (when application is approved)
     * Also updates status to FILLED if slots reach 0
     */
    boolean decrementSlot(String opportunityId);

    /**
     * Increment available slots for an opportunity (when withdrawal is approved)
     * Also updates status from FILLED to SUCCESSFUL if slots become available
     */
    boolean incrementSlot(String opportunityId);

    /**
     * Delete opportunities that have passed their closing date
     * 
     * @return number of opportunities deleted
     */
    int deleteExpiredOpportunities();
    
    /**
     * Delete an internship opportunity by ID
     * 
     * @param opportunityId the ID of the opportunity to delete
     * @return true if deleted successfully, false otherwise
     */
    boolean deleteOpportunity(String opportunityId);
}
