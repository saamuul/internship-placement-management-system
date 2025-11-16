package edu.ntu.ccds.sc2002.internship.repository.interfaces;

import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.enums.Status;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;

/**
 * Repository interface for Internship Application data access operations.
 * Follows Interface Segregation Principle and Dependency Inversion Principle.
 */
public interface IApplicationRepository {
    /**
     * Find an application by ID
     */
    Optional<InternshipApplication> findById(String applicationId);

    /**
     * Find all applications
     */
    List<InternshipApplication> findAll();

    /**
     * Find applications by student ID
     */
    List<InternshipApplication> findByStudentId(String studentId);

    /**
     * Find applications by internship ID
     */
    List<InternshipApplication> findByInternshipId(String internshipId);

    /**
     * Find applications by status
     */
    List<InternshipApplication> findByStatus(Status status);

    /**
     * Save or update an application
     */
    boolean save(InternshipApplication application);

    /**
     * Update application status
     */
    boolean updateStatus(String applicationId, Status status);

    /**
     * Delete an application
     */
    boolean delete(String applicationId);

    /**
     * Delete applications matching a condition
     */
    int deleteByStudentExcept(String studentId, String exceptApplicationId);

    /**
     * Get next available application ID
     */
    String getNextApplicationId();
}
