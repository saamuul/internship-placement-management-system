package edu.ntu.ccds.sc2002.internship.repository.interfaces;

import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;

/**
 * Repository interface for Withdrawal Request data access operations.
 * Follows Interface Segregation Principle and Dependency Inversion Principle.
 */
public interface IWithdrawalRepository {
    /**
     * Find a withdrawal request by application ID
     */
    Optional<InternshipApplication> findById(String applicationId);

    /**
     * Find all withdrawal requests
     */
    List<InternshipApplication> findAll();

    /**
     * Find withdrawal requests by status
     */
    List<InternshipApplication> findByStatus(Status status);

    /**
     * Save or update a withdrawal request
     */
    boolean save(InternshipApplication withdrawal);

    /**
     * Update withdrawal request status
     */
    boolean updateStatus(String applicationId, Status status);

    /**
     * Check if withdrawal request exists for an application
     */
    boolean exists(String applicationId, String studentId);
}
