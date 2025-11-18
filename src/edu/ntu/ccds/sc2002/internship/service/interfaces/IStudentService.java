package edu.ntu.ccds.sc2002.internship.service.interfaces;

import edu.ntu.ccds.sc2002.internship.model.Internship;
import edu.ntu.ccds.sc2002.internship.model.InternshipApplication;
import edu.ntu.ccds.sc2002.internship.model.Interview;
import edu.ntu.ccds.sc2002.internship.dto.OperationResult;

import java.util.List;

/**
 * Service interface for Student-related business operations.
 * Follows Single Responsibility and Interface Segregation Principles.
 */
public interface IStudentService {
    /**
     * Get all visible internship opportunities for a student
     */
    List<Internship> getAvailableInternships(String studentId);

    /**
     * Apply for an internship
     */
    OperationResult applyForInternship(String studentId, String internshipId);

    /**
     * Get all applications for a student
     */
    List<InternshipApplication> getApplications(String studentId);

    /**
     * Accept an internship offer
     */
    OperationResult acceptInternship(String studentId, String applicationId);

    /**
     * Get the accepted internship for a student
     */
    InternshipApplication getAcceptedInternship(String studentId);

    /**
     * Submit a withdrawal request for an application
     */
    OperationResult withdrawApplication(String studentId, String applicationId);

    /**
     * Change student password
     */
    OperationResult changePassword(String studentId, String oldPassword, String newPassword);

    boolean proposeInterview(String studentId, String internshipId, String proposedTime);
    List<Interview> getStudentInterviews(String studentId);
}
