package edu.ntu.ccds.sc2002.internship.repository.interfaces;

import java.util.List;
import java.util.Optional;

import edu.ntu.ccds.sc2002.internship.model.CareerStaff;
import edu.ntu.ccds.sc2002.internship.model.CompanyRepresentative;
import edu.ntu.ccds.sc2002.internship.model.Student;
import edu.ntu.ccds.sc2002.internship.model.User;

/**
 * Repository interface for User data access operations.
 * Follows Interface Segregation Principle and Dependency Inversion Principle.
 */
public interface IUserRepository {
    /**
     * Find a user by their ID (email or user ID)
     */
    Optional<User> findById(String userId);

    /**
     * Find all students
     */
    List<Student> findAllStudents();

    /**
     * Find a student by ID
     */
    Optional<Student> findStudentById(String studentId);

    /**
     * Find all company representatives
     */
    List<CompanyRepresentative> findAllCompanyReps();

    /**
     * Find all career staff
     */
    List<CareerStaff> findAllCareerStaff();

    /**
     * Save or update a user
     */
    boolean save(User user);

    /**
     * Update user password
     */
    boolean updatePassword(String userId, String newPassword);

    /**
     * Get the accepted internship ID for a student
     * 
     * @return accepted internship ID or null if none
     */
    String getAcceptedInternshipId(String studentId);

    /**
     * Add an application ID to student's applied internships list
     */
    boolean addAppliedInternship(String studentId, String applicationId);

    /**
     * Set student's accepted internship and clear applied internships
     */
    boolean setAcceptedInternship(String studentId, String applicationId);

    /**
     * Clear student's internship records (both applied and accepted)
     */
    boolean clearStudentInternships(String studentId);
}
